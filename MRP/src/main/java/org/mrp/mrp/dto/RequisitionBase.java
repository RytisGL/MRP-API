package org.mrp.mrp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequisitionBase {
    @NotBlank(message = "{validation.constraints.quantity.message} {validation.constraints.not_blank.message}")
    @Max(value = 999999999, message = "{validation.constraints.quantity.message} {validation.constraints.max.message}")
    @Min(value = 1, message = "{validation.constraints.quantity.message} {validation.constraints.min.message}")
    private Float quantity;
    @NotBlank(message = "{validation.constraints.status.message} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.status.message} {validation.constraints.size.message}")
    private String status;
}
