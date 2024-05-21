package org.mrp.mrp.converters;

import org.mrp.mrp.dto.CustomerOrderDTO;
import org.mrp.mrp.entities.CustomerOrder;

import java.util.ArrayList;
import java.util.List;

public abstract class Converter {
    protected Converter() {}

    public static CustomerOrder customerOrderDTOToCustomerOrder(CustomerOrderDTO dto) {
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setName(dto.getName());
        customerOrder.setStatus(dto.getStatus());
        customerOrder.setDetails(dto.getDetails());
        return customerOrder;
    }
    public static CustomerOrderDTO customerOrderToCustomerOrderDTO(CustomerOrder customerOrder) {
        CustomerOrderDTO dto = new CustomerOrderDTO();
        dto.setName(customerOrder.getName());
        dto.setStatus(customerOrder.getStatus());
        dto.setDetails(customerOrder.getDetails());
        return dto;
    }
    public static List<CustomerOrderDTO> customerOrdersToCustomerOrderDTOs(List<CustomerOrder> customerOrders) {
        List<CustomerOrderDTO> dtos = new ArrayList<>();
        for (CustomerOrder customerOrder : customerOrders) {
            dtos.add(customerOrderToCustomerOrderDTO(customerOrder));
        }
        return dtos;
    }
    public static List<CustomerOrder> customerOrderDTOsToCustomerOrders(List<CustomerOrderDTO> dtos) {
        List<CustomerOrder> customerOrders = new ArrayList<>();
        for (CustomerOrderDTO dto : dtos) {
            customerOrders.add(customerOrderDTOToCustomerOrder(dto));
        }
        return customerOrders;
    }
}
