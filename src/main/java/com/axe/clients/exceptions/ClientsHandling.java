package com.axe.clients.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ClientsHandling {

    @ResponseBody
    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<String> runtime(ClientNotFoundException e) {
        log.error("Client not found exception:", e);
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(),  HttpStatus.valueOf(e.getCode()));
    }

}