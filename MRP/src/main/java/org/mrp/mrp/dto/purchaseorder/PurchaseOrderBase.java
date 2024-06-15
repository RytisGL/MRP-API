package org.mrp.mrp.dto.purchaseorder;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;

@Data
public class PurchaseOrderBase {
    @Max(value = 999999999, message = "{validation.constraints.quantity.name} {validation.constraints.max.message}")
    @Min(value = 1, message = "{validation.constraints.quantity.name} {validation.constraints.min.message}")
    private Float quantity;
    @Future(message = "{validation.constraints.delivery_date.name} {validation.constraints" +
            ".future_or_present.message}")
    @NotNull (message = "{validation.constraints.delivery_date.name} {validation.constraints.not_null.message}")
    private Date deliveryDate;
    @NotBlank(message = "{validation.constraints.status.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.status.name} {validation.constraints.size.message}")
    private String status;
}
