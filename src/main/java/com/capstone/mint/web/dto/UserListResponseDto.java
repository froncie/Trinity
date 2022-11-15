package com.capstone.mint.web.dto;

import com.capstone.mint.domain.user.Role;
import com.capstone.mint.domain.user.User;
import lombok.Getter;

@Getter
public class UserListResponseDto {

    private Long userId;
    private String userName;
    private String userEmail;
    private Role userRole;

    public UserListResponseDto(User entity) {
        this.userId = entity.getUserId();
        this.userName = entity.getUserName();
        this.userEmail = entity.getUserEmail();
        this.userRole = entity.getUserRole();
    }
}
