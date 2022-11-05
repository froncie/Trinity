package com.capstone.mint.web;

import com.capstone.mint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
