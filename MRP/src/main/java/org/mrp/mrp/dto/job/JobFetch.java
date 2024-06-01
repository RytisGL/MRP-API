package org.mrp.mrp.dto.job;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class JobFetch extends JobBase {
    private LocalDate startDate;
    private LocalDateTime updatedAt;
    private Long id;
}
