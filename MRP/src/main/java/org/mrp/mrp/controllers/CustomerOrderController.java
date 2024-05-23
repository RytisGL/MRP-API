package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.CustomerOrderBase;
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
    public ResponseEntity<List<CustomerOrderBase>> getCustomerOrders() {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrders());
    }

    @GetMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> getCustomerOrderById(@PathVariable Long customerOrderId) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrderById(customerOrderId));
    }

    @GetMapping(value = "/filters")
    public ResponseEntity<List<CustomerOrderBase>> getFilteredCustomerOrders(@RequestBody CustomerOrderBase customerOrder) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrdersByCustomerFiltered(customerOrder));
    }

    @PostMapping
    public ResponseEntity<CustomerOrderBase> createCustomerOrder(@RequestBody @Valid CustomerOrderBase customerOrder) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerOrderService.createCustomerOrder(customerOrder));
    }

    @PatchMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> updateCustomerOrder(@PathVariable Long customerOrderId, @RequestBody @Valid CustomerOrderBase customerOrder) {
        return ResponseEntity.ok(this.customerOrderService.updateCustomerOrder(customerOrder, customerOrderId));
    }

    @DeleteMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> deleteCustomerOrder(@PathVariable Long customerOrderId) {
        return ResponseEntity.ok(this.customerOrderService.deleteCustomerOrderById(customerOrderId));
    }
}
