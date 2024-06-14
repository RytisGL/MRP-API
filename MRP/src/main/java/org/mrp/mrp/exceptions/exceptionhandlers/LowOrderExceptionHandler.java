package org.mrp.mrp.exceptions.exceptionhandlers;

import lombok.extern.slf4j.Slf4j;
import org.mrp.mrp.exceptions.errorresponses.BaseErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Order
@ControllerAdvice
public class LowOrderExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private static final String LOG_MESSAGE = "Exception: {}, Request: {}";

    public LowOrderExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
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
        baseErrorResponse.setMessage(messageSource.getMessage(message, null, message, locale));
        baseErrorResponse.setError(messageSource.getMessage(error, null, error, locale));
        baseErrorResponse.setStatus(status.value());
        return baseErrorResponse;
    }
}