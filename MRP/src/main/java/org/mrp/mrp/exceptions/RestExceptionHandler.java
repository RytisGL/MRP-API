package org.mrp.mrp.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
        baseErrorResponse.setMessage("Message not readable");
        baseErrorResponse.setError("Wrong request format");
        baseErrorResponse.setStatus(BAD_REQUEST.value());
        return new ResponseEntity<>(baseErrorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound() {
        BaseErrorResponse errorResponse = new BaseErrorResponse();
        errorResponse.setMessage("Request not found");
        errorResponse.setStatus(NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(ex.getFieldErrors());
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> logError(Exception ex, WebRequest request) {
        log.error("Exception: {}Request: {}Stack trace: {}", ex.getMessage(), request.getDescription(false), ex.toString());
        BaseErrorResponse errorResponse = new BaseErrorResponse();
        errorResponse.setMessage("Critical Error");
        errorResponse.setError("Internal Server Error");
        errorResponse.setStatus(INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

}
