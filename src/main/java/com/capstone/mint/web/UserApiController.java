package com.capstone.mint.web;

import com.capstone.mint.service.UserService;
import com.capstone.mint.web.dto.UserListResponseDto;
import com.capstone.mint.web.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    // 로그인 페이지 매핑, 실패나 예외 처리 값을 model에 반환
    @GetMapping("/login")
    public String getLoginPage(Model model,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "exception", required = false) String exception) {

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/member/login";
    }

    @PutMapping("/api/user/update/{id}")
    public Long update(@PathVariable Long id, @RequestBody UserUpdateRequestDto requestDto) {
        return userService.update(id, requestDto);
    }

    @GetMapping("/api/user/all")
    public List<UserListResponseDto> allUser(){
        List<UserListResponseDto> userList = userService.findAllUser();
        return userList;
    }
}
