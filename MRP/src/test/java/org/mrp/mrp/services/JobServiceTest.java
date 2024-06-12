package org.mrp.mrp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.job.JobFetch;
import org.mrp.mrp.dto.job.JobStatus;
import org.mrp.mrp.entities.Job;
import org.mrp.mrp.entities.User;
import org.mrp.mrp.exceptions.customexceptions.ValidationConstraintException;
import org.mrp.mrp.repositories.CustomerOrderRepository;
import org.mrp.mrp.repositories.JobRepository;
import org.mrp.mrp.repositories.StockRepository;
import org.mrp.mrp.repositories.UserRepository;
import org.springframework.security.core.Authentication;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobService jobService;

    private Job job = new Job();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        job.setJobBlockers(new ArrayList<>());
        job.setRequisitions(new ArrayList<>());
        job.setJobRecord(new ArrayList<>());
        job.setJobBlockers(new ArrayList<>());
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        job.setInventoryUsageRecord(new ArrayList<>());
        job.setId(1L);
        job.setStatus("In Progress");
    }

    @Test
    void testGetJobById() {
        Long jobId = 1L;
        job.setId(jobId);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        JobFetch result = (JobFetch) jobService.getJobById(jobId);
        assertNotNull(result);
        assertEquals(jobId, result.getId());
    }

    @Test
    void testGetJobById_NotFound() {
        Long jobId = 1L;
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> jobService.getJobById(jobId));

        assertEquals("validation.constraints.job.name", exception.getMessage());
    }

    @Test
    void testDeleteJobById() throws ValidationConstraintException {
        Long jobId = 1L;
        job.setId(jobId);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        JobFetch result = (JobFetch) jobService.deleteJobById(jobId);

        assertNotNull(result);
        assertEquals(jobId, result.getId());
    }

    @Test
    void testDeleteJobById_NotFound() {
        Long jobId = 1L;
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> jobService.deleteJobById(jobId));

        assertEquals("validation.constraints.job.name", exception.getMessage());
    }

    @Test
    void testUpdateJobStatus() throws ValidationConstraintException {
        Long jobId = 1L;
        String newStatus = "Complete";
        JobStatus jobStatus = new JobStatus();
        jobStatus.setStatus(newStatus);
        Authentication authentication = mock(Authentication.class);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        JobBase result = jobService.updateJobStatus(jobStatus, jobId, authentication);

        assertNotNull(result);
        assertEquals(newStatus, job.getStatus());
    }

    @Test
    void testUpdateJobStatus_NotFound() {
        Long jobId = 1L;
        JobStatus jobStatus = new JobStatus();
        jobStatus.setStatus("Complete");
        Authentication authentication = mock(Authentication.class);

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> jobService.updateJobStatus(jobStatus, jobId, authentication));

        assertEquals("validation.constraints.job.name", exception.getMessage());
    }
}