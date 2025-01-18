package com.axe.utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
@Setter
public class ApiErrorResponse {
    private HttpStatus status;
    private String message;
}
