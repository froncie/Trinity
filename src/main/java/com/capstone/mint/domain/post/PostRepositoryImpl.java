package com.capstone.mint.domain.post;

import com.capstone.mint.web.dto.PageResponseDto;
import com.capstone.mint.domain.post.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static  com.capstone.mint.domain.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PageResponseDto> searchAll(Pageable pageable) {

        List<Post> content = queryFactory
                .selectFrom(post)
                .orderBy(post.postId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PageResponseDto> pages = content
                .stream()
                .map(PageResponseDto::of)
                .collect(Collectors.toList());

        int totalSize = queryFactory
                .selectFrom(post)
                .fetch()
                .size();

        return new PageImpl<>(pages, pageable, totalSize);

    } // searchAll

}
