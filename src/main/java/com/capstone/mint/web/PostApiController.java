package com.capstone.mint.web;

import com.capstone.mint.domain.post.Post;
import com.capstone.mint.service.PostService;
import com.capstone.mint.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class PostApiController {

    private final PostService postService;

    @GetMapping("api/post/page/{page}")
    public ResponseEntity<Page<PageResponseDto>> pagePost(@PathVariable("page") int page) {
        return ResponseEntity.ok(postService.pagePost(page));
    }

    @GetMapping("/api/post/one/{id}")
    public ResponseEntity<PostResponseDto> getOnePost(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.onePost(id));
    }

    @PostMapping("/api/post/create")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody CreatePostRequestDto request) {
        return ResponseEntity.ok(postService.createPost(request.getPostTitle(), request.getPostBody()));
    }

    @GetMapping("api/post/change/{id}")
    public ResponseEntity<PostResponseDto> getChangeArticle(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.onePost((id)));
    }

    @PutMapping("/api/post/change/")
    public ResponseEntity<PostResponseDto> putChangeArticle(@RequestBody ChangePostRequestDto request) {
        return ResponseEntity.ok(postService.changePost(
                request.getPostId(), request.getPostTitle(), request.getPostBody()));
    }

    @DeleteMapping("/api/post/delete/{id}")
    public ResponseEntity<MessageDto> deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(new MessageDto("Success"));
    }



}