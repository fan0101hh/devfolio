package com.devfolio.backend.project;

import com.devfolio.backend.common.model.ContentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = "tags")
    List<Project> findAllByStatusOrderByFeaturedDescPublishedAtDesc(ContentStatus status);

    @EntityGraph(attributePaths = "tags")
    Optional<Project> findBySlugAndStatus(String slug, ContentStatus status);

    @EntityGraph(attributePaths = "tags")
    List<Project> findAllByOrderByUpdatedAtDesc();

    @EntityGraph(attributePaths = "tags")
    Optional<Project> findById(Long id);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsByTags_Id(Long tagId);
}

