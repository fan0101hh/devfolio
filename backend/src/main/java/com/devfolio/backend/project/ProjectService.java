package com.devfolio.backend.project;

import com.devfolio.backend.common.exception.BadRequestException;
import com.devfolio.backend.common.exception.ResourceNotFoundException;
import com.devfolio.backend.common.model.ContentStatus;
import com.devfolio.backend.common.util.SlugValidator;
import com.devfolio.backend.project.dto.ProjectDetailResponse;
import com.devfolio.backend.project.dto.ProjectRequest;
import com.devfolio.backend.project.dto.ProjectSummaryResponse;
import com.devfolio.backend.tag.TagService;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TagService tagService;

    public ProjectService(ProjectRepository projectRepository, TagService tagService) {
        this.projectRepository = projectRepository;
        this.tagService = tagService;
    }

    @Transactional(readOnly = true)
    public List<ProjectSummaryResponse> listPublished() {
        return projectRepository.findAllByStatusOrderByFeaturedDescPublishedAtDesc(ContentStatus.PUBLISHED).stream()
                .map(ProjectMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getPublishedBySlug(String slug) {
        return projectRepository.findBySlugAndStatus(slug, ContentStatus.PUBLISHED)
                .map(ProjectMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
    }

    @Transactional(readOnly = true)
    public List<ProjectSummaryResponse> listAllForAdmin() {
        return projectRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(ProjectMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getById(Long id) {
        return projectRepository.findById(id)
                .map(ProjectMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
    }

    @Transactional
    public ProjectDetailResponse create(ProjectRequest request) {
        validateUniqueSlug(null, request.slug());
        SlugValidator.requireValid(request.slug(), "项目 slug");

        Project project = new Project();
        applyRequest(project, request);
        return ProjectMapper.toDetailResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectDetailResponse update(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
        validateUniqueSlug(id, request.slug());
        SlugValidator.requireValid(request.slug(), "项目 slug");

        applyRequest(project, request);
        return ProjectMapper.toDetailResponse(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("项目不存在");
        }
        projectRepository.deleteById(id);
    }

    private void applyRequest(Project project, ProjectRequest request) {
        project.setName(request.name().trim());
        project.setSlug(request.slug().trim());
        project.setSummary(request.summary().trim());
        project.setDescription(request.description().trim());
        project.setRepoUrl(trimToNull(request.repoUrl()));
        project.setDemoUrl(trimToNull(request.demoUrl()));
        project.setFeatured(request.featured());
        project.setStatus(request.status());
        project.setTags(tagService.getTagsByIds(request.tagIds()));
        project.setPublishedAt(resolvePublishedAt(request.status(), request.publishedAt()));
    }

    private Instant resolvePublishedAt(ContentStatus status, Instant publishedAt) {
        if (status == ContentStatus.PUBLISHED) {
            return publishedAt != null ? publishedAt : Instant.now();
        }
        return null;
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private void validateUniqueSlug(Long currentId, String slug) {
        boolean exists = currentId == null
                ? projectRepository.existsBySlug(slug.trim())
                : projectRepository.existsBySlugAndIdNot(slug.trim(), currentId);
        if (exists) {
            throw new BadRequestException("项目 slug 已存在");
        }
    }
}

