package com.medvedev.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports", indexes = {
        @Index(name = "idx_user_id", columnList = "User_Id"),
        @Index(name = "idx_report_date", columnList = "Report_Date"),
        @Index(name = "idx_report_type", columnList = "Report_Type")
})
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Report_Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @Column(name = "Report_Date", updatable = false)
    private LocalDateTime reportDate;

    @Column(name = "Report_Type", nullable = false)
    private String reportType;

    @Column(name = "Generated_File_Path")
    private String generatedFilePath;

    @PrePersist
    protected void onCreate() {
        if (this.reportDate == null) {
            this.reportDate = LocalDateTime.now();
        }
    }
}
