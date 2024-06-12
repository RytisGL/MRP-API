package org.mrp.mrp.exceptions.errorresponses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse extends BaseErrorResponse {
    public ValidationErrorResponse(String message, List<FieldError> errors) {
        super(message, errors, HttpStatus.BAD_REQUEST.value());
    }
}
