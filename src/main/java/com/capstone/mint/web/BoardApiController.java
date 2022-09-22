package com.capstone.mint.web;

import com.capstone.mint.domain.board.Board;
import com.capstone.mint.domain.board.BoardRepository;
import com.capstone.mint.service.BoardService;
import com.capstone.mint.web.dto.BoardResponseDto;
import com.capstone.mint.web.dto.BoardSaveRequestsDto;
import com.capstone.mint.web.dto.BoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("/api/board/save")
    public Long save(@RequestBody BoardSaveRequestsDto requestsDto) {
        return boardService.save(requestsDto);
    }

    @PutMapping("/api/board/{id}")
        public Long update(@PathVariable Long id, @RequestBody BoardUpdateRequestDto requestDto) {
            return boardService.update(id, requestDto);
    }

    @GetMapping("/api/board/{id}")
    public BoardResponseDto findById (@PathVariable Long id) {
        return boardService.findById(id);
    }

    }