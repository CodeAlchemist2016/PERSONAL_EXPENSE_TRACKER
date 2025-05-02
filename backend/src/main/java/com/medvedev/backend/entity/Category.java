package com.medvedev.backend.entity;

import com.medvedev.backend.enums.CategoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "categories", indexes = {
        @Index(name = "idx_category_type", columnList = "Category_Type"),
        @Index(name = "idx_parent_category_id", columnList = "Parent_Category_Id"),
        @Index(name = "idx_hierarchy_path", columnList = "Hierarchy_Path"),
        @Index(name = "idx_created_date", columnList = "Created_Date")
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Category_Id")
    private Integer id;

    @Column(name = "Category_Name", nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "Category_Type", nullable = false)
    private CategoryType type;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "Parent_Category_Id")
    private Category parentCategory;

    @Column(name = "Hierarchy_Level", nullable = false)
    private Integer hierarchyLevel = 1;

    @Min(1)
    @Max(5)
    @Column(name = "Priority_Level")
    private Integer priorityLevel;

    @CreatedDate
    @Column(name = "Created_Date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "Hierarchy_Path", length = 255)
    private String hierarchyPath;

    public void setPriorityLevel(Integer priorityLevel) {
        if (priorityLevel < 1 || priorityLevel > 5) {
            throw new IllegalArgumentException("Priority level must be between 1 and 5.");
        }
        this.priorityLevel = priorityLevel;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }
}
