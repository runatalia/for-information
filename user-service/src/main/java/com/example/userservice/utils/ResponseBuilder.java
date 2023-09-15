package com.example.userservice.utils;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseBuilder {

    private static final String TOTAL_COUNT_HEADER = "X-Total-Count";

    private ResponseBuilder() {
    }

    public static <T> ResponseEntity<List<T>> build(Page<T> pages) {
        if (pages.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok()
                    .header(TOTAL_COUNT_HEADER, String.valueOf(pages.getTotalElements()))
                    .body(pages.getContent());
        }
    }

    public static <T> ResponseEntity<List<T>> build(List<T> collection) {
        if (collection.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok()
                    .body(collection);
        }
    }
}
