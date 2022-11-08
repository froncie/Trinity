package com.capstone.mint.domain.user;


import com.capstone.mint.domain.BaseTimeEntity;
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
    private Long user_id;

    @Column(name = "user_name", nullable = false)
    private String user_name;

    @Column(name = "user_email",nullable = false)
    private String user_email;

    @Column(name = "user_pwd",nullable = false)
    private String user_pwd;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role",nullable = false)
    private Role user_role;

    public User update(String name, String email, String pwd) {
        this.user_name = name;
        this.user_email = email;
        this.user_pwd = pwd;

        return this;
    }

    @Override
    public String getUsername() {
        return this.user_email;
    }

    @Override
    public String getPassword() {
        return this.user_pwd;
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

    // 계정이 사용 가능한지 반 (true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRoleKey() {
        return this.user_role.getKey();
    }

}
