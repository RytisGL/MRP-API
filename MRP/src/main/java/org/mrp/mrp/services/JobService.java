package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.JobConverter;
import org.mrp.mrp.converters.JobStatusHistoryConverter;
import org.mrp.mrp.dto.JobBase;
import org.mrp.mrp.dto.JobStatusHistoryBase;
import org.mrp.mrp.entities.Job;
import org.mrp.mrp.entities.JobStatusHistory;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.repositories.JobRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public JobBase getJobById(Long jobId) {
        return JobConverter.jobToDTO(this.jobRepository.findById(jobId).orElseThrow(), TypeDTO.FETCH);
    }

    public JobBase createJob(JobBase jobDTO) {
        return JobConverter.jobToDTO(this.jobRepository.saveAndFlush(JobConverter.jobDTOToJob(jobDTO)), TypeDTO.FETCH);
    }

    public List<JobBase> getJobs() {
        return JobConverter.jobsToJobDTOs(this.jobRepository.findAll(), TypeDTO.FETCH);
    }

    public JobBase deleteJobById(Long jobId) {
        Job job = this.jobRepository.findById(jobId).orElseThrow();
        this.jobRepository.delete(job);
        return JobConverter.jobToDTO(job, TypeDTO.FETCH);
    }

    public JobBase updateJob(JobBase jobDTO, Long jobId, String updateDetails) {
        Job job = this.jobRepository.findById(jobId).orElseThrow();
        if (!job.getStatus().equals(jobDTO.getStatus())) {
            addJobStatusHistory(job, jobDTO.getStatus(), updateDetails);
        }
        if (jobDTO.getStatus().equals("Complete")) {
            job.setJobBlockers(null);
        }
        JobConverter.updateJobDTOToJob(jobDTO, job);
        this.jobRepository.saveAndFlush(job);
        return JobConverter.jobToDTO(job, TypeDTO.FETCH);
    }

    public List<JobBase> getJobFiltered(JobBase jobDTO) {
        Job job = JobConverter.jobDTOToJob(jobDTO);
        return JobConverter.jobsToJobDTOs(this.jobRepository.findAll(Example.of(job)), TypeDTO.FETCH);
    }

    public List<JobBase> addJobBlockers(Long jobId, List<Long> blockerJobIds) {
        Job job = this.jobRepository.findById(jobId).orElseThrow();
        List<Job> blockers = this.jobRepository.findAllById(blockerJobIds);
        job.setJobBlockers(blockers);
        this.jobRepository.saveAndFlush(job);
        return JobConverter.jobsToJobDTOs(blockers, TypeDTO.FETCH);
    }

    public List<JobBase> getJobBlockersById(Long jobId) {
        return JobConverter.jobsToJobDTOs(this.jobRepository.findById(jobId).orElseThrow().getJobBlockers(), TypeDTO.FETCH);
    }

    public List<JobStatusHistoryBase> getJobStatusHistoryByJobId(Long jobId) {
        return JobStatusHistoryConverter.jobStatusHistoryToJobStatusHistoryDTOs(this.jobRepository.findById(jobId).orElseThrow().getJobStatusHistory(), TypeDTO.FETCH);
    }

    private void addJobStatusHistory(Job job, String newStatus, String updateDetails) {
        List<JobStatusHistory> jobStatusHistoryList = job.getJobStatusHistory();
        JobStatusHistory jobStatusHistory = new JobStatusHistory();
        jobStatusHistory.setJob(job);
        jobStatusHistory.setStatus(newStatus);
        jobStatusHistory.setDetails(updateDetails);
        jobStatusHistoryList.add(jobStatusHistory);
        job.setJobStatusHistory(jobStatusHistoryList);
    }
}
