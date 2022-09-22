package com.capstone.mint.web.dto;

import com.capstone.mint.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSaveRequestsDto {
    private String title;
    private String content;
    private String writer;

    @Builder
    public BoardSaveRequestsDto(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public Board toEntity() {
        return Board.builder()
                .board_title(title)
                .board_content(content)
                .board_writer(writer)
                .build();
    }
}
