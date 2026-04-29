package com.inventory.tracker.controller;

import com.inventory.tracker.dto.ImportJobResponseDto;
import com.inventory.tracker.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping("/csv")
    public ResponseEntity<ImportJobResponseDto> uploadCsv(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(importService.processImportJob(file));
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<ImportJobResponseDto>> getAllImportJobs() {
        return ResponseEntity.ok(importService.getAllImportJobs());
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ImportJobResponseDto> getImportJobStatus(@PathVariable Long id) {
        return ResponseEntity.ok(importService.getImportJobStatus(id));
    }

    @PostMapping("/jobs/{id}/rollback")
    public ResponseEntity<Void> rollbackImportJob(@PathVariable Long id) {
        importService.rollbackImportJob(id);
        return ResponseEntity.noContent().build();
    }
}
