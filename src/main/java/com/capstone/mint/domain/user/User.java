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
@Builder
@Getter
public class User {

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

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public User nameUpdate(String name, String email) {
        this.userName = name;
        this.userEmail = email;

        return this;
    }
    @Builder
    public User(Long userId, String userName, String userEmail, String userPwd, Role userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPwd = userPwd;
        this.userRole = userRole;
    }
}
