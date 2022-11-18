package com.capstone.mint.web;

import com.capstone.mint.service.CommentService;
import com.capstone.mint.web.dto.CommentRequestDto;
import com.capstone.mint.web.dto.CommentResponseDto;
import com.capstone.mint.web.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;

    @GetMapping("/api/comment/list/{id}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable("id") Long id) {
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @PostMapping("/api/comment/create")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.createComment(request.getPostId(), request.getPostBody()));
    }

    @DeleteMapping("/api/comment/delete/id")
    public ResponseEntity<MessageDto> deleteComment(@PathVariable("id") Long id) {
        commentService.removeComment(id);
        return ResponseEntity.ok(new MessageDto("Success"));
    }

}
