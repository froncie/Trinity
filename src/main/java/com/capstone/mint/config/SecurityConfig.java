package com.capstone.mint.config;

import com.capstone.mint.domain.user.Role;
import com.capstone.mint.handler.LoginFailureHandler;
import com.capstone.mint.handler.LoginSuccessHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// csrf 토큰 비활성화
                .authorizeRequests() // URL별 권한 관리 설정
                    .antMatchers("/", "/login/**", "/js/**", "/css/**", "/image/**").permitAll() // 접근 허용
                    .antMatchers("/api/**").hasRole("USER") // /api/** 주소를 가진 API는 USER권한 이상
                    .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight Request 허용
                    .anyRequest().authenticated() // 설정된 값 이외 나머지 URL은 인증된 사용자만 허용
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login/action") // 해당 URL로 요청이 오면 스프링 시큐리티가 가로채서 처리 -> loadUserByName
                    .successHandler(loginSuccessHandler) // 로그인 성공 시 요청 처리할 핸들러
                    .failureHandler(loginFailureHandler) // 로그인 실패 시 요청 처리할 핸들러
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 URL
                    .logoutSuccessUrl("/") // 로그아웃 성공 시 리다이렉트 주소
                    .invalidateHttpSession(true) // 세션 날리기
                    .deleteCookies("JSESSIONID", "remember-me")
                .and()
                .sessionManagement() // 세션
                    .maximumSessions(1) // 세션 최대 허용 수, -1의 경우 무제한
                    .maxSessionsPreventsLogin(false) // 중복 로그인 설정
                    .expiredUrl("/") // 세션 만료 시 리다이렉트 URL
                .and()
                    .and().rememberMe() // 로그인 유지
                    .alwaysRemember(false) // 항상 기억할 것인지 여부
                    .tokenValiditySeconds(43200) // in seconds, 토큰 12시간 유지
                    .rememberMeParameter("remember-me")
        ;
    }
}
