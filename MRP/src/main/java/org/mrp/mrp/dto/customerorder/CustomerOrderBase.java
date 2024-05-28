package org.mrp.mrp.dto.customerorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerOrderBase {
    @NotBlank(message = "{validation.constraints.name.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.name.name} {validation.constraints.size.message}")
    private String name;
    @Size(min = 5, max = 50, message = "{validation.constraints.details.name} {validation.constraints.size.message}")
    @NotBlank(message = "{validation.constraints.details.name} {validation.constraints.not_blank.message}")
    private String details;
    @NotBlank(message = "{validation.constraints.status.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.status.name} {validation.constraints.size.message}")
    private String status;
}
