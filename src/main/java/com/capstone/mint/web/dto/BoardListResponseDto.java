package com.capstone.mint.web.dto;

import com.capstone.mint.domain.board.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListResponseDto {

    private Long id;
    private String title;
    private String writer;
    private LocalDateTime modifiedDate;

    public BoardListResponseDto(Board entity) {
        this.id = entity.getBoard_id();
        this.title = entity.getBoard_title();
        this.writer = entity.getBoard_writer();
        this.modifiedDate = entity.getModifiedDate();
    }

}
