package com.capstone.mint.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository <Board, Long> {

    @Query("SELECT p FROM Board p ORDER BY p.board_id DESC")
    List<Board> findAllDesc();
}