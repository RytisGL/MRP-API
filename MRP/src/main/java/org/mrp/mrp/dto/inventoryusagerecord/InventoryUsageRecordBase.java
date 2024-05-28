package org.mrp.mrp.dto.inventoryusagerecord;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InventoryUsageRecordBase {
    @Max(value = 999999999, message = "{validation.constraints.quantity.name} {validation.constraints.max.message}")
    @Min(value = 1, message = "{validation.constraints.quantity.name} {validation.constraints.min.message}")
    private Float quantity;
    @Size(min = 5, max = 50, message = "{validation.constraints.status.name} {validation.constraints.size.message}")
    @NotBlank(message = "{validation.constraints.status.name} {validation.constraints.not_blank.message}")
    private String status;
}
