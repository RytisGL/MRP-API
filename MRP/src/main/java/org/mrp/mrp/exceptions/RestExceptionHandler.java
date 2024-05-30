package org.mrp.mrp.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
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
    private final MessageSource messageSource;

    private static final String LOG_MESSAGE = "Exception: {} Request: {}";

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            WebRequest request) {

        BaseErrorResponse baseErrorResponse = getBaseErrorResponse(
                "exception.errors.message_not_readable.error",
                "exception.errors.message_not_readable.message",
                BAD_REQUEST,
                request.getLocale());

        return new ResponseEntity<>(baseErrorResponse, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull TypeMismatchException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request)
    {
        BaseErrorResponse baseErrorResponse = getBaseErrorResponse(
                "exception.errors.type_mismatch.error",
                "exception.errors.type_mismatch.message.errors.message_not_readable.message",
                BAD_REQUEST,
                request.getLocale());

        return new ResponseEntity<>(baseErrorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(WebRequest request) {
        BaseErrorResponse errorResponse = getBaseErrorResponse(
                "exception.errors.entity_not_found.error",
                "exception.errors.entity_not_found.message",
                NOT_FOUND,
                request.getLocale());

        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(EmailException.class)
    protected ResponseEntity<Object> handleEmailException(WebRequest request) {
        BaseErrorResponse errorResponse = getBaseErrorResponse(
                "exception.errors.email_exception.error",
                "exception.errors.email_exception.message",
                CONFLICT,
                request.getLocale());

        return new ResponseEntity<>(errorResponse, CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request)
    {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(ex.getFieldErrors());
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleSQLException(NoSuchElementException ex, WebRequest request) {
        log.debug(LOG_MESSAGE, ex.getMessage(), request.getDescription(false));

        BaseErrorResponse errorResponse = getBaseErrorResponse(
                "exception.errors.entity_not_found.error",
                "exception.errors.entity_not_found.message",
                NOT_FOUND,
                request.getLocale());

        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex, WebRequest request) {
        log.debug(LOG_MESSAGE, ex.getMessage(), request.getDescription(false));

        BaseErrorResponse errorResponse = getBaseErrorResponse(
                "exception.errors.number_format_exception.error",
                "exception.errors.number_format_exception.message",
                BAD_REQUEST,
                request.getLocale());

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleNumberFormatException(BadCredentialsException ex, WebRequest request) {
        log.debug(LOG_MESSAGE, ex.getMessage(), request.getDescription(false));

        BaseErrorResponse errorResponse = getBaseErrorResponse(
                "exception.errors.bad_credentials.error",
                "exception.errors.bad_credentials.error",
                UNAUTHORIZED,
                request.getLocale());

        return new ResponseEntity<>(errorResponse, UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAcessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.debug(LOG_MESSAGE, ex.getMessage(), request.getDescription(false));

        BaseErrorResponse errorResponse = getBaseErrorResponse(
                "exception.errors.access_denied.error",
                "exception.errors.access_denied.error",
                FORBIDDEN,
                request.getLocale());

        return new ResponseEntity<>(errorResponse, FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        log.debug(LOG_MESSAGE, ex.getMessage(), request.getDescription(false));

        BaseErrorResponse errorResponse = getBaseErrorResponse(
                "exception.errors.jwt_expired.error",
                "exception.errors.jwt_expired.error",
                UNAUTHORIZED,
                request.getLocale());

        return new ResponseEntity<>(errorResponse, UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnspecifiedException(Exception ex, WebRequest request) {
        log.error(LOG_MESSAGE, ex.getMessage(), request.getDescription(false));

        BaseErrorResponse errorResponse = getBaseErrorResponse(
                "exception.errors.exception.error",
                "exception.errors.exception.message",
                INTERNAL_SERVER_ERROR,
                request.getLocale());

        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    private BaseErrorResponse getBaseErrorResponse(String error, String message, HttpStatus status, Locale locale) {
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
        baseErrorResponse.setMessage(messageSource.getMessage(message, null, "default message", locale));
        baseErrorResponse.setError(messageSource.getMessage(error, null, "default message", locale));
        baseErrorResponse.setStatus(status.value());
        return baseErrorResponse;
    }
}
