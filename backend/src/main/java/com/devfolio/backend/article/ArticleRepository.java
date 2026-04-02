package com.devfolio.backend.article;

import com.devfolio.backend.common.model.ContentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = "tags")
    List<Article> findAllByStatusOrderByPublishedAtDesc(ContentStatus status);

    @EntityGraph(attributePaths = "tags")
    Optional<Article> findBySlugAndStatus(String slug, ContentStatus status);

    @EntityGraph(attributePaths = "tags")
    List<Article> findAllByOrderByUpdatedAtDesc();

    @EntityGraph(attributePaths = "tags")
    Optional<Article> findById(Long id);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsByTags_Id(Long tagId);
}

