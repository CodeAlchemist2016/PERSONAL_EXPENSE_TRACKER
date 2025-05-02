package com.medvedev.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer priorityLevel;
    private Integer hierarchyLevel;
    private String parentCategory; // Derived from parentCategory
    private String hierarchyPath;
    private String type;
}
