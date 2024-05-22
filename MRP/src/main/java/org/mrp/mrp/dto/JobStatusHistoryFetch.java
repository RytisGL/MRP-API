package org.mrp.mrp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class JobStatusHistoryFetch extends JobStatusHistoryBase {
    private Long id;
    private LocalDateTime createdAt;
}
