package com.inventory.tracker.controller;

import com.inventory.tracker.dto.AuthRequestDto;
import com.inventory.tracker.dto.AuthResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        // Mock token logic since Security is a placeholder
        return ResponseEntity.ok(new AuthResponseDto("mock-jwt-token-12345"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh() {
        return ResponseEntity.ok(new AuthResponseDto("new-mock-jwt-token-67890"));
    }
}
