package com.weatherchecker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ForecastExceptionHandler {

  @ExceptionHandler(PostalCodeNotFoundException.class)
  public ResponseEntity<String> handle(PostalCodeNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(PostalCodeIllegalFormatException.class)
  public ResponseEntity<String> handle(PostalCodeIllegalFormatException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
