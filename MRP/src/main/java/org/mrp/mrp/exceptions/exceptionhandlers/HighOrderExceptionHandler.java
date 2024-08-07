package org.mrp.mrp.exceptions.exceptionhandlers;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mrp.mrp.exceptions.customexceptions.ProductionConstraintException;
import org.mrp.mrp.exceptions.customexceptions.UniqueDataException;
import org.mrp.mrp.exceptions.customexceptions.ValidationConstraintException;
import org.mrp.mrp.exceptions.errorresponses.BaseErrorResponse;
import org.mrp.mrp.exceptions.errorresponses.ValidationErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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
public class HighOrderExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    private static final String ENTITY_NOT_FOUND_MSG = "exception.errors.entity_not_found.message";

    public HighOrderExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        BaseErrorResponse baseErrorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.message_not_readable.error",
                "exception.errors.message_not_readable.message",
                BAD_REQUEST,
                request);

        return new ResponseEntity<>(baseErrorResponse, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull TypeMismatchException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        BaseErrorResponse baseErrorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.type_mismatch.error",
                "exception.errors.type_mismatch.message",
                BAD_REQUEST,
                request);

        return new ResponseEntity<>(baseErrorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(WebRequest request, EntityNotFoundException ex) {
        BaseErrorResponse errorResponse;

        if (ex.getMessage() != null) {
            String entityName = messageSource.getMessage(ex.getMessage(), null, "default message",
                    request.getLocale());

            errorResponse = BaseErrorResponse.getBaseErrorResponse(
                    messageSource,
                    entityName,
                    "exception.errors.not_found.error",
                    ENTITY_NOT_FOUND_MSG,
                    NOT_FOUND,
                    request);

            return new ResponseEntity<>(errorResponse, NOT_FOUND);
        }

        errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.entity_not_found.error",
                ENTITY_NOT_FOUND_MSG,
                NOT_FOUND,
                request);

        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(UniqueDataException.class)
    protected ResponseEntity<Object> handleEmailException(WebRequest request, UniqueDataException ex) {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.unique_data.error",
                ex.getMessage(),
                CONFLICT,
                request);

        return new ResponseEntity<>(errorResponse, CONFLICT);
    }

    @ExceptionHandler(ProductionConstraintException.class)
    protected ResponseEntity<Object> handleProductionConstraintException(
            WebRequest request,
            ProductionConstraintException ex)
    {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "Production constraint exception",
                ex.getMessage(),
                I_AM_A_TEAPOT,
                request);

        return new ResponseEntity<>(errorResponse, I_AM_A_TEAPOT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException(WebRequest request) {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.entity_not_found.error",
                ENTITY_NOT_FOUND_MSG,
                CONFLICT,
                request);

        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.debug("Exception: {}, Request: {}", messageSource.getMessage(ex.getMessage(), null, ex.getMessage(),
                        request.getLocale()),
                request.getDescription(false));

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(messageSource.getMessage(
                "validation.constraints.validation.message", null, "default message", request.getLocale()),
                ex.getFieldErrors());
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleSQLException(WebRequest request) {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.data_integrity.error",
                "exception.errors.data_integrity.message",
                CONFLICT,
                request);

        return new ResponseEntity<>(errorResponse, CONFLICT);
    }

    @ExceptionHandler(ValidationConstraintException.class)
    protected ResponseEntity<Object> handleValidationConstraintException(ValidationConstraintException ex, WebRequest request) {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.validation_constraint.error",
                ex.getMessage(),
                CONFLICT,
                request);

        return new ResponseEntity<>(errorResponse, CONFLICT);
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleNumberFormatException(WebRequest request) {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.number_format_exception.error",
                "exception.errors.number_format_exception.message",
                BAD_REQUEST,
                request);

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentials(WebRequest request) {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.bad_credentials.error",
                "exception.errors.bad_credentials.error",
                UNAUTHORIZED,
                request);

        return new ResponseEntity<>(errorResponse, UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(WebRequest request) {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.access_denied.error",
                "exception.errors.access_denied.error",
                UNAUTHORIZED,
                request);

        return new ResponseEntity<>(errorResponse, UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpiredJwtException(WebRequest request) {
        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.jwt_expired.error",
                "exception.errors.jwt_expired.error",
                UNAUTHORIZED,
                request);

        return new ResponseEntity<>(errorResponse, UNAUTHORIZED);
    }
}
