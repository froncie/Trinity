package com.capstone.mint.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    static final String UPDATE_USER_LAST_LOGIN = "UPDATE User SET LAST_LONG_TIME = :lastLoginTime WHERE EMAIL = :email";

    // 로그인 성공 시 최종 로그인 일시를 업데이트할 쿼리
    @Transactional
    @Modifying
    @Query (value = UPDATE_USER_LAST_LOGIN, nativeQuery = true)
    public int updateUserLastLogin(@Param("email") String email, @Param("lastLongTime")LocalDateTime lastLoginTime);

    // Email을 통해 이미 가입된 사용자인지 판단
    Optional<User> findByEmail(String email);

}
