package org.mrp.mrp.dto.inventoryusagerecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InventoryUsageRecordFetch extends InventoryUsageRecordBase {
    private Long id;
}
