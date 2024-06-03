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
        customerOrder.setCustomer(dto.getCustomer());
        customerOrder.setStatus(dto.getStatus());
        customerOrder.setProduct(dto.getProduct());
        return customerOrder;
    }

    public static CustomerOrderBase customerOrderToDTO(CustomerOrder customerOrder, TypeDTO type) {
        CustomerOrderBase dto;

        if (type == TypeDTO.BASE) {
            dto = new CustomerOrderBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new CustomerOrderFetch();
            ((CustomerOrderFetch) dto).setOrderDate(customerOrder.getCreatedAt().toLocalDate());
            ((CustomerOrderFetch) dto).setId(customerOrder.getId());
        } else if (type == TypeDTO.FETCH_JOBS) {
            dto = new CustomerOrderFetchJobs();
            ((CustomerOrderFetchJobs) dto).setId(customerOrder.getId());
            ((CustomerOrderFetchJobs) dto).setOrderDate(customerOrder.getCreatedAt().toLocalDate());
            ((CustomerOrderFetchJobs) dto).setJobs(JobConverter.jobsToJobDTOs(customerOrder.getJobs(), TypeDTO.FETCH));
        } else {
            throw new IllegalArgumentException("Invalid CustomerOrderType");
        }

        dto.setCustomer(customerOrder.getCustomer());
        dto.setStatus(customerOrder.getStatus());
        dto.setProduct(customerOrder.getProduct());

        return dto;
    }


    public static List<CustomerOrderBase> customerOrdersToCustomerOrderDTOs(List<CustomerOrder> customerOrders,
                                                                            TypeDTO type) {
        List<CustomerOrderBase> dtos = new ArrayList<>();
        for (CustomerOrder customerOrder : customerOrders) {
            dtos.add(customerOrderToDTO(customerOrder, type));
        }
        return dtos;
    }

    public static void updateCustomerOrderDTOToCustomerOrder(CustomerOrderBase dto, CustomerOrder customerOrder) {
        customerOrder.setCustomer(dto.getCustomer());
        customerOrder.setStatus(dto.getStatus());
        customerOrder.setProduct(dto.getProduct());
    }

}
