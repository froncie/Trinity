package com.capstone.mint.web.dto;

import com.capstone.mint.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class PageResponseDto {

    private Long postId;
    private String postTitle;
    private String userName;
    private String postReg;

    // Post 객체를 DTO로 변환 시키는 Builder 메소드
    public static PageResponseDto of(Post post) {
        return PageResponseDto.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .userName(post.getUser().getUserName())
                .postReg(post.getPostReg().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
