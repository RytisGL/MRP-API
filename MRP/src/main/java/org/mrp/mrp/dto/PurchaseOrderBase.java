package org.mrp.mrp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;

@Data
public class PurchaseOrderBase {
    @NotBlank(message = "{validation.constraints.quantity.message} {validation.constraints.not_blank.message}")
    @Max(value = 999999999, message = "{validation.constraints.quantity.message} {validation.constraints.max.message}")
    @Min(value = 1, message = "{validation.constraints.quantity.message} {validation.constraints.min.message}")
    private Float quantity;
    @FutureOrPresent(message = "{validation.constraints.delivery_date.message} {validation.constraints.future_or_present.message}")
    @NotBlank(message = "{validation.constraints.delivery_date.message} {validation.constraints.not_blank.message}")
    private Date deliveryDate;
    @NotBlank(message = "{validation.constraints.status.message} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.status.message} {validation.constraints.size.message}")
    private String status;
}
