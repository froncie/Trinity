package com.capstone.mint.domain.board;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository <Board, Long> {

    @Query("SELECT p FROM Board p ORDER BY p.id DESC")
    List<Board> findAllDesc();
}