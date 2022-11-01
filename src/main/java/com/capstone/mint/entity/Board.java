package com.capstone.mint.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table (name = "board")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class Board {

    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long board_id;

    @Column (name = "board_writer")
    private String board_writer;

    @Column (name = "board_title")
    private String board_title;

    @Column (name = "board_content")
    private String board_content;

    @Column (name = "board_regdate")
    private LocalDateTime board_regdate;

    @Column (name = "board_updatedate", nullable = true)
    private LocalDateTime board_updatedate;

    @Column (name = "board_deletedate", nullable = true)
    private LocalDateTime board_deletedate;

}