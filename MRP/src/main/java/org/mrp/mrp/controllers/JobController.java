package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.JobBase;
import org.mrp.mrp.dto.JobStatusHistoryBase;
import org.mrp.mrp.services.JobService;
import org.mrp.mrp.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/jobs")
@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobBase>> getJobs() {
        return ResponseEntity.ok(this.jobService.getJobs());
    }

    @GetMapping(value = "/{jobId}")
    public ResponseEntity<JobBase> getJobById(@PathVariable Long jobId) {
        System.out.println(jobId);
        return ResponseEntity.ok(this.jobService.getJobById(jobId));
    }

    @GetMapping(value = "/filters")
    public ResponseEntity<List<JobBase>> getJobsFiltered(@RequestBody JobBase job) {
        return ResponseEntity.ok(this.jobService.getJobFiltered(job));
    }

    @GetMapping(value = "/blockers")
    public ResponseEntity<List<JobBase>> getJobBlockers(@PathVariable Long jobId) {
        return ResponseEntity.ok(this.jobService.getJobBlockersById(jobId));
    }

    @GetMapping(value = "/{jobId}/history")
    public ResponseEntity<List<JobStatusHistoryBase>> getJobStatusHistory(@PathVariable Long jobId) {
        return ResponseEntity.ok(this.jobService.getJobStatusHistoryByJobId(jobId));
    }

    @PostMapping
    public ResponseEntity<JobBase> createJob(@RequestBody @Valid JobBase job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.createJob(job));
    }

    @PatchMapping(value = "/{jobId}")
    public ResponseEntity<JobBase> updateJob(@PathVariable Long jobId, @RequestBody @Valid JobBase job, @RequestParam(value = "details") String updateDetails) {
        return ResponseEntity.ok(this.jobService.updateJob(job, jobId, updateDetails));
    }

    @DeleteMapping(value = "/{jobId}")
    public ResponseEntity<JobBase> deleteJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(this.jobService.deleteJobById(jobId));
    }

    @PatchMapping(value = "/{jobId}/blockers")
    public ResponseEntity<List<JobBase>> updateJobBlockers(@PathVariable Long jobId, @RequestParam(value = "ids") String blockerJobIds) {
        return ResponseEntity.ok(this.jobService.addJobBlockers(jobId, Utils.parseOptionalIdStringToLongList(blockerJobIds)));
    }
}
