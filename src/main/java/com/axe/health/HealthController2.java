package com.axe.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController2 {
    @GetMapping("")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}