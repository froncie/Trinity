package com.capstone.mint.service;

import com.capstone.mint.config.SecurityUtilConfig;
import com.capstone.mint.domain.post.Post;
import com.capstone.mint.domain.post.PostRepository;
import com.capstone.mint.domain.user.User;
import com.capstone.mint.domain.user.UserRepository;
import com.capstone.mint.web.dto.PageResponseDto;
import com.capstone.mint.web.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto onePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException(("글이 없습니다.")));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getPrincipal() == "anonymousUser") {
            return PostResponseDto.of(post, false);
        } else {
            User user = userRepository.findById(Long.parseLong(authentication.getName())).orElseThrow();
            boolean result = post.getUser().equals(user);
            return PostResponseDto.of(post, result);
        }
    } // onePost

    public Page<PageResponseDto> pagePost(int pageNum) {
        return postRepository.searchAll(PageRequest.of(pageNum - 1, 20));
    }

    @Transactional
    public PostResponseDto createPost(String title, String body) {
        User user = isUserCurrent();
        Post post = Post.createPost(title, body, user);
        return PostResponseDto.of(postRepository.save(post), true);
    }

    @Transactional
    public PostResponseDto changePost(Long id, String title, String body) {
        Post post = authorizationArticleWriter(id);
        return PostResponseDto.of(postRepository.save(Post.changePost(post, title, body)), true);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = authorizationArticleWriter(id);
        postRepository.delete(post);
    }

    // findAll 메소드를 통해 전체 목록을 얻은 후, DTO 변환
    public List<PageResponseDto> allPost() {
        List<Post> posts = postRepository.findAll();

        return posts
                .stream()
                .map(PageResponseDto::of)
                .collect(Collectors.toList());
    } // allPost

    public User isUserCurrent() {
        return userRepository.findById(SecurityUtilConfig.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public Post authorizationArticleWriter(Long id) {
        User user = isUserCurrent();
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("글이 없습니다."));
        if (!post.getUser().equals(user)) {
            throw new RuntimeException("로그인한 유저와 작성 유저가 같지 않습니다.");
        }
        return post;
    } // isUserCurrent

}
