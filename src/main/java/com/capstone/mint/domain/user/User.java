package com.capstone.mint.domain.user;


import com.capstone.mint.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.capstone.mint.domain.user.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String pwd;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String pwd, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.pwd = pwd;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.pwd;
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
        return this.role.getKey();
    }

}
