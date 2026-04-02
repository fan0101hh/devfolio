package com.devfolio.backend.admin;

import com.devfolio.backend.article.ArticleService;
import com.devfolio.backend.article.dto.ArticleDetailResponse;
import com.devfolio.backend.article.dto.ArticleRequest;
import com.devfolio.backend.article.dto.ArticleSummaryResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {

    private final ArticleService articleService;

    public AdminArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleSummaryResponse> list() {
        return articleService.listAllForAdmin();
    }

    @GetMapping("/{id}")
    public ArticleDetailResponse get(@PathVariable Long id) {
        return articleService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDetailResponse create(@Valid @RequestBody ArticleRequest request) {
        return articleService.create(request);
    }

    @PutMapping("/{id}")
    public ArticleDetailResponse update(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
        return articleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        articleService.delete(id);
    }
}

