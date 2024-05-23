package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.CustomerOrderConverter;
import org.mrp.mrp.dto.CustomerOrderBase;
import org.mrp.mrp.entities.CustomerOrder;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.repositories.CustomerOrderRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderBase getCustomerOrderById(Long customerOrderId) {
        return CustomerOrderConverter.customerOrderToDTO(this.customerOrderRepository.findById(customerOrderId).orElseThrow(), TypeDTO.FETCH);
    }

    public CustomerOrderBase createCustomerOrder(CustomerOrderBase customerOrderBase) {
        return CustomerOrderConverter.customerOrderToDTO(this.customerOrderRepository.saveAndFlush(CustomerOrderConverter.customerOrderDTOToCustomerOrder(customerOrderBase)), TypeDTO.FETCH);
    }

    public List<CustomerOrderBase> getCustomerOrders() {
        return CustomerOrderConverter.customerOrdersToCustomerOrderDTOs(this.customerOrderRepository.findAll(), TypeDTO.FETCH);
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

    public List<CustomerOrderBase> getCustomerOrdersByCustomerFiltered(CustomerOrderBase filterCustomerOrderBase) {
        CustomerOrder customerOrder = CustomerOrderConverter.customerOrderDTOToCustomerOrder(filterCustomerOrderBase);
        return CustomerOrderConverter.customerOrdersToCustomerOrderDTOs(this.customerOrderRepository.findAll(Example.of(customerOrder)), TypeDTO.FETCH);
    }
}
