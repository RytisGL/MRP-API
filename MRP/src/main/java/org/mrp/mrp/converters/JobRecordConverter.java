package org.mrp.mrp.converters;

import org.mrp.mrp.dto.jobrecord.JobRecordBase;
import org.mrp.mrp.dto.jobrecord.JobRecordFetch;
import org.mrp.mrp.entities.JobRecord;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class JobRecordConverter {
    private JobRecordConverter() {
    }

    public static JobRecordBase jobStatusHistoryToJobStatusHistoryDTO(JobRecord jobHistory, TypeDTO type) {
        JobRecordBase dto;
        if (type == TypeDTO.BASE) {
            dto = new JobRecordBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new JobRecordFetch();
            ((JobRecordFetch) dto).setId(jobHistory.getId());
            ((JobRecordFetch) dto).setCreatedAt(jobHistory.getCreatedAt());
        } else {
            throw new IllegalArgumentException("Invalid JobStatusHistoryType");
        }
        dto.setStatus(jobHistory.getStatus());
        dto.setDetails(jobHistory.getDetails());
        return dto;
    }

    public static List<JobRecordBase> jobStatusHistoryToJobStatusHistoryDTOs(
            List<JobRecord> jobRecordList,
            TypeDTO type)
    {
        List<JobRecordBase> dtos = new ArrayList<>();
        for (JobRecord jobRecord : jobRecordList) {
            dtos.add(jobStatusHistoryToJobStatusHistoryDTO(jobRecord, type));
        }
        return dtos;
    }

}
