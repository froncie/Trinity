package com.capstone.mint.web.dto;

import com.capstone.mint.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String userEmail;
    private String userName;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .build();
    }

}
