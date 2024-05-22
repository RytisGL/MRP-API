package org.mrp.mrp.converters;

import org.mrp.mrp.dto.JobStatusHistoryBase;
import org.mrp.mrp.dto.JobStatusHistoryFetch;
import org.mrp.mrp.entities.JobStatusHistory;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class JobStatusHistoryConverter {
    private JobStatusHistoryConverter() {
    }

    public static JobStatusHistoryBase jobStatusHistoryToJobStatusHistoryDTO(JobStatusHistory jobHistory, TypeDTO type) {
        JobStatusHistoryBase dto;
        if (type == TypeDTO.BASE) {
            dto = new JobStatusHistoryBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new JobStatusHistoryFetch();
            ((JobStatusHistoryFetch) dto).setId(jobHistory.getId());
            ((JobStatusHistoryFetch) dto).setCreatedAt(jobHistory.getCreatedAt());
        } else {
            throw new IllegalArgumentException("Invalid JobHistoryStatus");
        }
        dto.setStatus(jobHistory.getStatus());
        dto.setDetails(jobHistory.getDetails());
        return dto;
    }

    public static List<JobStatusHistoryBase> jobStatusHistoryToJobStatusHistoryDTOs(List<JobStatusHistory> jobStatusHistoryList, TypeDTO type) {
        List<JobStatusHistoryBase> dtos = new ArrayList<>();
        for (JobStatusHistory jobStatusHistory : jobStatusHistoryList) {
            dtos.add(jobStatusHistoryToJobStatusHistoryDTO(jobStatusHistory, type));
        }
        return dtos;
    }

}
