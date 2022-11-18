package com.capstone.mint.web.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String postBody;
}
