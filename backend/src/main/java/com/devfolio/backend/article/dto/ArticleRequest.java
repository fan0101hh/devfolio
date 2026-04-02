package com.devfolio.backend.article.dto;

import com.devfolio.backend.common.model.ContentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

public record ArticleRequest(
        @NotBlank(message = "文章标题不能为空")
        @Size(max = 180, message = "文章标题不能超过180个字符")
        String title,
        @NotBlank(message = "文章 slug 不能为空")
        @Size(max = 180, message = "文章 slug 不能超过180个字符")
        String slug,
        @NotBlank(message = "文章摘要不能为空")
        @Size(max = 320, message = "文章摘要不能超过320个字符")
        String summary,
        @NotBlank(message = "文章内容不能为空")
        String content,
        @NotNull(message = "文章状态不能为空")
        ContentStatus status,
        Instant publishedAt,
        @NotNull(message = "标签列表不能为 null")
        List<Long> tagIds
) {
}

