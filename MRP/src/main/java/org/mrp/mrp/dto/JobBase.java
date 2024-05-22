package org.mrp.mrp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JobBase {
    @NotBlank(message = "{validation.constraints.type.message} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.type.message} {validation.constraints.size.message}")
    private String type;
    @Size(min = 5, max = 50, message = "{validation.constraints.details.message} {validation.constraints.size.message}")
    @NotBlank(message = "{validation.constraints.details.message} {validation.constraints.not_blank.message}")
    private String details;
    @NotBlank(message = "{validation.constraints.status.message} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.status.message} {validation.constraints.size.message}")
    private String status;
}
