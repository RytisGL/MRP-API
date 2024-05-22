package org.mrp.mrp.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
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

import java.util.NoSuchElementException;

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

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
        baseErrorResponse.setMessage("Type mismatch");
        baseErrorResponse.setError("Wrong argument type");
        baseErrorResponse.setStatus(BAD_REQUEST.value());
        return new ResponseEntity<>(baseErrorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound() {
        BaseErrorResponse errorResponse = new BaseErrorResponse();
        errorResponse.setMessage("Requested data does not exist");
        errorResponse.setError("Request not found");
        errorResponse.setStatus(NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(ex.getFieldErrors());
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleSQLException(NoSuchElementException ex, WebRequest request) {
        log.debug("Exception: {}Request: {}Stack trace: {}", ex.getMessage(), request.getDescription(false), ex.toString());
        BaseErrorResponse errorResponse = new BaseErrorResponse();
        errorResponse.setMessage("Requested data does not exist");
        errorResponse.setError("Request not found");
        errorResponse.setStatus(NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex, WebRequest request) {
        log.debug("Exception: {}Request: {}Stack trace: {}", ex.getMessage(), request.getDescription(false), ex.toString());
        BaseErrorResponse errorResponse = new BaseErrorResponse();
        errorResponse.setMessage("Ill redo error messages later ");
        errorResponse.setError("For now i just need to know it catches number parse");
        errorResponse.setStatus(BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnspecifiedException(Exception ex, WebRequest request) {
        log.error("Exception: {}Request: {}Stack trace: {}", ex.getMessage(), request.getDescription(false), ex.toString());
        BaseErrorResponse errorResponse = new BaseErrorResponse();
        errorResponse.setMessage("Critical Error");
        errorResponse.setError("Internal Server Error");
        errorResponse.setStatus(INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

}
