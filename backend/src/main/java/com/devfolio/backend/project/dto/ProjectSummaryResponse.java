package com.devfolio.backend.project.dto;

import com.devfolio.backend.common.model.ContentStatus;
import com.devfolio.backend.tag.dto.TagResponse;
import java.time.Instant;
import java.util.List;

public record ProjectSummaryResponse(
        Long id,
        String name,
        String slug,
        String summary,
        String repoUrl,
        String demoUrl,
        boolean featured,
        ContentStatus status,
        Instant publishedAt,
        Instant updatedAt,
        List<TagResponse> tags
) {
}

