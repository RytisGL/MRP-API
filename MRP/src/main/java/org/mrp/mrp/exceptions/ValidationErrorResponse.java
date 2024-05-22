package org.mrp.mrp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse extends BaseErrorResponse {
    public ValidationErrorResponse(List<FieldError> errors) {
        super("Validation error", errors, HttpStatus.BAD_REQUEST.value());
    }
}
