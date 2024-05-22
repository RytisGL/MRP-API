package org.mrp.mrp.converters;

import org.mrp.mrp.dto.JobBase;
import org.mrp.mrp.dto.JobFetch;
import org.mrp.mrp.entities.Job;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class JobConverter {
    private JobConverter() {
    }

    public static Job jobDTOToJob(JobBase dto) {
        Job job = new Job();
        job.setType(dto.getType());
        job.setStatus(dto.getStatus());
        job.setDetails(dto.getDetails());
        return job;
    }

    public static JobBase jobToDTO(Job job, TypeDTO type) {
        JobBase dto;
        if (type == TypeDTO.BASE) {
            dto = new JobBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new JobFetch();
            ((JobFetch) dto).setId(job.getId());
        } else {
            throw new IllegalArgumentException("Invalid JobType");
        }
        dto.setType(job.getType());
        dto.setStatus(job.getStatus());
        dto.setDetails(job.getDetails());
        return dto;
    }

    public static List<JobBase> jobsToJobDTOs(List<Job> jobs, TypeDTO type) {
        List<JobBase> dtos = new ArrayList<>();
        for (Job job : jobs) {
            dtos.add(jobToDTO(job, type));
        }
        return dtos;
    }

    public static void updateJobDTOToJob(JobBase dto, Job job) {
        job.setType(dto.getType());
        job.setStatus(dto.getStatus());
        job.setDetails(dto.getDetails());
    }
}
