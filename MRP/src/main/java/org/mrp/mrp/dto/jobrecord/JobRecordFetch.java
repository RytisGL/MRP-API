package org.mrp.mrp.dto.jobrecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mrp.mrp.dto.user.UserFetch;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class JobRecordFetch extends JobRecordBase {
    private Long id;
    private String userEmail;
    private LocalDateTime createdAt;
}
