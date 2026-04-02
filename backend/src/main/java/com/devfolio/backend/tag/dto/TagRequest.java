package com.devfolio.backend.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagRequest(
        @NotBlank(message = "标签名称不能为空")
        @Size(max = 64, message = "标签名称不能超过64个字符")
        String name,
        @NotBlank(message = "标签 slug 不能为空")
        @Size(max = 64, message = "标签 slug 不能超过64个字符")
        String slug
) {
}

