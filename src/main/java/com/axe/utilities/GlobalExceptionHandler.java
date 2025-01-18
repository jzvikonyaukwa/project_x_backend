package com.axe.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.bugsnag.Bugsnag;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private Bugsnag bugsnag;

    @ExceptionHandler(RateNotSavedException.class)
    public ResponseEntity<ErrorResponse> handleRateNotSavedException(RateNotSavedException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "Additional details if needed");
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Exception:", ex);
        bugsnag.notify(ex);
        ApiErrorResponse response = new ApiErrorResponse(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(SteelCoilNumberExists.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<String> runtime(SteelCoilNumberExists e) {
        log.error("steel coil number exist exception:", e);
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(),  HttpStatus.valueOf(e.getCode()));
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<String> handleInvoiceNotFoundException(InvoiceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
