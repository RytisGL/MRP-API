package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.JobConverter;
import org.mrp.mrp.converters.JobStatusHistoryConverter;
import org.mrp.mrp.converters.RequisitionConverter;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.job.JobFetchBlocked;
import org.mrp.mrp.dto.jobstatushistory.JobStatusHistoryBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.entities.Job;
import org.mrp.mrp.entities.JobStatusHistory;
import org.mrp.mrp.entities.Requisition;
import org.mrp.mrp.entities.Stock;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.repositories.JobRepository;
import org.mrp.mrp.repositories.StockRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final StockRepository stockRepository;

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

        //If status changed, status change history recorded
        if (!job.getStatus().equals(jobDTO.getStatus())) {
            addJobStatusHistory(job, jobDTO.getStatus(), updateDetails);
        }
        //If status changed to complete, blocker record cleared
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

    public List<RequisitionBase> createRequisition(RequisitionBase requisitionDTO, Long jobId, Long stockId) {
        Job job = this.jobRepository.findById(jobId).orElseThrow();
        Stock stock = this.stockRepository.findById(stockId).orElseThrow();
        Requisition requisition = RequisitionConverter.requisitionDTOToRequisition(requisitionDTO);
        requisition.setStock(stock);
        requisition.setJob(job);
        List<Requisition> requisitionList = job.getRequisitions();
        requisitionList.add(requisition);
        job.setRequisitions(requisitionList);
        this.jobRepository.saveAndFlush(job);
        return RequisitionConverter.requisitionsToRequisitionDTOs(job.getRequisitions(), TypeDTO.FETCH);
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

    public List<JobBase> getAvailableJobs() {
        List<JobBase> availableJobs = new ArrayList<>();
        List<Job> jobs = this.jobRepository.findAll();
        for (Job job : jobs) {
            boolean jobAvailable;
            if (job.getStatus().equals("Complete")) {
                continue;
            }
            jobAvailable = isCompleteJob(job.getJobBlockers());
            if (jobAvailable) {
                jobAvailable = isCompleteRequisition(job.getRequisitions());
            }
            if (jobAvailable) {
                availableJobs.add(JobConverter.jobToDTO(job, TypeDTO.FETCH));
            }
        }
        return availableJobs;
    }

    public List<JobFetchBlocked> getJobsWithBlockers() {
        List<Job> jobs = this.jobRepository.findAll();
        List<JobFetchBlocked> jobFetchBlockedList = new ArrayList<>();
        for (Job job : jobs) {
            List<JobBase> jobBlockers = new ArrayList<>();
            List<RequisitionBase> requisitionList = new ArrayList<>();
            boolean jobAvailable = true;
            if (job.getStatus().equals("Complete")) {
                continue;
            }
            if (!isCompleteJob(job.getJobBlockers())) {
                jobBlockers = JobConverter.jobsToJobDTOs(job.getJobBlockers(), TypeDTO.FETCH);
                jobAvailable = false;
            }
            if (!isCompleteRequisition(job.getRequisitions())) {
                requisitionList = RequisitionConverter.requisitionsToRequisitionDTOs(job.getRequisitions(), TypeDTO.FETCH);
                jobAvailable = false;
            }
            if (!jobAvailable) {
                JobFetchBlocked jobFetchBlocked = JobConverter.jobToJobBlockedDTO(job, jobBlockers, requisitionList);
                jobFetchBlockedList.add(jobFetchBlocked);
            }
        }
        return jobFetchBlockedList;
    }

    private boolean isCompleteRequisition(List<Requisition> requisitions) {
        for (Requisition requisition : requisitions) {
            if (!requisition.getStatus().equals("Complete")) {
                return false;
            }
        }
        return true;
    }

    private boolean isCompleteJob(List<Job> jobs) {
        for (Job jobBlocker : jobs) {
            if (!jobBlocker.getStatus().equals("Complete")) {
                return false;
            }
        }
        return true;
    }
}
