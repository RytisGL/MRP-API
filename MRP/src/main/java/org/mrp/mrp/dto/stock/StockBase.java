package org.mrp.mrp.dto.stock;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StockBase {
    @NotBlank(message = "{validation.constraints.name.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.name.name} {validation.constraints.size.message}")
    private String name;
    @Max(value = 999999999, message = "{validation.constraints.quantity.name} {validation.constraints.max.message}")
    @Min(value = 1, message = "{validation.constraints.quantity.name} {validation.constraints.min.message}")
    private Float quantity;
    @NotBlank(message = "{validation.constraints.unit_of_measurement.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.unit_of_measurement.name} {validation.constraints.size.message}")
    private String unitOfMeasurement;
}
