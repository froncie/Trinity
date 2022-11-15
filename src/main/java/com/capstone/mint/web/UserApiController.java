package com.capstone.mint.web;

import java.lang.Long;
import com.capstone.mint.service.UserService;
import com.capstone.mint.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/user/nameUpdate/{id}")
    public ResponseEntity<UserResponseDto> nameUpdate(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.nameUpdate(id, requestDto.getUserName(), requestDto.getUserEmail()));
    }

    @PostMapping("/api/user/pwdUpdate")
    public ResponseEntity<UserResponseDto> pwdUpdate(@RequestBody ChangePasswordRequestDto requestDto) {
        return ResponseEntity.ok(userService.pwdUpdate(requestDto.getUserEmail(), requestDto.getExPassword(), requestDto.getNewPassword()));
    }

    @GetMapping("/api/user/all")
    public List<UserListResponseDto> allUser(){
        List<UserListResponseDto> userList = userService.findAllUser();
        return userList;
    }
}
