package com.capstone.mint.web.dto;

import com.capstone.mint.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String userName;
    private String commentText;
    private Long commentReg;
    private boolean isWritten;

    public static CommentResponseDto of(Comment comment, boolean bool) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .userName(comment.getUser().getUserName())
                .commentText(comment.getCommentText())
                .commentReg(Timestamp.valueOf(comment.getCommentReg()).getTime())
                .isWritten(bool)
                .build();
    }
}
