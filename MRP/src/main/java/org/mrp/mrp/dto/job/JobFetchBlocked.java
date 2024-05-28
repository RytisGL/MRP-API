package org.mrp.mrp.dto.job;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mrp.mrp.dto.requisition.RequisitionBase;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class JobFetchBlocked extends JobFetch {
    List<JobBase> blockedByJobs;
    List<RequisitionBase> blockedByRequisitions;
}
