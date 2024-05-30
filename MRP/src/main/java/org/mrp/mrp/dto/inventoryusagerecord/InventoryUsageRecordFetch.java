package org.mrp.mrp.dto.inventoryusagerecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mrp.mrp.dto.user.UserFetch;

@EqualsAndHashCode(callSuper = true)
@Data
public class InventoryUsageRecordFetch extends InventoryUsageRecordBase {
    private UserFetch user;
    private Long id;
}
