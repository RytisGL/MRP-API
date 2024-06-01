package org.mrp.mrp.dto.requisition;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequisitionFetch extends RequisitionBase {
    private Long id;
    private LocalDateTime createdAt;
}
