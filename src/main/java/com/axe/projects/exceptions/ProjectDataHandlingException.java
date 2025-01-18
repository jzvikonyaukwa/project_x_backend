package com.axe.projects.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ProjectDataHandlingException {

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        Map<String, String> errorResponse = new HashMap<>();

        Throwable rootCause = e.getRootCause();
        if (rootCause != null && rootCause.getMessage() != null && rootCause.getMessage().contains("projects.unique_name")) {
            errorResponse.put("error", "A project with this name already exists. Please choose a different name.");
            return ResponseEntity.badRequest().body(errorResponse);
        } else {
            errorResponse.put("error", "Data integrity violation occurred.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
}