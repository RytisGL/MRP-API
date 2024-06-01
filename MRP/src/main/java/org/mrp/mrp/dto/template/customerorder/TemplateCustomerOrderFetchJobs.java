package org.mrp.mrp.dto.template.customerorder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mrp.mrp.dto.template.job.TemplateJobFetch;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateCustomerOrderFetchJobs extends TemplateCustomerOrderFetch {
    private List<TemplateJobFetch> jobs;
}
