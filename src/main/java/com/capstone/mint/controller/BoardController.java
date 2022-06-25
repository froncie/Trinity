package com.capstone.mint.controller;

import com.capstone.mint.entity.Board;
import com.capstone.mint.entity.BoardRepository;
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

    @GetMapping("/findall")
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

}