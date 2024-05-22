package org.mrp.mrp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JobStatusHistoryBase {
    @Size(min = 5, max = 50, message = "About Me must be between 5 and 50 characters")
    @NotNull(message = "Name cannot be null")
    @NotBlank
    private String status;
    @Size(min = 5, max = 50, message = "About Me must be between 5 and 50 characters")
    @NotNull(message = "Name cannot be null")
    @NotBlank
    private String details;
}
