package com.capstone.mint.web.dto;

import com.capstone.mint.domain.user.Role;
import com.capstone.mint.domain.user.User;
import lombok.Getter;

@Getter
public class UserListResponseDto {

    private Long id;
    private String name;
    private String email;
    private Role role;

    public UserListResponseDto(User entity) {
        this.id = entity.getUserId();
        this.name = entity.getUsername();
        this.email = entity.getUserEmail();
        this.role = entity.getUserRole();
    }
}
