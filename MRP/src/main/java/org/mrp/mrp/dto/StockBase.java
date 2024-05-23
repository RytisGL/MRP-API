package org.mrp.mrp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StockBase {
    @NotBlank(message = "{validation.constraints.name.message} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.name.message} {validation.constraints.size.message}")
    private String name;
    @NotBlank(message = "{validation.constraints.quantity.message} {validation.constraints.not_blank.message}")
    @Max(value = 999999999, message = "{validation.constraints.quantity.message} {validation.constraints.max.message}")
    private Float quantity;
    @NotBlank(message = "{validation.constraints.unit_of_measurement.message} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.unit_of_measurement.message} {validation.constraints.size.message}")
    private String unitOfMeasurement;
}
