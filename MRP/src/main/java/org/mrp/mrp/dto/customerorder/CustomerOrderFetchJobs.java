package org.mrp.mrp.dto.customerorder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mrp.mrp.dto.job.JobBase;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerOrderFetchJobs extends CustomerOrderFetch {
    private List<JobBase> jobs;
}
