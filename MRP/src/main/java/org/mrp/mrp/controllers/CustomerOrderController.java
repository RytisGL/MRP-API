package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.CustomerOrderDTO;
import org.mrp.mrp.services.CustomerOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/orders")
@RestController
@RequiredArgsConstructor
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;
    @GetMapping
    public List<CustomerOrderDTO> getAllCustomerOrders() {
        return null;
    }
    @GetMapping (value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderDTO> getCustomerOrderById(@PathVariable Long customerOrderId) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrderById(customerOrderId));
    }
    @GetMapping(value = "/filter")
    public List<CustomerOrderDTO> filterCustomerOrders(@RequestBody CustomerOrderDTO customerOrder) {
        return null;
    }
    @PostMapping
    public ResponseEntity<CustomerOrderDTO> createCustomerOrder(@RequestBody @Valid CustomerOrderDTO customerOrder) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerOrderService.createCustomerOrder(customerOrder));
    }
    @PatchMapping (value = "/{customerOrderId}")
    public CustomerOrderDTO updateCustomerOrder(@PathVariable Long customerOrderId, @RequestBody CustomerOrderDTO customerOrder) {
        return null;
    }
    @DeleteMapping (value = "/{customerOrderId}")
    public void deleteCustomerOrder(@PathVariable Long customerOrderId) {

    }
}
