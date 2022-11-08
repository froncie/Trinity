package com.capstone.mint.controller;

import com.capstone.mint.domain.board.Board;
import com.capstone.mint.domain.board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardController(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @GetMapping("/boards")
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

}