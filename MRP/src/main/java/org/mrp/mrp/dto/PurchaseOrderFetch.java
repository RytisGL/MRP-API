package org.mrp.mrp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mrp.mrp.entities.PurchaseOrder;

@EqualsAndHashCode(callSuper = true)
@Data
public class PurchaseOrderFetch extends PurchaseOrderBase {
    private Long id;
}
