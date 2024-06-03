package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.CustomerOrderConverter;
import org.mrp.mrp.converters.TemplateConverter;
import org.mrp.mrp.dto.customerorder.CustomerOrderBase;
import org.mrp.mrp.dto.customerorder.CustomerOrderFromTemp;
import org.mrp.mrp.dto.template.customerorder.TemplateCustomerOrderFetch;
import org.mrp.mrp.entities.*;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.repositories.CustomerOrderRepository;
import org.mrp.mrp.repositories.JobRepository;
import org.mrp.mrp.repositories.TemplateCustomerOrderRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final JobRepository jobRepository;
    private final TemplateCustomerOrderRepository templateCustomerOrderRepository;

    public CustomerOrderBase getCustomerOrderById(Long customerOrderId) {
        return CustomerOrderConverter.customerOrderToDTO(
                this.customerOrderRepository.findById(customerOrderId).orElseThrow(), TypeDTO.FETCH);
    }

    public CustomerOrderBase createCustomerOrder(CustomerOrderBase customerOrderBase) {
        return CustomerOrderConverter.customerOrderToDTO(this.customerOrderRepository.saveAndFlush(
                CustomerOrderConverter.customerOrderDTOToCustomerOrder(customerOrderBase)), TypeDTO.FETCH);
    }

    public CustomerOrderBase deleteCustomerOrderById(Long customerOrderId) {
        CustomerOrder customerOrder = this.customerOrderRepository.findById(customerOrderId).orElseThrow();
        this.customerOrderRepository.delete(customerOrder);
        return CustomerOrderConverter.customerOrderToDTO(customerOrder, TypeDTO.FETCH);
    }

    public CustomerOrderBase updateCustomerOrder(CustomerOrderBase customerOrderBase, Long customerOrderId) {
        CustomerOrder customerOrder = this.customerOrderRepository.findById(customerOrderId).orElseThrow();
        CustomerOrderConverter.updateCustomerOrderDTOToCustomerOrder(customerOrderBase, customerOrder);
        this.customerOrderRepository.saveAndFlush(customerOrder);
        return CustomerOrderConverter.customerOrderToDTO(customerOrder, TypeDTO.FETCH);
    }

    public List<CustomerOrderBase> getCustomerOrders(CustomerOrderBase filterCustomerOrderBase) {
        if (filterCustomerOrderBase == null) {
            return CustomerOrderConverter.customerOrdersToCustomerOrderDTOs(
                    this.customerOrderRepository.findAll(), TypeDTO.FETCH);
        }
        CustomerOrder customerOrder = CustomerOrderConverter.customerOrderDTOToCustomerOrder(filterCustomerOrderBase);
        return CustomerOrderConverter.customerOrdersToCustomerOrderDTOs(
                this.customerOrderRepository.findAll(Example.of(customerOrder)), TypeDTO.FETCH);
    }

    public CustomerOrderBase addJobsToCustomerOrder(List<Long> jobIds, Long customerOrderId) {
        CustomerOrder customerOrder = this.customerOrderRepository.findById(customerOrderId).orElseThrow();
        List<Job> jobs = this.jobRepository.findAllById(jobIds);
        for (Job job : jobs) {
            job.setCustomerOrder(customerOrder);
        }
        customerOrder.setJobs(jobs);
        this.jobRepository.saveAllAndFlush(jobs);
        return CustomerOrderConverter.customerOrderToDTO(customerOrder, TypeDTO.FETCH_JOBS);
    }

    public CustomerOrderBase getCustomerOrderJobsById(Long orderId) {
        return CustomerOrderConverter.customerOrderToDTO(
                this.customerOrderRepository.findById(orderId).orElseThrow(), TypeDTO.FETCH_JOBS);
    }

    public TemplateCustomerOrderFetch createTemplateFromCustomerOrder(Long orderId) {
        CustomerOrder customerOrder = this.customerOrderRepository.findById(orderId).orElseThrow();
        TemplateCustomerOrder templateCustomerOrder = TemplateConverter.customerOrderToTempOrder(customerOrder);

        //Saves which tempJob is converter to which job for setting blockers later.
        HashMap<Job, TemplateJob> templateJobRelation = new HashMap<>();

        List<TemplateJob> templateJobs = TemplateConverter.jobsToTemplateJobs(customerOrder.getJobs(),
                templateJobRelation, templateCustomerOrder);

        templateCustomerOrder.setJobs(addTemplateJobBlockers(templateJobs, templateJobRelation, customerOrder.getJobs()));
        this.templateCustomerOrderRepository.saveAndFlush(templateCustomerOrder);
        return TemplateConverter.tempCustomerOrderToDTO(templateCustomerOrder, TypeDTO.FETCH);
    }

    public List<TemplateCustomerOrderFetch> getCustomerOrderTemplates() {
        return TemplateConverter.templateCustomerOrdersToDTOs(this.templateCustomerOrderRepository.findAll(), TypeDTO.FETCH);
    }

    public TemplateCustomerOrderFetch getCustomerOrderTemplateById(Long templateId) {
        return TemplateConverter.tempCustomerOrderToDTO(this.templateCustomerOrderRepository.findById(templateId).orElseThrow(),
                TypeDTO.FETCH_JOBS);
    }

    public CustomerOrderBase createCustomerOrderFromTemplate(Long templateId, CustomerOrderFromTemp orderBase) {
        TemplateCustomerOrder templateCustomerOrder = this.templateCustomerOrderRepository.findById(templateId).orElseThrow();
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomer(orderBase.getCustomer());
        customerOrder.setStatus(orderBase.getStatus());
        customerOrder.setProduct(templateCustomerOrder.getProduct());

        //Saves which job is converter to which tempJob for setting blockers later.
        HashMap<TemplateJob, Job> jobRelation = new HashMap<>();

        List<Job> jobs = TemplateConverter.templateJobsToJobs(templateCustomerOrder.getJobs(),
                jobRelation, customerOrder);

        customerOrder.setJobs(addJobBlockers(jobs, jobRelation, templateCustomerOrder.getJobs()));
        this.customerOrderRepository.saveAndFlush(customerOrder);
        return CustomerOrderConverter.customerOrderToDTO(customerOrder, TypeDTO.FETCH);
    }

    public TemplateCustomerOrderFetch deleteTemplateCustomerOrderById(Long templateId) {
        TemplateCustomerOrder templateCustomerOrder = this.templateCustomerOrderRepository.findById(templateId).orElseThrow();
        this.templateCustomerOrderRepository.delete(templateCustomerOrder);
        return TemplateConverter.tempCustomerOrderToDTO(templateCustomerOrder, TypeDTO.FETCH);
    }

    private List<Job> addJobBlockers(List<Job> jobs, HashMap<TemplateJob, Job> jobRelation,
                                     List<TemplateJob> templateJobs) {
        for (TemplateJob templateJob : templateJobs) {
            for (TemplateJob templateJobBlockers : templateJob.getJobBlockers()) {
                Job relatedJob = new Job();
                for (Job job : jobs) {
                    if (job.equals(jobRelation.get(templateJob))) {
                        relatedJob = job;
                    }
                }
                relatedJob.getJobBlockers().add(jobRelation.get(templateJobBlockers));
            }

        }
        return jobs;
    }
    private List<TemplateJob> addTemplateJobBlockers(List<TemplateJob> templateJobs,
                                                     HashMap<Job, TemplateJob> templateJobRelation, List<Job> jobs) {
        for (Job job : jobs) {
            for (Job jobBlockers : job.getJobBlockers()) {
                TemplateJob relatedTemplateJob = new TemplateJob();
                for (TemplateJob templateJob : templateJobs) {
                    if (templateJob.equals(templateJobRelation.get(job))) {
                        relatedTemplateJob = templateJob;
                    }
                }
                relatedTemplateJob.getJobBlockers().add(templateJobRelation.get(jobBlockers));
            }

        }
        return templateJobs;
    }
}
