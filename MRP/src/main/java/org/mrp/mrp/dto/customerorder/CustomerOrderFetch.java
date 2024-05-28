package org.mrp.mrp.dto.customerorder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerOrderFetch extends CustomerOrderBase {
    private Long id;
}
