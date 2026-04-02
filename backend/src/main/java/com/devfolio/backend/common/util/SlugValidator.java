package com.devfolio.backend.common.util;

import com.devfolio.backend.common.exception.BadRequestException;

public final class SlugValidator {

    private SlugValidator() {
    }

    public static void requireValid(String slug, String fieldName) {
        if (slug == null || !slug.matches("^[a-z0-9]+(?:-[a-z0-9]+)*$")) {
            throw new BadRequestException(fieldName + " 格式不正确，只能包含小写字母、数字和中划线");
        }
    }
}

