package com.capstone.mint.web.dto;

import com.capstone.mint.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {

    private Long postId;
    private String userName;
    private String postTitle;
    private String postBody;
    private String postReg;
    private String postUpdate;
    private boolean isWritten;

    public static PostResponseDto of(Post post, boolean bool) {
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .userName(post.getUser().getUserName())
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postReg(post.getPostReg().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .postUpdate(post.getPostUpdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isWritten(bool)
                .build();
    }

}
