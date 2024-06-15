package org.mrp.mrp.converters;

import org.mrp.mrp.dto.template.customerorder.TemplateCustomerOrderFetch;
import org.mrp.mrp.dto.template.customerorder.TemplateCustomerOrderFetchJobs;
import org.mrp.mrp.dto.template.job.TemplateJobFetch;
import org.mrp.mrp.dto.template.requisition.TemplateRequisitionFetch;
import org.mrp.mrp.entities.*;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class TemplateConverter {
    private TemplateConverter() {
    }

    private static final String IN_PROGRESS = "In progress";

    public static List<TemplateJob> jobsToTemplateJobs(List<Job> jobs, Map<Job, TemplateJob> templateJobRelation,
                                                       TemplateCustomerOrder templateCustomerOrder) {
        List<TemplateJob> templateJobs = new ArrayList<>();
        for (Job job : jobs) {
            templateJobs.add(jobToTemplateJob(job, templateJobRelation, templateCustomerOrder));
        }
        return templateJobs;
    }

    public static TemplateJob jobToTemplateJob(
            Job job,
            Map<Job, TemplateJob> templateJobRelation,
            TemplateCustomerOrder templateCustomerOrder)
    {
        TemplateJob templateJob = new TemplateJob();
        templateJobRelation.put(job, templateJob);
        templateJob.setCustomerOrder(templateCustomerOrder);
        templateJob.setDetails(job.getDetails());
        templateJob.setStatus(IN_PROGRESS);
        templateJob.setType(job.getType());
        templateJob.setJobBlockers(new ArrayList<>());
        List<TemplateRequisition> templateRequisitions = requisitionsToTempRequisitions(job.getRequisitions(),
                templateJob);
        templateJob.setRequisitions(templateRequisitions);
        return templateJob;
    }

    public static TemplateCustomerOrder customerOrderToTempOrder(CustomerOrder customerOrder) {
        TemplateCustomerOrder templateCustomerOrder = new TemplateCustomerOrder();
        templateCustomerOrder.setProduct(customerOrder.getProduct());
        return templateCustomerOrder;
    }

    public static TemplateRequisition requisitionToTempRequisition(Requisition requisition, TemplateJob templateJob) {
        TemplateRequisition templateRequisition = new TemplateRequisition();
        templateRequisition.setJob(templateJob);
        templateRequisition.setQuantity(requisition.getQuantity());
        templateRequisition.setStock(requisition.getStock());
        templateRequisition.setStatus(IN_PROGRESS);
        return templateRequisition;
    }

    public static List<TemplateRequisition> requisitionsToTempRequisitions(List<Requisition> requisitions,
                                                                           TemplateJob templateJob) {
        List<TemplateRequisition> templateRequisitions = new ArrayList<>();
        for (Requisition requisition : requisitions) {
            templateRequisitions.add(requisitionToTempRequisition(requisition, templateJob));
        }
        return templateRequisitions;
    }

    public static List<TemplateCustomerOrderFetch> templateCustomerOrdersToDTOs(List<TemplateCustomerOrder> templateCustomerOrders, TypeDTO type) {
        List<TemplateCustomerOrderFetch> templateCustomerOrderFetches = new ArrayList<>();
        for (TemplateCustomerOrder templateCustomerOrder : templateCustomerOrders) {
            templateCustomerOrderFetches.add(tempCustomerOrderToDTO(templateCustomerOrder, type));
        }
        return templateCustomerOrderFetches;
    }

    public static TemplateCustomerOrderFetch tempCustomerOrderToDTO(TemplateCustomerOrder template, TypeDTO type) {
        TemplateCustomerOrderFetch templateCustomerOrderFetch;

        if (type == TypeDTO.FETCH) {
            templateCustomerOrderFetch = new TemplateCustomerOrderFetch();
        } else if (type == TypeDTO.FETCH_JOBS) {
            templateCustomerOrderFetch = new TemplateCustomerOrderFetchJobs();
            ((TemplateCustomerOrderFetchJobs) templateCustomerOrderFetch).setJobs(templateJobsToDTOs(template.getJobs()));
        } else {
            throw new IllegalArgumentException("Invalid TemplateCustomerOrder Type");
        }
        templateCustomerOrderFetch.setId(template.getId());
        templateCustomerOrderFetch.setProduct(template.getProduct());

        return templateCustomerOrderFetch;
    }

    public static List<TemplateJobFetch> templateJobsToDTOs(List<TemplateJob> jobs) {
        List<TemplateJobFetch> templateJobFetches = new ArrayList<>();
        for (TemplateJob job : jobs) {
            templateJobFetches.add(templateJobToDTO(job));
        }
        return templateJobFetches;
    }

    public static List<TemplateRequisitionFetch> templateRequisitionsToDTOs(List<TemplateRequisition> requisitions) {
        List<TemplateRequisitionFetch> templateRequisitionFetches = new ArrayList<>();
        for (TemplateRequisition templateRequisition : requisitions) {
            templateRequisitionFetches.add(templateRequisitionToDTO(templateRequisition));
        }
        return templateRequisitionFetches;
    }

    public static TemplateJobFetch templateJobToDTO(TemplateJob job) {
        TemplateJobFetch templateJobFetch = new TemplateJobFetch();
        templateJobFetch.setStatus(job.getStatus());
        templateJobFetch.setDetails(job.getDetails());
        templateJobFetch.setType(job.getType());
        templateJobFetch.setRequisitions(templateRequisitionsToDTOs(job.getRequisitions()));
        return templateJobFetch;
    }
    public static TemplateRequisitionFetch templateRequisitionToDTO(TemplateRequisition requisition) {
        TemplateRequisitionFetch templateRequisitionFetch = new TemplateRequisitionFetch();
        templateRequisitionFetch.setQuantity(requisition.getQuantity());
        templateRequisitionFetch.setStatus(requisition.getStatus());
        return templateRequisitionFetch;
    }

    public static List<Job> templateJobsToJobs(List<TemplateJob> templateJobs, Map<TemplateJob, Job> jobRelation,
                                               CustomerOrder customerOrder) {
        List<Job> jobs = new ArrayList<>();
        for (TemplateJob templateJob : templateJobs) {
            jobs.add(templateJobToJob(templateJob, jobRelation, customerOrder));
        }
        return jobs;
    }

    private static Job templateJobToJob(TemplateJob templateJob, Map<TemplateJob, Job> jobRelation,
                                      CustomerOrder customerOrder) {
        Job job = new Job();
        jobRelation.put(templateJob, job);
        job.setCustomerOrder(customerOrder);
        job.setDetails(templateJob.getDetails());
        job.setStatus(templateJob.getStatus());
        job.setType(templateJob.getType());
        job.setJobBlockers(new ArrayList<>());
        List<Requisition> requisitions = templateRequisitionsToRequisitions(templateJob.getRequisitions(),
                job);
        job.setRequisitions(requisitions);
        return job;
    }

    private static List<Requisition> templateRequisitionsToRequisitions(List<TemplateRequisition> templateRequisitions,
                                                                        Job job) {
        List<Requisition> requisitions = new ArrayList<>();
        for (TemplateRequisition templateRequisition : templateRequisitions) {
            requisitions.add(templateRequisitionToRequisition(templateRequisition, job));
        }
        return requisitions;
    }

    private static Requisition templateRequisitionToRequisition(TemplateRequisition templateRequisition, Job job) {
        Requisition requisition = new Requisition();
        requisition.setJob(job);
        requisition.setQuantity(templateRequisition.getQuantity());
        requisition.setStock(templateRequisition.getStock());
        requisition.setStatus(templateRequisition.getStatus());
        return requisition;
    }
}
