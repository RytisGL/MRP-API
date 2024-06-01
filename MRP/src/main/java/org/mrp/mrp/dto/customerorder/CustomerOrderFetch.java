package org.mrp.mrp.dto.customerorder;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerOrderFetch extends CustomerOrderBase {
    private Long id;
    private LocalDate orderDate;
}
