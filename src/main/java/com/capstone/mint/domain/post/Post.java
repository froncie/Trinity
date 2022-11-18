package com.capstone.mint.domain.post;

import com.capstone.mint.domain.comment.Comment;
import com.capstone.mint.domain.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "post")
@Getter
public class Post {

    @Id // Primary Key
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column (name = "post_title", nullable = false)
    private String postTitle;

    @Column (name = "post_body", nullable = false, columnDefinition = "TEXT")
    private String postBody;

    @CreationTimestamp // Hibernate에서 엔티티 객체에 대해 INSERT, UPDATE 등의 쿼리가 발생할 때, 현재 시간을 자동으로 저장
    @Column(name = "post_reg")
    private LocalDateTime postReg = LocalDateTime.now();

    @UpdateTimestamp // Hibernate에서 엔티티 객체에 대해 INSERT, UPDATE 등의 쿼리가 발생할 때, 현재 시간을 자동으로 저장
    @Column(name = "post_update")
    private LocalDateTime postUpdate = LocalDateTime.now();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public static Post createPost (String title, String body, User user) {
        Post post = new Post();
        post.postTitle = title;
        post.postBody = body;
        post.user = user;

        return post;
    }

    public static Post changePost (Post post, String title, String body) {
        post.postTitle = title;
        post.postBody = body;

        return post;
    }

}