package org.mrp.mrp.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.job.JobFetch;
import org.mrp.mrp.dto.job.JobStatus;
import org.mrp.mrp.dto.jobrecord.JobRecordBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.entities.*;
import org.mrp.mrp.exceptions.customexceptions.ValidationConstraintException;
import org.mrp.mrp.repositories.CustomerOrderRepository;
import org.mrp.mrp.repositories.JobRepository;
import org.mrp.mrp.repositories.StockRepository;
import org.mrp.mrp.repositories.UserRepository;
import org.mrp.mrp.utils.TestUtils;
import org.springframework.data.domain.Example;
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

    private static final String STATUS_BLOCKED = "Blocked";
    private static final String STATUS_AVAILABLE = "Available";

    private Job job;
    private List<Job> jobs;
    private JobBase jobBase;
    private Requisition requisition;
    private Long id;
    private JobStatus jobStatus;
    private List<Long> blockerJobIds;
    private Stock stock;
    private CustomerOrder customerOrder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jobs = new ArrayList<>();
        jobStatus = new JobStatus();
        jobStatus.setStatus("Complete");
        id = 1L;
        requisition = TestUtils.getTestRequisition();
        jobBase = new JobBase();
        job = TestUtils.getTestJob();
        stock = TestUtils.getTestStock();
        jobs.add(job);
        customerOrder = new CustomerOrder();
    }

    @Test
    void testGetJobById() {
        job.setId(id);
        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        JobFetch result = (JobFetch) jobService.getJobById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testGetJobByIdNotFound() {
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> jobService.getJobById(id));

        assertEquals("validation.constraints.job.name", exception.getMessage());
    }

    @Test
    void testDeleteJobById() throws ValidationConstraintException {
        job.setId(id);
        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        JobFetch result = (JobFetch) jobService.deleteJobById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testDeleteJobByIdNotFound() {
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> jobService.deleteJobById(id));

        assertEquals("validation.constraints.job.name", exception.getMessage());
    }

    @Test
    void testUpdateJobStatus() throws ValidationConstraintException {
        String newStatus = "Complete";
        Authentication authentication = mock(Authentication.class);

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        JobBase result = jobService.updateJobStatus(jobStatus, id, authentication);

        assertNotNull(result);
        assertEquals(newStatus, job.getStatus());
    }

    @Test
    void testUpdateJobStatusNotFound() {
        Authentication authentication = mock(Authentication.class);

        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> jobService.updateJobStatus(jobStatus, id, authentication));

        assertEquals("validation.constraints.job.name", exception.getMessage());
    }

    @Test
    void testGetJobsNoFilters() {
        when(jobRepository.findAll()).thenReturn(jobs);

        List<JobBase> result = jobService.getJobs(null, null, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetJobsWithJobDTO() {
        when(jobRepository.findAll(ArgumentMatchers.<Example<Job>>any())).thenReturn(jobs);

        List<JobBase> result = jobService.getJobs(jobBase, null, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetJobsWithOrderId() {
        customerOrder.setId(1L);
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));
        when(jobRepository.findAll(ArgumentMatchers.<Example<Job>>any())).thenReturn(jobs);

        List<JobBase> result = jobService.getJobs(null, null, id);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetJobsWithOrderIdNotFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> jobService.getJobs(null, null, id));

        assertEquals("validation.constraints.order.name", exception.getMessage());
    }

    @Test
    void testGetJobsWithBlockedStatus() {
        jobs.clear();
        job.getJobBlockers().add(job);
        Job jobReq = TestUtils.getTestJob();
        jobReq.getRequisitions().add(requisition);
        jobs.add(jobReq);
        jobs.add(job);
        when(jobRepository.findAll(ArgumentMatchers.<Example<Job>>any())).thenReturn(jobs);

        List<JobBase> result = jobService.getJobs(null, STATUS_BLOCKED, null);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetJobsWithAvailableStatus() {
        jobs.clear();
        jobs.add(job);
        when(jobRepository.findAll(ArgumentMatchers.<Example<Job>>any())).thenReturn(jobs);

        List<JobBase> result = jobService.getJobs(null, "Available", null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetJobsWithAllFiltersStatusAvailable() {
        customerOrder.setId(id);
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));
        job.setCustomerOrder(customerOrder);
        jobs.add(job);
        when(jobRepository.findAll(ArgumentMatchers.<Example<Job>>any())).thenReturn(jobs);

        List<JobBase> result = jobService.getJobs(jobBase, STATUS_AVAILABLE, id);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetJobsWithAllFiltersStatusBlocked() {
        jobs.clear();
        customerOrder.setId(id);
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));
        job.setCustomerOrder(customerOrder);
        job.getJobBlockers().add(job);
        Job jobReq = TestUtils.getTestJob();
        jobReq.getRequisitions().add(requisition);
        jobs.add(jobReq);
        jobs.add(job);
        when(jobRepository.findAll(ArgumentMatchers.<Example<Job>>any())).thenReturn(jobs);

        List<JobBase> result = jobService.getJobs(jobBase, STATUS_BLOCKED, id);

        assertNotNull(result);
        assertEquals(2, jobs.size());
    }

    @Test
    void testAddJobBlockersThrowsValidationConstraintExceptionWhenJobIdInBlockerJobIds() {
        blockerJobIds = Arrays.asList(id, 2L, 3L);

        assertThatThrownBy(() -> jobService.addJobBlockers(id, blockerJobIds))
                .isInstanceOf(ValidationConstraintException.class)
                .hasMessage("exception.errors.blockers_contains_job.message");
    }

    @Test
    void testAddJobBlockersThrowsEntityNotFoundExceptionThenJobNotFound() {
        blockerJobIds = Arrays.asList(2L, 3L);

        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobService.addJobBlockers(id, blockerJobIds))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.job.name");
    }

    @Test
    void testAddJobBlockersSavesJobWithBlockersWhenValidInput() throws ValidationConstraintException {
        blockerJobIds = Arrays.asList(2L, 3L);
        List<Job> blockers = Arrays.asList(TestUtils.getTestJob(), TestUtils.getTestJob());

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));
        when(jobRepository.findAllById(blockerJobIds)).thenReturn(blockers);

        List<JobBase> result = jobService.addJobBlockers(id, blockerJobIds);

        verify(jobRepository).saveAndFlush(any(Job.class));
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetJobBlockersByIdReturnsJobBlockersWhenJobExists() {
        job.setJobBlockers(Arrays.asList(TestUtils.getTestJob(), TestUtils.getTestJob()));

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        List<JobBase> result = jobService.getJobBlockersById(id);

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetJobBlockersByIdThrowsEntityNotFoundJob() {
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobService.getJobBlockersById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.job.name");
    }

    @Test
    void testGetJobStatusHistoryByJobIdThrowsEntityNotFoundJob() {
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobService.getJobStatusHistoryByJobId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.job.name");
    }

    @Test
    void testGetRequisitionRecordsByJobIdThrowsEntityNotFoundJob() {
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobService.getJobRequisitionRecordsByJobId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.job.name");
    }

    @Test
    void testGCreateRequisitionThrowsEntityNotFoundJob() {
        when(jobRepository.findById(id)).thenReturn(Optional.empty());
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));

        assertThatThrownBy(() -> jobService.createRequisition(TestUtils.getTestRequisitionBase(),id,id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.job.name");
    }

    @Test
    void testGCreateRequisitionThrowsEntityNotFoundStock() {
        when(stockRepository.findById(id)).thenReturn(Optional.empty());
        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        assertThatThrownBy(() -> jobService.createRequisition(TestUtils.getTestRequisitionBase(),id,id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.stock.name");
    }

    @Test
    void testGetJobStatusHistoryByJobIdReturnsJobStatusHistoryWhenJobExists() {
        JobRecord testRecord = TestUtils.getTestJobRecord();
        testRecord.setUser(TestUtils.getTestUser());
        job.setJobRecord(Arrays.asList(testRecord, testRecord));

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        List<JobRecordBase> result = jobService.getJobStatusHistoryByJobId(id);

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetJobRequisitionsRecordsByJobIdReturnsRequisitionRecordsWhenJobExists() {
        InventoryUsageRecord testRecord = TestUtils.getTestInventoryUsageRecord();
        testRecord.setUser(TestUtils.getTestUser());
        job.setInventoryUsageRecord(Arrays.asList(testRecord, testRecord));

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        List<InventoryUsageRecordBase> result = jobService.getJobRequisitionRecordsByJobId(id);

        assertFalse(result.isEmpty());
    }

    @Test
    void testCreateRequisitionSavesRequisitionWhenValidInput() {
        when(jobRepository.findById(id)).thenReturn(Optional.of(job));
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        doAnswer(invocation -> {
            Job savedJob = invocation.getArgument(0);
            savedJob.getRequisitions().forEach(req -> req.setCreatedAt(LocalDateTime.now()));
            return savedJob;
        }).when(jobRepository).saveAndFlush(any(Job.class));

        List<RequisitionBase> result = jobService.createRequisition(TestUtils.getTestRequisitionBase(), id, id);

        verify(jobRepository).saveAndFlush(any(Job.class));
        assertFalse(result.isEmpty());
    }
}