package org.mrp.mrp.exceptions.exceptionhandlers;

import org.mrp.mrp.exceptions.errorresponses.BaseErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order
@ControllerAdvice
public class LowOrderExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private static final Logger log = LoggerFactory.getLogger(LowOrderExceptionHandler.class);
    private static final String LOG_MESSAGE = "Exception: {}, Request: {}";

    public LowOrderExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnspecifiedException(Exception ex, WebRequest request) {
        log.error(LOG_MESSAGE, ex, request);

        BaseErrorResponse errorResponse = BaseErrorResponse.getBaseErrorResponse(
                messageSource,
                "exception.errors.exception.error",
                "exception.errors.exception.message",
                INTERNAL_SERVER_ERROR,
                request);

        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }
}