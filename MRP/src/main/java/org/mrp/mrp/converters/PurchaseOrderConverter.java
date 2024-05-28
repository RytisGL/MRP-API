package org.mrp.mrp.converters;

import org.mrp.mrp.dto.purchaseorder.PurchaseOrderBase;
import org.mrp.mrp.dto.purchaseorder.PurchaseOrderFetch;
import org.mrp.mrp.entities.PurchaseOrder;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderConverter {
    private PurchaseOrderConverter() {
    }

    public static PurchaseOrder purchaseOrderDTOToPurchaseOrder(PurchaseOrderBase dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setQuantity(dto.getQuantity());
        purchaseOrder.setStatus(dto.getStatus());
        purchaseOrder.setDeliveryDate(dto.getDeliveryDate());
        return purchaseOrder;
    }

    public static PurchaseOrderBase purchaseOrderToDTO(PurchaseOrder purchaseOrder, TypeDTO type) {
        PurchaseOrderBase dto;

        if (type == TypeDTO.BASE) {
            dto = new PurchaseOrderBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new PurchaseOrderFetch();
            ((PurchaseOrderFetch) dto).setId(purchaseOrder.getId());
        } else {
            throw new IllegalArgumentException("Invalid PurchaseOrderType");
        }

        dto.setQuantity(purchaseOrder.getQuantity());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setDeliveryDate(purchaseOrder.getDeliveryDate());

        return dto;
    }

    public static List<PurchaseOrderBase> purchaseOrdersToPurchaseOrderDTOs(List<PurchaseOrder> purchaseOrders, TypeDTO type) {
        List<PurchaseOrderBase> dtos = new ArrayList<>();
        for (PurchaseOrder purchaseOrder : purchaseOrders) {
            dtos.add(purchaseOrderToDTO(purchaseOrder, type));
        }
        return dtos;
    }

    public static void updateCustomerOrderDTOToCustomerOrder(PurchaseOrderBase dto, PurchaseOrder purchaseOrder) {
        purchaseOrder.setQuantity(dto.getQuantity());
        purchaseOrder.setStatus(dto.getStatus());
        purchaseOrder.setDeliveryDate(dto.getDeliveryDate());
    }
}
