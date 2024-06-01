package org.mrp.mrp.dto.customerorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerOrderBase {
    @NotBlank(message = "{validation.constraints.customer.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.customer.name} {validation.constraints.size.message}")
    private String customer;
    @Size(min = 5, max = 50, message = "{validation.constraints.product.name} {validation.constraints.size.message}")
    @NotBlank(message = "{validation.constraints.product.name} {validation.constraints.not_blank.message}")
    private String product;
    @NotBlank(message = "{validation.constraints.status.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.status.name} {validation.constraints.size.message}")
    private String status;
}
