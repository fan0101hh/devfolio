package com.devfolio.backend.publicapi;

import com.devfolio.backend.article.ArticleService;
import com.devfolio.backend.article.dto.ArticleDetailResponse;
import com.devfolio.backend.article.dto.ArticleSummaryResponse;
import com.devfolio.backend.project.ProjectService;
import com.devfolio.backend.project.dto.ProjectDetailResponse;
import com.devfolio.backend.project.dto.ProjectSummaryResponse;
import com.devfolio.backend.tag.TagService;
import com.devfolio.backend.tag.dto.TagResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicContentController {

    private final ArticleService articleService;
    private final ProjectService projectService;
    private final TagService tagService;

    public PublicContentController(
            ArticleService articleService,
            ProjectService projectService,
            TagService tagService
    ) {
        this.articleService = articleService;
        this.projectService = projectService;
        this.tagService = tagService;
    }

    @GetMapping("/articles")
    public List<ArticleSummaryResponse> listArticles() {
        return articleService.listPublished();
    }

    @GetMapping("/articles/{slug}")
    public ArticleDetailResponse getArticle(@PathVariable String slug) {
        return articleService.getPublishedBySlug(slug);
    }

    @GetMapping("/projects")
    public List<ProjectSummaryResponse> listProjects() {
        return projectService.listPublished();
    }

    @GetMapping("/projects/{slug}")
    public ProjectDetailResponse getProject(@PathVariable String slug) {
        return projectService.getPublishedBySlug(slug);
    }

    @GetMapping("/tags")
    public List<TagResponse> listTags() {
        return tagService.listAll();
    }
}

