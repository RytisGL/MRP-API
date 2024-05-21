package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.Converter;
import org.mrp.mrp.dto.CustomerOrderDTO;
import org.mrp.mrp.repositories.CustomerOrderRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderDTO getCustomerOrderById(Long customerOrderId) {
        return Converter.customerOrderToCustomerOrderDTO(this.customerOrderRepository.getReferenceById(customerOrderId));
    }
    public CustomerOrderDTO createCustomerOrder(CustomerOrderDTO customerOrderDTO) {
        return Converter.customerOrderToCustomerOrderDTO(customerOrderRepository.save(Converter.customerOrderDTOToCustomerOrder(customerOrderDTO)));
    }
}
