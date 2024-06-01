package org.mrp.mrp.dto.template.job;

import lombok.Data;
import org.mrp.mrp.dto.template.requisition.TemplateRequisitionFetch;

import java.util.List;

@Data
public class TemplateJobFetch {
    private String type;
    private String details;
    private String status;
    private List<TemplateRequisitionFetch> requisitions;
}
