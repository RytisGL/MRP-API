package org.mrp.mrp.exceptions.errorresponses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BaseErrorResponse {
    private static final Logger log = LoggerFactory.getLogger(BaseErrorResponse.class);
    private static final String LOG_MESSAGE = "Exception: {}, Request: {}";

    private String message;
    private List<String> errors;
    private int status;

    public BaseErrorResponse(String message, List<FieldError> errors, int status) {
        this.message = message;
        this.errors = new ArrayList<>();
        for (FieldError error : errors) {
            this.errors.add(error.getDefaultMessage());
        }
        this.status = status;
    }

    public void setError(String error) {
        this.errors = new ArrayList<>();
        this.errors.add(error);
    }

    public static BaseErrorResponse getBaseErrorResponse(
            MessageSource messageSource,
            String error,
            String message,
            HttpStatus status,
            WebRequest webRequest)
    {
        log.debug(LOG_MESSAGE, error, webRequest);
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
        baseErrorResponse.setMessage(messageSource.getMessage(message, null, message, webRequest.getLocale()));
        baseErrorResponse.setError(messageSource.getMessage(error, null, error, webRequest.getLocale()));
        baseErrorResponse.setStatus(status.value());
        return baseErrorResponse;
    }

    public static BaseErrorResponse getBaseErrorResponse(
            MessageSource messageSource
            ,String entityName,
            String error,
            String message,
            HttpStatus status,
            WebRequest webRequest)
    {
        log.debug(LOG_MESSAGE, error, webRequest);
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
        baseErrorResponse.setMessage(messageSource.getMessage(message, null, message,
                webRequest.getLocale()));
        baseErrorResponse.setError(entityName + " " + messageSource.getMessage(error, null, error, webRequest.getLocale()));
        baseErrorResponse.setStatus(status.value());
        return baseErrorResponse;
    }
}
