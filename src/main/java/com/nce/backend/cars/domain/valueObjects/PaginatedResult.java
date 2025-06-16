package com.nce.backend.cars.domain.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PaginatedResult <T> {
    private List<T> items;
    private final int totalPages;
    private final long totalElements;
    private final int currentPage;
}

