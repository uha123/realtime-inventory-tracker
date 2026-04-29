package com.inventory.tracker.repository;

import com.inventory.tracker.model.CsvImportJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsvImportJobRepository extends JpaRepository<CsvImportJob, Long> {
}
