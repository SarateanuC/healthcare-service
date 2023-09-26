package com.example.healthcareservice.controller.global_exception_handler;

import com.example.healthcareservice.exception.MedicationNotFoundException;
import com.example.healthcareservice.exception.PatientNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {PatientNotFoundException.class, MedicationNotFoundException.class,
            MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<APIError> validationException(
           Exception ex,
            HttpServletRequest request) {

        log.error("repository exception : " +
                ex.getMessage() +
                " for " +
                request.getRequestURI());

        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ex.getMessage())
                        .errorCode(BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage("Request is not valid")
                        .build(), BAD_REQUEST);
    }
}
