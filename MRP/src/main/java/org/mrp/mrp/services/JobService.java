package org.mrp.mrp.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mrp.mrp.converters.InventoryUsageRecordConverter;
import org.mrp.mrp.converters.JobConverter;
import org.mrp.mrp.converters.JobRecordConverter;
import org.mrp.mrp.converters.RequisitionConverter;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.job.JobStatus;
import org.mrp.mrp.dto.jobrecord.JobRecordBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.entities.Job;
import org.mrp.mrp.entities.JobRecord;
import org.mrp.mrp.entities.Requisition;
import org.mrp.mrp.entities.Stock;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.exceptions.customexceptions.ProductionConstraintException;
import org.mrp.mrp.exceptions.customexceptions.UniqueDataException;
import org.mrp.mrp.exceptions.customexceptions.ValidationConstraintException;
import org.mrp.mrp.repositories.CustomerOrderRepository;
import org.mrp.mrp.repositories.JobRepository;
import org.mrp.mrp.repositories.StockRepository;
import org.mrp.mrp.repositories.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final StockRepository stockRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final UserRepository userRepository;

    private static final String COMPLETE = "Complete";
    private static final String BLOCKED = "Blocked";
    private static final String AVAILABLE = "Available";
    private static final String JOB_EXCEPTION_MESSAGE = "validation.constraints.job.name";

    public JobBase getJobById(Long jobId) {
        return JobConverter.jobToDTO(this.jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException(JOB_EXCEPTION_MESSAGE)), TypeDTO.FETCH);
    }

    public JobBase deleteJobById(Long jobId) throws ValidationConstraintException {
        Job job = this.jobRepository.findById(jobId).orElseThrow(() -> new EntityNotFoundException("validation" +
                ".constraints.job.name"));

        validateStockConstraints(job);

        this.jobRepository.delete(job);
        return JobConverter.jobToDTO(job, TypeDTO.FETCH);
    }

    public JobBase updateJobStatus(JobStatus jobStatus, Long jobId, Authentication authentication) throws ValidationConstraintException {
        Job job = this.jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException(JOB_EXCEPTION_MESSAGE));

        if (jobStatus.getStatus().equalsIgnoreCase(COMPLETE)) {
            validateBlockers(job);
            job.setJobBlockers(null);
        }

        //If status changed, status change history recorded
        if (!job.getStatus().equalsIgnoreCase(jobStatus.getStatus())) {
            addJobStatusHistory(job, jobStatus.getStatus(), authentication);
        }

        job.setStatus(jobStatus.getStatus());
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
            example.setCustomerOrder(this.customerOrderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("validation.constraints.order.name")));
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

    public List<JobBase> addJobBlockers(Long jobId, List<Long> blockerJobIds) throws ValidationConstraintException {
        if (blockerJobIds.contains(jobId)) {
            throw new ValidationConstraintException("exception.errors.blockers_contains_job.message");
        }

        Job job = this.jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException(JOB_EXCEPTION_MESSAGE));

        List<Job> newBlockers = this.jobRepository.findAllById(blockerJobIds);

        //Checks if all the ids provided were found
        if (blockerJobIds.size() != newBlockers.size()) {
            throw new EntityNotFoundException("validation.constraints.jobs.name");
        }

        job.setJobBlockers(validateBlockersUnique(job.getJobBlockers(), newBlockers));
        this.jobRepository.saveAndFlush(job);
        return JobConverter.jobsToJobDTOs(newBlockers, TypeDTO.FETCH);
    }

    public List<JobBase> getJobBlockersById(Long jobId) {
        return JobConverter.jobsToJobDTOs(
                this.jobRepository.findById(jobId)
                        .orElseThrow(() -> new EntityNotFoundException(JOB_EXCEPTION_MESSAGE))
                        .getJobBlockers(), TypeDTO.FETCH);
    }

    public List<JobRecordBase> getJobStatusHistoryByJobId(Long jobId) {
        return JobRecordConverter.jobStatusHistoryToJobStatusHistoryDTOs(
                this.jobRepository.findById(jobId)
                        .orElseThrow(() -> new EntityNotFoundException(JOB_EXCEPTION_MESSAGE))
                        .getJobRecord(), TypeDTO.FETCH);
    }


    public List<InventoryUsageRecordBase> getJobRequisitionRecordsByJobId(Long jobId) {
        return InventoryUsageRecordConverter.inventoryUsageRecordsToInventoryUsageRecordDTOs(
                this.jobRepository.findById(jobId)
                        .orElseThrow(() -> new EntityNotFoundException(JOB_EXCEPTION_MESSAGE))
                        .getInventoryUsageRecord(), TypeDTO.FETCH);
    }

    @SneakyThrows
    public List<RequisitionBase> createRequisition(RequisitionBase requisitionDTO, Long jobId, Long stockId) {
        Job job = this.jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException(JOB_EXCEPTION_MESSAGE));

        Stock stock = this.stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException("validation.constraints.stock.name"));

        ordinaryValidation(job, stock);

        Requisition requisition = RequisitionConverter.requisitionDTOToRequisition(requisitionDTO);

        requisition.setStock(stock);
        requisition.setJob(job);
        List<Requisition> requisitionList = job.getRequisitions();
        requisitionList.add(requisition);
        job.setRequisitions(requisitionList);
        this.jobRepository.saveAndFlush(job);
        return RequisitionConverter.requisitionsToRequisitionDTOs(job.getRequisitions(), TypeDTO.FETCH);
    }

    private void addJobStatusHistory(Job job, String newStatus, Authentication authentication) {
        List<JobRecord> jobRecordList = job.getJobRecord();
        JobRecord jobRecord = new JobRecord();
        jobRecord.setJob(job);
        jobRecord.setStatus(newStatus);

        jobRecord.setUser(this.userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("validation.constraints.user.name")));

        jobRecordList.add(jobRecord);
        job.setJobRecord(jobRecordList);
    }

    private List<JobBase> getAvailableJobs(List<Job> jobs) {
        List<JobBase> availableJobs = new ArrayList<>();
        for (Job job : jobs) {
            boolean jobAvailable;
            jobAvailable = isCompleteJob(job.getJobBlockers());
            if (jobAvailable) {
                //Complete requisitions deleted
                jobAvailable = job.getRequisitions().isEmpty();
            }
            if (jobAvailable) {
                availableJobs.add(JobConverter.jobToDTO(job, TypeDTO.FETCH));
            }
        }
        return availableJobs;
    }

    private List<JobBase> getJobsBlocked(List<Job> jobs) {
        List<JobBase> jobFetchBlockedList = new ArrayList<>();
        for (Job job : jobs) {
            List<JobBase> jobBlockers = new ArrayList<>();
            List<RequisitionBase> requisitionList = new ArrayList<>();
            boolean jobAvailable = true;
            if (!isCompleteJob(job.getJobBlockers())) {
                jobBlockers = JobConverter.jobsToJobDTOs(job.getJobBlockers(), TypeDTO.FETCH);
                jobAvailable = false;
            }
            if (!job.getRequisitions().isEmpty()) {
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

    private void ordinaryValidation(Job job, Stock stock) throws ProductionConstraintException {
        if (job.getDetails().toLowerCase().contains("teapot") && stock.getName().toLowerCase().contains("coffee")) {
            throw new ProductionConstraintException("Cannot brew coffee in a teapot");
        }
    }

    private boolean isCompleteJob(List<Job> jobs) {
        for (Job jobBlocker : jobs) {
            if (!jobBlocker.getStatus().equalsIgnoreCase(COMPLETE)) {
                return false;
            }
        }
        return true;
    }

    private void validateStockConstraints(Job job) throws ValidationConstraintException {
        if (!job.getInventoryUsageRecord().isEmpty()) {
            throw new ValidationConstraintException("validation.constraints.inventory_record.message");
        }
        if (!job.getRequisitions().isEmpty()) {
            throw new ValidationConstraintException("validation.constraints.requisition.message");
        }
    }


    private void validateBlockers(Job job) throws ValidationConstraintException {
        List<Job> blockers = job.getJobBlockers();
        if (!blockers.isEmpty()) {
            for (Job blocker : blockers) {
                if (!blocker.getStatus().equalsIgnoreCase(COMPLETE)) {
                    throw new ValidationConstraintException("validation.constraints.blocker.message");
                }
            }
        }
        if (!job.getRequisitions().isEmpty()) {
            throw new ValidationConstraintException("validation.constraints.requisitions.message");
        }
    }

    private List<Job> validateBlockersUnique(List<Job> blockers,
    List<Job> newBlockers) {
        for(Job blocker : newBlockers) {
            if(blockers.contains(blocker)) {
                throw new UniqueDataException("exception.errors.blocker_unique.message");
            }
            blockers.add(blocker);
        }
        return blockers;
    }
}
