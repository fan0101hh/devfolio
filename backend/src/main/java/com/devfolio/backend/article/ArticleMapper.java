package com.devfolio.backend.article;

import com.devfolio.backend.article.dto.ArticleDetailResponse;
import com.devfolio.backend.article.dto.ArticleSummaryResponse;
import com.devfolio.backend.tag.TagMapper;

public final class ArticleMapper {

    private ArticleMapper() {
    }

    public static ArticleSummaryResponse toSummaryResponse(Article article) {
        return new ArticleSummaryResponse(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getSummary(),
                article.getStatus(),
                article.getPublishedAt(),
                article.getUpdatedAt(),
                article.getTags().stream().map(TagMapper::toResponse).toList()
        );
    }

    public static ArticleDetailResponse toDetailResponse(Article article) {
        return new ArticleDetailResponse(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getSummary(),
                article.getContent(),
                article.getStatus(),
                article.getPublishedAt(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getTags().stream().map(TagMapper::toResponse).toList()
        );
    }
}

