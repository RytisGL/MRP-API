package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.JobConverter;
import org.mrp.mrp.converters.JobRecordConverter;
import org.mrp.mrp.converters.RequisitionConverter;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.jobrecord.JobRecordBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.entities.Job;
import org.mrp.mrp.entities.JobRecord;
import org.mrp.mrp.entities.Requisition;
import org.mrp.mrp.entities.Stock;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.repositories.CustomerOrderRepository;
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
    private final CustomerOrderRepository customerOrderRepository;

    private static final String COMPLETE = "Complete";
    private static final String BLOCKED = "Blocked";
    private static final String AVAILABLE = "Available";

    public JobBase getJobById(Long jobId) {
        return JobConverter.jobToDTO(this.jobRepository.findById(jobId).orElseThrow(), TypeDTO.FETCH);
    }

    public JobBase createJob(JobBase jobDTO) {
        return JobConverter.jobToDTO(this.jobRepository.saveAndFlush(JobConverter.jobDTOToJob(jobDTO)), TypeDTO.FETCH);
    }

    public JobBase deleteJobById(Long jobId) {
        Job job = this.jobRepository.findById(jobId).orElseThrow();
        this.jobRepository.delete(job);
        return JobConverter.jobToDTO(job, TypeDTO.FETCH);
    }

    public JobBase updateJob(JobBase jobDTO, Long jobId) {
        Job job = this.jobRepository.findById(jobId).orElseThrow();

        //If status changed, status change history recorded
        if (!job.getStatus().equals(jobDTO.getStatus())) {
            addJobStatusHistory(job, jobDTO.getStatus(), jobDTO.getDetails());
        }
        //If status changed to complete, blocker record cleared
        if (jobDTO.getStatus().equals(COMPLETE)) {
            job.setJobBlockers(null);
        }
        JobConverter.updateJobDTOToJob(jobDTO, job);
        this.jobRepository.saveAndFlush(job);
        return JobConverter.jobToDTO(job, TypeDTO.FETCH);
    }

    public List<JobBase> getJobs(JobBase jobDTO, String status, Long orderId) {
        //Return all jobs if no filters
        if (jobDTO == null && status == null && orderId == null) {
            return JobConverter.jobsToJobDTOs(this.jobRepository.findAll(), TypeDTO.FETCH);
        }
        //Create example job from dto or empty job to use as example
        Job example;
        if (jobDTO != null) {
            example = JobConverter.jobDTOToJob(jobDTO);
        } else {
            example = new Job();
        }
        //Returns based on example without customer order
        if (orderId == null && status == null) {
            return JobConverter.jobsToJobDTOs(this.jobRepository.findAll(Example.of(example)), TypeDTO.FETCH);
        }
        //Adds customer order to example if not null
        if (orderId != null) {
            example.setCustomerOrder(this.customerOrderRepository.findById(orderId).orElseThrow());
        }
        //Returns blocked based on example
        if (status != null && status.equalsIgnoreCase(BLOCKED)) {
            return this.getJobsBlocked(this.jobRepository.findAll(Example.of(example)));
        }
        //Returns available based on example
        if (status != null && status.equalsIgnoreCase(AVAILABLE)) {
            return this.getAvailableJobs(this.jobRepository.findAll(Example.of(example)));
        }
        //Returns based on example with customer order
        return JobConverter.jobsToJobDTOs(this.jobRepository.findAll(Example.of(example)), TypeDTO.FETCH);
    }

    public List<JobBase> addJobBlockers(Long jobId, List<Long> blockerJobIds) {
        Job job = this.jobRepository.findById(jobId).orElseThrow();
        List<Job> blockers = this.jobRepository.findAllById(blockerJobIds);
        job.setJobBlockers(blockers);
        this.jobRepository.saveAndFlush(job);
        return JobConverter.jobsToJobDTOs(blockers, TypeDTO.FETCH);
    }

    public List<JobBase> getJobBlockersById(Long jobId) {
        return JobConverter.jobsToJobDTOs(
                this.jobRepository.findById(jobId).orElseThrow().getJobBlockers(), TypeDTO.FETCH);
    }

    public List<JobRecordBase> getJobStatusHistoryByJobId(Long jobId) {
        return JobRecordConverter.jobStatusHistoryToJobStatusHistoryDTOs(
                this.jobRepository.findById(jobId).orElseThrow().getJobRecord(), TypeDTO.FETCH);
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
        List<JobRecord> jobRecordList = job.getJobRecord();
        JobRecord jobRecord = new JobRecord();
        jobRecord.setJob(job);
        jobRecord.setStatus(newStatus);
        jobRecord.setDetails(updateDetails);
        jobRecordList.add(jobRecord);
        job.setJobRecord(jobRecordList);
    }

    public List<JobBase> getAvailableJobs(List<Job> jobs) {
        List<JobBase> availableJobs = new ArrayList<>();
        for (Job job : jobs) {
            boolean jobAvailable;
            if (job.getStatus().equals(COMPLETE)) {
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

    public List<JobBase> getJobsBlocked(List<Job> jobs) {
        List<JobBase> jobFetchBlockedList = new ArrayList<>();
        for (Job job : jobs) {
            List<JobBase> jobBlockers = new ArrayList<>();
            List<RequisitionBase> requisitionList = new ArrayList<>();
            boolean jobAvailable = true;
            if (job.getStatus().equals(COMPLETE)) {
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
                JobBase jobFetchBlocked = JobConverter.jobToJobBlockedDTO(job, jobBlockers, requisitionList);
                jobFetchBlockedList.add(jobFetchBlocked);
            }
        }
        return jobFetchBlockedList;
    }

    private boolean isCompleteRequisition(List<Requisition> requisitions) {
        for (Requisition requisition : requisitions) {
            if (!requisition.getStatus().equals(COMPLETE)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCompleteJob(List<Job> jobs) {
        for (Job jobBlocker : jobs) {
            if (!jobBlocker.getStatus().equals(COMPLETE)) {
                return false;
            }
        }
        return true;
    }
}
