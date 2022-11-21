package com.capstone.mint.service;

import com.capstone.mint.config.SecurityUtilConfig;
import com.capstone.mint.domain.user.User;
import com.capstone.mint.domain.user.UserRepository;
import com.capstone.mint.web.dto.UserListResponseDto;
import com.capstone.mint.web.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getMyInfoBySecurity() {
        return userRepository.findById(SecurityUtilConfig.getCurrentUserId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    // id로 유저 정보를 찾고, name / email / pwd 업데이트
    @Transactional
    public UserResponseDto nameUpdate(Long id, String name, String email) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id = " + id));
        user.nameUpdate(name, email);
        return UserResponseDto.of(userRepository.save(user));
    }

    // TOKEN을 토대로 USER를 찾아 제시된 예전 패스워드와 DB를 비교한다.
    @Transactional
    public UserResponseDto pwdUpdate(String email, String exPassword, String newPassword) {
        User user = userRepository.findById(SecurityUtilConfig.getCurrentUserId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, user.getUserPwd())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        user.setUserPwd(passwordEncoder.encode((newPassword)));
        return UserResponseDto.of(userRepository.save(user));
    }

    @Transactional
    public List<UserListResponseDto> findAllUser() {
        return userRepository.findAll().stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByUserEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("DB에서 찾을 수 없습니다 : " + email));
    } // loadUserByUsername

    private UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority  = new SimpleGrantedAuthority(user.getUserRole().toString());

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getUserId()),
                user.getUserPwd(),
                Collections.singleton(grantedAuthority)
        );
    } // createUserDetails

}
