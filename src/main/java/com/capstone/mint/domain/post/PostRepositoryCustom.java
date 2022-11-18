package com.capstone.mint.domain.post;

import com.capstone.mint.web.dto.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PageResponseDto> searchAll(Pageable pageable);
}
