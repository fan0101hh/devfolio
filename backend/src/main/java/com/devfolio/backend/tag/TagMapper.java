package com.devfolio.backend.tag;

import com.devfolio.backend.tag.dto.TagResponse;

public final class TagMapper {

    private TagMapper() {
    }

    public static TagResponse toResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName(), tag.getSlug());
    }
}

