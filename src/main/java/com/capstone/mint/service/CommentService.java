package com.capstone.mint.service;

import com.capstone.mint.config.SecurityUtilConfig;
import com.capstone.mint.domain.comment.Comment;
import com.capstone.mint.domain.comment.CommentRepository;
import com.capstone.mint.domain.post.Post;
import com.capstone.mint.domain.post.PostRepository;
import com.capstone.mint.domain.user.User;
import com.capstone.mint.domain.user.UserRepository;
import com.capstone.mint.web.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<CommentResponseDto> getComment(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("댓글이 없습니다."));
        List<Comment> comments = commentRepository.findAllByPost(post);
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "aanonymousUser") {
            return comments
                    .stream()
                    .map(comment -> CommentResponseDto.of(comment, false))
                    .collect(Collectors.toList());
        } else {
            User user = userRepository.findById(Long.parseLong(authentication.getName())).orElseThrow();
            Map<Boolean, List<Comment>> collect = comments.stream()
                    .collect(Collectors.partitioningBy(comment -> comment.getUser().equals(user)));
            List<CommentResponseDto> tCollect = collect.get(true).stream()
                    .map(t -> CommentResponseDto.of(t, true))
                    .collect(Collectors.toList());
            List<CommentResponseDto> fCollect = collect.get(false).stream()
                    .map(f -> CommentResponseDto.of(f, false))
                    .collect(Collectors.toList());

            return Stream
                    .concat(tCollect.stream() ,fCollect.stream())
                    .sorted(Comparator.comparing(CommentResponseDto::getCommentId))
                    .collect(Collectors.toList());
        }
    } // getComment

    @Transactional
    public CommentResponseDto createComment(Long id, String text) {
        User user = userRepository.findById(SecurityUtilConfig.getCurrentUserId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        Comment comment = Comment.builder()
                .text(text)
                .post(post)
                .user(user)
                .build();

        return CommentResponseDto.of(commentRepository.save(comment), true);
    } // createComment

    @Transactional
    public void removeComment(Long id) {
        User user = userRepository.findById(SecurityUtilConfig.getCurrentUserId()).orElseThrow(() -> new RuntimeException("로그인 하세요."));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("댓글이 없습니다."));
        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("작성자와 로그인이 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
    } // removeComment
}
