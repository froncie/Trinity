package com.capstone.mint.domain.user;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table (name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email",nullable = false)
    private String userEmail;

    @Column(name = "user_pwd",nullable = false)
    private String userPwd;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role",nullable = false)
    private Role userRole;

    public User update(String name, String email, String pwd) {
        this.userName = name;
        this.userEmail = email;
        this.userPwd = pwd;

        return this;
    }

    @Override
    public String getUsername() {
        return this.userEmail;
    }

    @Override
    public String getPassword() {
        return this.userPwd;
    }

    // 계정이 갖고있는 권한 목록 반환
    @Override
    public Collection <? extends GrantedAuthority> getAuthorities() {
        Collection <GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> {
            return "계정별 등록할 권한";
        });
        return collectors;
    }

    // 계정이 만료되지 않았는지 반환 (true : 만료 안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있는지 않았는지 반환 (true : 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는지 반환 (true : 만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 사용 가능한지 반환 (true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRoleKey() {
        return this.userRole.getKey();
    }

}
