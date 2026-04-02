package com.devfolio.backend.project;

import com.devfolio.backend.project.dto.ProjectDetailResponse;
import com.devfolio.backend.project.dto.ProjectSummaryResponse;
import com.devfolio.backend.tag.TagMapper;

public final class ProjectMapper {

    private ProjectMapper() {
    }

    public static ProjectSummaryResponse toSummaryResponse(Project project) {
        return new ProjectSummaryResponse(
                project.getId(),
                project.getName(),
                project.getSlug(),
                project.getSummary(),
                project.getRepoUrl(),
                project.getDemoUrl(),
                project.isFeatured(),
                project.getStatus(),
                project.getPublishedAt(),
                project.getUpdatedAt(),
                project.getTags().stream().map(TagMapper::toResponse).toList()
        );
    }

    public static ProjectDetailResponse toDetailResponse(Project project) {
        return new ProjectDetailResponse(
                project.getId(),
                project.getName(),
                project.getSlug(),
                project.getSummary(),
                project.getDescription(),
                project.getRepoUrl(),
                project.getDemoUrl(),
                project.isFeatured(),
                project.getStatus(),
                project.getPublishedAt(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                project.getTags().stream().map(TagMapper::toResponse).toList()
        );
    }
}

