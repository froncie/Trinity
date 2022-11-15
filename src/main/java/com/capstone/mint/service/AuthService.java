package com.capstone.mint.service;

import com.capstone.mint.domain.user.User;
import com.capstone.mint.domain.user.UserRepository;
import com.capstone.mint.jwt.TokenProvider;
import com.capstone.mint.web.dto.TokenDto;
import com.capstone.mint.web.dto.UserRequestDto;
import com.capstone.mint.web.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponseDto signUp(UserRequestDto requestDto) {

        if (userRepository.existsByUserEmail(requestDto.getUserEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        User user = requestDto.toUser(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    } // signUp

    public TokenDto login(UserRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    } // login

}
