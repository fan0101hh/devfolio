package com.devfolio.backend.project.dto;

import com.devfolio.backend.common.model.ContentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

public record ProjectRequest(
        @NotBlank(message = "项目名称不能为空")
        @Size(max = 180, message = "项目名称不能超过180个字符")
        String name,
        @NotBlank(message = "项目 slug 不能为空")
        @Size(max = 180, message = "项目 slug 不能超过180个字符")
        String slug,
        @NotBlank(message = "项目摘要不能为空")
        @Size(max = 320, message = "项目摘要不能超过320个字符")
        String summary,
        @NotBlank(message = "项目描述不能为空")
        String description,
        @Size(max = 255, message = "仓库地址不能超过255个字符")
        String repoUrl,
        @Size(max = 255, message = "演示地址不能超过255个字符")
        String demoUrl,
        boolean featured,
        @NotNull(message = "项目状态不能为空")
        ContentStatus status,
        Instant publishedAt,
        @NotNull(message = "标签列表不能为 null")
        List<Long> tagIds
) {
}

