package org.mrp.mrp.converters;

import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.dto.requisition.RequisitionFetch;
import org.mrp.mrp.entities.Requisition;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public class RequisitionConverter {
    private RequisitionConverter() {
    }

    public static Requisition requisitionDTOToRequisition(RequisitionBase dto) {
        Requisition requisition = new Requisition();
        requisition.setQuantity(dto.getQuantity());
        requisition.setStatus(dto.getStatus());
        return requisition;
    }

    public static RequisitionBase requisitionToDTO(Requisition requisition, TypeDTO type) {
        RequisitionBase dto;

        if (type == TypeDTO.BASE) {
            dto = new RequisitionBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new RequisitionFetch();
            ((RequisitionFetch) dto).setId(requisition.getId());
        } else {
            throw new IllegalArgumentException("Invalid RequisitionType");
        }

        dto.setQuantity(requisition.getQuantity());
        dto.setStatus(requisition.getStatus());

        return dto;
    }

    public static List<RequisitionBase> requisitionsToRequisitionDTOs(List<Requisition> requisitions, TypeDTO type) {
        List<RequisitionBase> dtos = new ArrayList<>();
        for (Requisition requisition : requisitions) {
            dtos.add(requisitionToDTO(requisition, type));
        }
        return dtos;
    }

}
