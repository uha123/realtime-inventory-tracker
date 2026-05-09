package com.inventory.tracker.controller;

import com.inventory.tracker.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inventory.tracker.constants.AppConstants;

import java.util.List;

@RestController
@RequestMapping(AppConstants.API_V1_ALERTS)
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<Object>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Object>> getAlertsByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(alertService.getAlertsByWarehouse(warehouseId));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveAlert(@PathVariable Long id) {
        alertService.resolveAlert(id);
        return ResponseEntity.noContent().build();
    }
}
