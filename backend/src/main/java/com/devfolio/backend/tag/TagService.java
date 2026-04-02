package com.devfolio.backend.tag;

import com.devfolio.backend.article.ArticleRepository;
import com.devfolio.backend.common.exception.BadRequestException;
import com.devfolio.backend.common.exception.ResourceNotFoundException;
import com.devfolio.backend.common.util.SlugValidator;
import com.devfolio.backend.project.ProjectRepository;
import com.devfolio.backend.tag.dto.TagRequest;
import com.devfolio.backend.tag.dto.TagResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final ProjectRepository projectRepository;

    public TagService(TagRepository tagRepository, ArticleRepository articleRepository, ProjectRepository projectRepository) {
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<TagResponse> listAll() {
        return tagRepository.findAllByOrderByNameAsc().stream()
                .map(TagMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Set<Tag> getTagsByIds(List<Long> tagIds) {
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new BadRequestException("部分标签不存在，请检查 tagIds");
        }
        return tags.stream().collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    @Transactional
    public TagResponse create(TagRequest request) {
        validateUnique(null, request.name(), request.slug());
        SlugValidator.requireValid(request.slug(), "标签 slug");

        Tag tag = new Tag();
        tag.setName(request.name().trim());
        tag.setSlug(request.slug().trim());
        return TagMapper.toResponse(tagRepository.save(tag));
    }

    @Transactional
    public TagResponse update(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在"));
        validateUnique(id, request.name(), request.slug());
        SlugValidator.requireValid(request.slug(), "标签 slug");

        tag.setName(request.name().trim());
        tag.setSlug(request.slug().trim());
        return TagMapper.toResponse(tagRepository.save(tag));
    }

    @Transactional
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("标签不存在");
        }
        if (articleRepository.existsByTags_Id(id) || projectRepository.existsByTags_Id(id)) {
            throw new BadRequestException("该标签仍被文章或项目引用，无法删除");
        }
        tagRepository.deleteById(id);
    }

    private void validateUnique(Long currentId, String name, String slug) {
        boolean nameExists = currentId == null
                ? tagRepository.existsByName(name.trim())
                : tagRepository.existsByNameAndIdNot(name.trim(), currentId);
        if (nameExists) {
            throw new BadRequestException("标签名称已存在");
        }

        boolean slugExists = currentId == null
                ? tagRepository.existsBySlug(slug.trim())
                : tagRepository.existsBySlugAndIdNot(slug.trim(), currentId);
        if (slugExists) {
            throw new BadRequestException("标签 slug 已存在");
        }
    }
}

