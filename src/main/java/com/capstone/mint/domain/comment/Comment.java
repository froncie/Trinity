package com.capstone.mint.domain.comment;

import com.capstone.mint.domain.post.Post;
import com.capstone.mint.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "comment_text", nullable = false, columnDefinition = "TEXT")
    private String commentText;

    @CreationTimestamp // Hibernate에서 엔티티 객체에 대해 INSERT, UPDATE 등의 쿼리가 발생할 때, 현재 시간을 자동으로 저장
    @Column(name = "comment_reg")
    private LocalDateTime commentReg = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(String text, User user, Post post) {
        this.commentText = text;
        this.user = user;
        this.post = post;
    }
}
