package com.learncode.Utils;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;

public class StreamableToPageConverter {

    public static <T> Page<T> toPage(Streamable<T> streamable, Pageable pageable) {
        List<T> contentList = streamable.toList();
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > contentList.size() ? contentList.size() : (start + pageable.getPageSize());

        return new PageImpl<>(contentList.subList(start, end), pageable, contentList.size());
    }
}
