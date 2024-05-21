package org.mrp.mrp.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BaseErrorResponse {
    private String message;
    private List<String> errors;
    private int status;

    public BaseErrorResponse(String message, List<FieldError> errors, int status) {
        this.message = message;
        this.errors = new ArrayList<>();
        for (FieldError error : errors) {
            this.errors.add(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
        }
        this.status = status;
    }
    public void setError(String error) {
        this.errors = new ArrayList<>();
        this.errors.add(error);
    }
}
