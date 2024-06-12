package org.mrp.mrp.dto.requisition;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RequisitionBase {
    @NotNull(message = "{validation.constraints.quantity.name} {validation.constraints.not_blank.message}")
    @Max(value = 999999999, message = "{validation.constraints.quantity.name} {validation.constraints.max.message}")
    @Min(value = 1, message = "{validation.constraints.quantity.name} {validation.constraints.min.message}")
    private Float quantity;
    @NotBlank(message = "{validation.constraints.status.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.status.name} {validation.constraints.size.message}")
    private String status;
}
