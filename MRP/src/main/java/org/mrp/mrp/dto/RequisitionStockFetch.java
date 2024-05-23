package org.mrp.mrp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequisitionStockFetch extends RequisitionStockBase {
    private Long id;
}
