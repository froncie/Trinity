package com.capstone.mint.service;

import com.capstone.mint.domain.board.Board;
import com.capstone.mint.domain.board.BoardRepository;
import com.capstone.mint.web.dto.BoardListResponseDto;
import com.capstone.mint.web.dto.BoardResponseDto;
import com.capstone.mint.web.dto.BoardSaveRequestsDto;
import com.capstone.mint.web.dto.BoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardSaveRequestsDto requestsDto) {
        return boardRepository.save(requestsDto.toEntity()).getBoard_id();
    }

    @Transactional
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        board.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional
    public void delete (Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        boardRepository.delete(board); // 존재하는 게시글인지 확인을 위해 조회 후 삭제
    }

    public BoardResponseDto findById (Long id) {
        Board entity = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        return new BoardResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> findAllDesc() {
        return boardRepository.findAllDesc().stream()
                .map(BoardListResponseDto::new)
                .collect(Collectors.toList());
    }
}
