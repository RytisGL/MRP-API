package org.mrp.mrp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequisitionFetch extends RequisitionBase {
    private Long id;
}
