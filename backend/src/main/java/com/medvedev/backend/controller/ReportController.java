package com.medvedev.backend.controller;

import com.medvedev.backend.dto.ReportDTO;
import com.medvedev.backend.enums.ReportType;
import com.medvedev.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportDTO> getReport(
            @RequestParam ReportType reportType,
            @RequestParam Integer userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        ReportDTO report = reportService.generateReport(reportType, userId, startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
