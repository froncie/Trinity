package com.capstone.mint.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    private String name;
    private String email;
    private String pwd;

    @Builder
    public UserUpdateRequestDto(String name, String email, String pwd) {
        this.name = name;
        this.email = email;
        this.pwd = pwd;
    }
}
