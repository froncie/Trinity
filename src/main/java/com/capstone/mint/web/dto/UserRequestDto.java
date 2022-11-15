package com.capstone.mint.web.dto;

import com.capstone.mint.domain.user.Role;
import com.capstone.mint.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    private String userEmail;
    private String userPwd;
    private String userName;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .userEmail(userEmail)
                .userPwd(passwordEncoder.encode(userPwd))
                .userName(userName)
                .userRole(Role.USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userEmail, userPwd);
    }
}
