package com.capstone.mint.config;

import com.capstone.mint.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable() // h2-console을 사용하기 위한 설정
                .and()
                .authorizeRequests() // URL별 권한 관리 설정
                    .antMatchers("/api/**").hasRole(Role.USER.name()) // /api/** 주소를 가진 API는 USER권한 이상
                    .anyRequest().authenticated() // 설정된 값 이외 나머지 URL은 인증된 사용자만 허용
                .and()
                .logout()
                    .logoutSuccessUrl("/") // 로그아웃 성공 시 리다이렉트 주소
                    .invalidateHttpSession(true) // 세션 날리기
                .and()
                    .formLogin()
        ;
    }
}
