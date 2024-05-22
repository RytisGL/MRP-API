package org.mrp.mrp.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.mrp.mrp.enums.errormessages.ErrorMessageCategory;
import org.mrp.mrp.enums.errormessages.ErrorMessage;
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

import java.util.Locale;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BaseErrorResponse baseErrorResponse = getBaseErrorResponse(BAD_REQUEST, request.getLocale(), ErrorMessageCategory.HTTP_MESSAGE_NOT_READABLE);
        return new ResponseEntity<>(baseErrorResponse, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BaseErrorResponse baseErrorResponse = getBaseErrorResponse(BAD_REQUEST, request.getLocale(), ErrorMessageCategory.TYPE_MISMATCH);
        return new ResponseEntity<>(baseErrorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(WebRequest request) {
        BaseErrorResponse errorResponse = getBaseErrorResponse(NOT_FOUND, request.getLocale(), ErrorMessageCategory.ENTITY_NOT_FOUND);
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
        BaseErrorResponse errorResponse = getBaseErrorResponse(NOT_FOUND, request.getLocale(), ErrorMessageCategory.ENTITY_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex, WebRequest request) {
        log.debug("Exception: {}Request: {}Stack trace: {}", ex.getMessage(), request.getDescription(false), ex.toString());
        BaseErrorResponse errorResponse = getBaseErrorResponse(BAD_REQUEST, request.getLocale(), ErrorMessageCategory.ENTITY_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnspecifiedException(Exception ex, WebRequest request) {
        log.error("Exception: {}Request: {}Stack trace: {}", ex.getMessage(), request.getDescription(false), ex.toString());
        BaseErrorResponse errorResponse = getBaseErrorResponse(INTERNAL_SERVER_ERROR, request.getLocale(), ErrorMessageCategory.ENTITY_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    private BaseErrorResponse getBaseErrorResponse(HttpStatus status, Locale locale, ErrorMessageCategory errorMessageCategory) {
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
        ErrorMessage errorMessage = ErrorMessage.getErrorMessage(locale, errorMessageCategory);
        baseErrorResponse.setMessage(errorMessage.getMessage());
        baseErrorResponse.setError(errorMessage.getError());
        baseErrorResponse.setStatus(status.value());
        return baseErrorResponse;
    }
}
