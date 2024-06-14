package org.mrp.mrp.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mrp.mrp.dto.customerorder.CustomerOrderBase;
import org.mrp.mrp.dto.customerorder.CustomerOrderFromTemp;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.template.customerorder.TemplateCustomerOrderFetch;
import org.mrp.mrp.entities.CustomerOrder;
import org.mrp.mrp.entities.Job;
import org.mrp.mrp.entities.TemplateCustomerOrder;
import org.mrp.mrp.exceptions.customexceptions.ValidationConstraintException;
import org.mrp.mrp.repositories.CustomerOrderRepository;
import org.mrp.mrp.repositories.JobRepository;
import org.mrp.mrp.repositories.TemplateCustomerOrderRepository;
import org.mrp.mrp.utils.TestUtils;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
class CustomerOrderServiceTest {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private TemplateCustomerOrderRepository templateCustomerOrderRepository;

    @InjectMocks
    private CustomerOrderService customerOrderService;

    private CustomerOrder customerOrder;
    private Job job;
    private TemplateCustomerOrder templateCustomerOrder;
    private final Long id = 1L;
    private CustomerOrderBase customerOrderBase;
    private JobBase jobDTO;
    private CustomerOrderFromTemp customerOrderFromTemp;

    @BeforeEach
    void setUp() {
        customerOrder = TestUtils.getTestCustomerOrder();
        job = TestUtils.getTestJob();
        templateCustomerOrder = new TemplateCustomerOrder();
        customerOrderBase = new CustomerOrderBase();
        jobDTO = new JobBase();
        customerOrderFromTemp = new CustomerOrderFromTemp();
    }

    @Test
    void testGetCustomerOrderByIdReturnsCustomerOrderWhenFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));

        CustomerOrderBase result = customerOrderService.getCustomerOrderById(id);

        assertThat(result).isNotNull();
    }

    @Test
    void testGetCustomerOrderByIdThrowsEntityNotFoundExceptionWhenNotFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.getCustomerOrderById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.order.name");
    }

    @Test
    void testCreateCustomerOrderSavesAndReturnsCustomerOrder() {
        when(customerOrderRepository.saveAndFlush(any())).thenReturn(customerOrder);

        CustomerOrderBase result = customerOrderService.createCustomerOrder(customerOrderBase);

        assertThat(result).isNotNull();
        verify(customerOrderRepository).saveAndFlush(any(CustomerOrder.class));
    }

    @Test
    void testDeleteCustomerOrderByIdDeletesAndReturnsCustomerOrderWhenFound() throws ValidationConstraintException {
        customerOrder.setJobs(new ArrayList<>());
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));

        CustomerOrderBase result = customerOrderService.deleteCustomerOrderById(id);

        assertThat(result).isNotNull();
        verify(customerOrderRepository).delete(any(CustomerOrder.class));
    }

    @Test
    void testDeleteCustomerOrderByIdThrowsEntityNotFoundExceptionWhenNotFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.deleteCustomerOrderById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.order.name");
    }

    @Test
    void testUpdateCustomerOrderUpdatesAndReturnsCustomerOrderWhenFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));

        CustomerOrderBase result = customerOrderService.updateCustomerOrder(customerOrderBase, id);

        assertThat(result).isNotNull();
        verify(customerOrderRepository).saveAndFlush(any(CustomerOrder.class));
    }

    @Test
    void testUpdateCustomerOrderThrowsEntityNotFoundExceptionWhenNotFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.updateCustomerOrder(customerOrderBase, id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.order.name");
    }

    @Test
    void testGetCustomerOrdersReturnsAllCustomerOrdersWhenFilterIsNull() {
        List<CustomerOrder> customerOrders = Arrays.asList(customerOrder, customerOrder);
        when(customerOrderRepository.findAll()).thenReturn(customerOrders);

        List<CustomerOrderBase> result = customerOrderService.getCustomerOrders(null);

        assertThat(result).isNotEmpty();
        verify(customerOrderRepository).findAll();
    }

    @Test
    void testGetCustomerOrdersReturnsFilteredCustomerOrdersWhenFilterIsNotNull() {
        List<CustomerOrder> filteredOrders = Arrays.asList(customerOrder, customerOrder);
        when(customerOrderRepository.findAll(ArgumentMatchers.<Example<CustomerOrder>>any())).thenReturn(filteredOrders);

        List<CustomerOrderBase> result = customerOrderService.getCustomerOrders(customerOrderBase);

        assertThat(result).isNotEmpty();
    }

    @Test
    void testCreateJobSavesAndReturnsJobWhenOrderExists() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));
        when(jobRepository.saveAndFlush(any(Job.class))).thenReturn(job);

        JobBase result = customerOrderService.createJob(jobDTO, id);

        assertThat(result).isNotNull();
        verify(jobRepository).saveAndFlush(any(Job.class));
    }

    @Test
    void testCreateJobThrowsEntityNotFoundExceptionWhenOrderNotFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.createJob(jobDTO, id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.order.name");
    }

    @Test
    void testGetCustomerOrderJobsByIdReturnsCustomerOrderWithJobsWhenFound() {
        customerOrder.setJobs(new ArrayList<>());
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));

        CustomerOrderBase result = customerOrderService.getCustomerOrderJobsById(id);

        assertThat(result).isNotNull();
        verify(customerOrderRepository).findById(id);
    }

    @Test
    void testGetCustomerOrderJobsByIdThrowsEntityNotFoundExceptionWhenNotFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.getCustomerOrderJobsById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.order.name");
    }

    @Test
    void testCreateTemplateFromCustomerOrderSavesAndReturnsTemplateWhenOrderExists() {
        customerOrder.setJobs(new ArrayList<>());
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(customerOrder));

        TemplateCustomerOrderFetch result = customerOrderService.createTemplateFromCustomerOrder(id);

        assertThat(result).isNotNull();
        verify(templateCustomerOrderRepository).saveAndFlush(any(TemplateCustomerOrder.class));
    }

    @Test
    void testCreateTemplateFromCustomerOrderThrowsEntityNotFoundExceptionWhenOrderNotFound() {
        when(customerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.createTemplateFromCustomerOrder(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.order.name");
    }

    @Test
    void testGetCustomerOrderTemplatesReturnsAllTemplates() {
        List<TemplateCustomerOrder> templates = Arrays.asList(new TemplateCustomerOrder(), new TemplateCustomerOrder());
        when(templateCustomerOrderRepository.findAll()).thenReturn(templates);

        List<TemplateCustomerOrderFetch> result = customerOrderService.getCustomerOrderTemplates();

        assertThat(result).isNotEmpty();
        verify(templateCustomerOrderRepository).findAll();
    }

    @Test
    void testGetCustomerOrderTemplateByIdReturnsTemplateWhenFound() {
        templateCustomerOrder.setJobs(new ArrayList<>());
        when(templateCustomerOrderRepository.findById(id)).thenReturn(Optional.of(templateCustomerOrder));

        TemplateCustomerOrderFetch result = customerOrderService.getCustomerOrderTemplateById(id);

        assertThat(result).isNotNull();
        verify(templateCustomerOrderRepository).findById(id);
    }

    @Test
    void testGetCustomerOrderTemplateByIdThrowsEntityNotFoundExceptionWhenNotFound() {
        when(templateCustomerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.getCustomerOrderTemplateById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.template.name");
    }

    @Test
    void testCreateCustomerOrderFromTemplateSavesAndReturnsCustomerOrderWhenTemplateExists() {
        templateCustomerOrder.setJobs(new ArrayList<>());
        when(templateCustomerOrderRepository.findById(id)).thenReturn(Optional.of(templateCustomerOrder));

        doAnswer(invocation -> {
            CustomerOrder savedOrder = invocation.getArgument(0);
            savedOrder.setCreatedAt(LocalDateTime.now());
            return savedOrder;
        }).when(customerOrderRepository).saveAndFlush(any(CustomerOrder.class));

        CustomerOrderBase result = customerOrderService.createCustomerOrderFromTemplate(id, customerOrderFromTemp);

        assertThat(result).isNotNull();
        verify(customerOrderRepository).saveAndFlush(any(CustomerOrder.class));
    }

    @Test
    void testCreateCustomerOrderFromTemplateThrowsEntityNotFoundExceptionWhenTemplateNotFound() {
        when(templateCustomerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.createCustomerOrderFromTemplate(id, customerOrderFromTemp))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.template.name");
    }

    @Test
    void testDeleteTemplateCustomerOrderByIdDeletesAndReturnsTemplateWhenFound() {
        when(templateCustomerOrderRepository.findById(id)).thenReturn(Optional.of(templateCustomerOrder));

        TemplateCustomerOrderFetch result = customerOrderService.deleteTemplateCustomerOrderById(id);

        assertThat(result).isNotNull();
        verify(templateCustomerOrderRepository).delete(any(TemplateCustomerOrder.class));
    }

    @Test
    void testDeleteTemplateCustomerOrderByIdThrowsEntityNotFoundExceptionWhenNotFound() {
        when(templateCustomerOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderService.deleteTemplateCustomerOrderById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("validation.constraints.template.name");
    }
}