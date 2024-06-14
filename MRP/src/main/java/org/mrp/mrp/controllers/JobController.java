package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.job.JobStatus;
import org.mrp.mrp.dto.jobrecord.JobRecordBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.services.JobService;
import org.mrp.mrp.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/jobs")
@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping()
    public ResponseEntity<List<JobBase>> getJobs(
            @RequestBody(required = false) JobBase job,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "orderId", required = false) Long orderId) {
        return ResponseEntity.ok(this.jobService.getJobs(job, status, orderId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/{jobId}")
    public ResponseEntity<JobBase> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(this.jobService.getJobById(jobId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/{jobId}/blockers")
    public ResponseEntity<List<JobBase>> getJobBlockers(@PathVariable Long jobId) {
        return ResponseEntity.ok(this.jobService.getJobBlockersById(jobId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/{jobId}/records")
    public ResponseEntity<List<JobRecordBase>> getJobStatusHistory(@PathVariable Long jobId) {
        return ResponseEntity.ok(this.jobService.getJobStatusHistoryByJobId(jobId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/{jobId}/requisitions/records")
    public ResponseEntity<List<InventoryUsageRecordBase>> getJobRequisitionsRecords(@PathVariable Long jobId) {
        return ResponseEntity.ok(this.jobService.getJobRequisitionRecordsByJobId(jobId));
    }


    @SneakyThrows
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping(value = "/{jobId}/requisitions/{stockId}")
    public ResponseEntity<List<RequisitionBase>> createRequisition(
            @RequestBody @Valid RequisitionBase requisition,
            @PathVariable Long jobId,
            @PathVariable Long stockId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.createRequisition(requisition, jobId, stockId));
    }

    @SneakyThrows
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping(value = "/{jobId}/blockers")
    public ResponseEntity<List<JobBase>> createJobBlockers(
            @PathVariable Long jobId,
            @RequestParam(value = "ids") String blockerJobIds) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.jobService.addJobBlockers(jobId, Utils.parseStringIdStringToLongList(blockerJobIds)));
    }

    @SneakyThrows
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @PatchMapping(value = "/{jobId}")
    public ResponseEntity<JobBase> updateJobStatus(
            @PathVariable Long jobId,
            @RequestBody @Valid JobStatus job,
            Authentication authentication) {
        return ResponseEntity.ok(this.jobService.updateJobStatus(job, jobId, authentication));
    }

    @SneakyThrows
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{jobId}")
    public ResponseEntity<JobBase> deleteJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(this.jobService.deleteJobById(jobId));
    }
}
