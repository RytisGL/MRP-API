package org.mrp.mrp.converters;

import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordFetch;
import org.mrp.mrp.entities.InventoryUsageRecord;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public class InventoryUsageRecordConverter {
    private InventoryUsageRecordConverter() {
    }

    public static InventoryUsageRecordBase inventoryUsageRecordToDTO(InventoryUsageRecord inventoryUsageRecord, TypeDTO type) {
        InventoryUsageRecordBase dto;

        if (type == TypeDTO.BASE) {
            dto = new InventoryUsageRecordBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new InventoryUsageRecordFetch();
            ((InventoryUsageRecordFetch) dto).setId(inventoryUsageRecord.getId());
            ((InventoryUsageRecordFetch) dto).setUser(UserConverter.userToUserDTO(inventoryUsageRecord.getUser()));
        } else {
            throw new IllegalArgumentException("Invalid InventoryUsageRecordType");
        }

        dto.setQuantity(inventoryUsageRecord.getQuantity());
        dto.setStatus(inventoryUsageRecord.getStatus());

        return dto;
    }

    public static List<InventoryUsageRecordBase> inventoryUsageRecordsToInventoryUsageRecordDTOs(
            List<InventoryUsageRecord> inventoryUsageRecords,
            TypeDTO type)
    {
        List<InventoryUsageRecordBase> dtos = new ArrayList<>();
        for (InventoryUsageRecord inventoryUsageRecord : inventoryUsageRecords) {
            dtos.add(inventoryUsageRecordToDTO(inventoryUsageRecord, type));
        }
        return dtos;
    }

}
