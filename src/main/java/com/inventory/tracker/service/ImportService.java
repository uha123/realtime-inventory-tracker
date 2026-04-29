package com.inventory.tracker.service;

import com.inventory.tracker.dto.ImportJobResponseDto;
import com.inventory.tracker.model.CsvImportJob;
import com.inventory.tracker.repository.CsvImportJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {

    private final CsvImportJobRepository importJobRepository;

    public CsvImportJob processImport(MultipartFile file) {
        log.info("Starting CSV import for file: {}", file.getOriginalFilename());
        
        CsvImportJob job = CsvImportJob.builder()
                .fileName(file.getOriginalFilename())
                .status(CsvImportJob.Status.PENDING)
                .build();
                
        job = importJobRepository.save(job);
        
        // The actual processing would typically be done asynchronously
        log.info("Import job created with ID: {}", job.getId());
        return job;
    }

    public ImportJobResponseDto processImportJob(MultipartFile file) {
        CsvImportJob job = processImport(file);
        return mapToDto(job);
    }

    public List<ImportJobResponseDto> getAllImportJobs() {
        log.info("Fetching all import jobs");
        return importJobRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ImportJobResponseDto getImportJobStatus(Long id) {
        log.info("Fetching import job status for id: {}", id);
        CsvImportJob job = importJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Import job not found"));
        return mapToDto(job);
    }

    public void rollbackImportJob(Long id) {
        log.info("Rolling back import job id: {}", id);
        CsvImportJob job = importJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Import job not found"));
                
        job.setStatus(CsvImportJob.Status.FAILED); // Simulated rollback status change
        importJobRepository.save(job);
        log.info("Successfully rolled back import job id: {}", id);
    }

    private ImportJobResponseDto mapToDto(CsvImportJob job) {
        return ImportJobResponseDto.builder()
                .id(job.getId())
                .fileName(job.getFileName())
                .status(job.getStatus().name())
                .totalRecords(job.getTotalRecords())
                .processed(job.getProcessed())
                .failed(job.getFailed())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
