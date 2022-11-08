package com.capstone.mint.web.dto;

import com.capstone.mint.domain.board.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;

    public BoardResponseDto(Board entity) {
        this.id = entity.getBoard_id();
        this.title = entity.getBoard_title();
        this.content = entity.getBoard_content();
        this.writer = entity.getBoard_writer();
    }
}
