package org.mrp.mrp.converters;

import org.mrp.mrp.dto.customerorder.CustomerOrderBase;
import org.mrp.mrp.dto.customerorder.CustomerOrderFetch;
import org.mrp.mrp.dto.customerorder.CustomerOrderFetchJobs;
import org.mrp.mrp.entities.CustomerOrder;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomerOrderConverter {
    private CustomerOrderConverter() {
    }

    public static CustomerOrder customerOrderDTOToCustomerOrder(CustomerOrderBase dto) {
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setName(dto.getName());
        customerOrder.setStatus(dto.getStatus());
        customerOrder.setDetails(dto.getDetails());
        return customerOrder;
    }

    public static CustomerOrderBase customerOrderToDTO(CustomerOrder customerOrder, TypeDTO type) {
        CustomerOrderBase dto;

        if (type == TypeDTO.BASE) {
            dto = new CustomerOrderBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new CustomerOrderFetch();
            ((CustomerOrderFetch) dto).setId(customerOrder.getId());
        } else if (type == TypeDTO.FETCH_JOBS) {
            dto = new CustomerOrderFetchJobs();
            ((CustomerOrderFetchJobs) dto).setId(customerOrder.getId());
            ((CustomerOrderFetchJobs) dto).setJobs(JobConverter.jobsToJobDTOs(customerOrder.getJobs(), TypeDTO.FETCH));
        } else {
            throw new IllegalArgumentException("Invalid CustomerOrderType");
        }

        dto.setName(customerOrder.getName());
        dto.setStatus(customerOrder.getStatus());
        dto.setDetails(customerOrder.getDetails());

        return dto;
    }

    public static List<CustomerOrderBase> customerOrdersToCustomerOrderDTOs(List<CustomerOrder> customerOrders, TypeDTO type) {
        List<CustomerOrderBase> dtos = new ArrayList<>();
        for (CustomerOrder customerOrder : customerOrders) {
            dtos.add(customerOrderToDTO(customerOrder, type));
        }
        return dtos;
    }

    public static void updateCustomerOrderDTOToCustomerOrder(CustomerOrderBase dto, CustomerOrder customerOrder) {
        customerOrder.setName(dto.getName());
        customerOrder.setStatus(dto.getStatus());
        customerOrder.setDetails(dto.getDetails());
    }

}
