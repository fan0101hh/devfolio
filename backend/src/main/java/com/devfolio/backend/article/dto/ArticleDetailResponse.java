package com.devfolio.backend.article.dto;

import com.devfolio.backend.common.model.ContentStatus;
import com.devfolio.backend.tag.dto.TagResponse;
import java.time.Instant;
import java.util.List;

public record ArticleDetailResponse(
        Long id,
        String title,
        String slug,
        String summary,
        String content,
        ContentStatus status,
        Instant publishedAt,
        Instant createdAt,
        Instant updatedAt,
        List<TagResponse> tags
) {
}

