package org.mrp.mrp.dto.stock;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockFetch extends StockBase {
    private Long id;
}
