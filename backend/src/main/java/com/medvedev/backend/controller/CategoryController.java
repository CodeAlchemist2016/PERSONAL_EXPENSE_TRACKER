package com.medvedev.backend.controller;

import com.medvedev.backend.dto.CategoryResponseDTO;
import com.medvedev.backend.entity.Category;
import com.medvedev.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200") // ✅ Allow frontend requests
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponseDTO> categoryDTOs = categories.stream()
                .map(category -> new CategoryResponseDTO(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.getPriorityLevel(),
                        category.getHierarchyLevel(),
                        category.getParentCategory() != null ? category.getParentCategory().getName() : null,
                        category.getHierarchyPath(),
                        category.getType().name())) // ✅ Correct closing parenthesis
                .collect(Collectors.toList()); // ✅ Properly closes stream processing

        return ResponseEntity.ok(categoryDTOs);
    }
}
