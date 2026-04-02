package com.devfolio.backend.article;

import com.devfolio.backend.article.dto.ArticleDetailResponse;
import com.devfolio.backend.article.dto.ArticleRequest;
import com.devfolio.backend.article.dto.ArticleSummaryResponse;
import com.devfolio.backend.common.exception.BadRequestException;
import com.devfolio.backend.common.exception.ResourceNotFoundException;
import com.devfolio.backend.common.model.ContentStatus;
import com.devfolio.backend.common.util.SlugValidator;
import com.devfolio.backend.tag.TagService;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagService tagService;

    public ArticleService(ArticleRepository articleRepository, TagService tagService) {
        this.articleRepository = articleRepository;
        this.tagService = tagService;
    }

    @Transactional(readOnly = true)
    public List<ArticleSummaryResponse> listPublished() {
        return articleRepository.findAllByStatusOrderByPublishedAtDesc(ContentStatus.PUBLISHED).stream()
                .map(ArticleMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponse getPublishedBySlug(String slug) {
        return articleRepository.findBySlugAndStatus(slug, ContentStatus.PUBLISHED)
                .map(ArticleMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));
    }

    @Transactional(readOnly = true)
    public List<ArticleSummaryResponse> listAllForAdmin() {
        return articleRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(ArticleMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponse getById(Long id) {
        return articleRepository.findById(id)
                .map(ArticleMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));
    }

    @Transactional
    public ArticleDetailResponse create(ArticleRequest request) {
        validateUniqueSlug(null, request.slug());
        SlugValidator.requireValid(request.slug(), "文章 slug");

        Article article = new Article();
        applyRequest(article, request);
        return ArticleMapper.toDetailResponse(articleRepository.save(article));
    }

    @Transactional
    public ArticleDetailResponse update(Long id, ArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));
        validateUniqueSlug(id, request.slug());
        SlugValidator.requireValid(request.slug(), "文章 slug");

        applyRequest(article, request);
        return ArticleMapper.toDetailResponse(articleRepository.save(article));
    }

    @Transactional
    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("文章不存在");
        }
        articleRepository.deleteById(id);
    }

    private void applyRequest(Article article, ArticleRequest request) {
        article.setTitle(request.title().trim());
        article.setSlug(request.slug().trim());
        article.setSummary(request.summary().trim());
        article.setContent(request.content().trim());
        article.setStatus(request.status());
        article.setTags(tagService.getTagsByIds(request.tagIds()));
        article.setPublishedAt(resolvePublishedAt(request.status(), request.publishedAt()));
    }

    private Instant resolvePublishedAt(ContentStatus status, Instant publishedAt) {
        if (status == ContentStatus.PUBLISHED) {
            return publishedAt != null ? publishedAt : Instant.now();
        }
        return null;
    }

    private void validateUniqueSlug(Long currentId, String slug) {
        boolean exists = currentId == null
                ? articleRepository.existsBySlug(slug.trim())
                : articleRepository.existsBySlugAndIdNot(slug.trim(), currentId);
        if (exists) {
            throw new BadRequestException("文章 slug 已存在");
        }
    }
}

