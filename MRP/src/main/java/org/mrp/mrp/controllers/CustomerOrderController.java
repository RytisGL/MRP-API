package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.customerorder.CustomerOrderBase;
import org.mrp.mrp.services.CustomerOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/orders")
@RestController
@RequiredArgsConstructor
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping
    public ResponseEntity<List<CustomerOrderBase>> getCustomerOrders() {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrders());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/jobs")
    public ResponseEntity<List<CustomerOrderBase>> getCustomerOrdersJobs() {
        return null;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/{orderId}/jobs")
    public ResponseEntity<List<CustomerOrderBase>> getCustomerOrderJobsByOrderId(@PathVariable Long orderId) {
        return null;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/{orderId}/jobs/blocked")
    public ResponseEntity<List<CustomerOrderBase>> getCustomerOrderBlockedJobsByOrderId(@PathVariable Long orderId) {
        return null;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/{orderId}/jobs/available")
    public ResponseEntity<List<CustomerOrderBase>> getCustomerOrderAvailableJobsByOrderId(@PathVariable Long orderId) {
        return null;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> getCustomerOrderById(@PathVariable Long customerOrderId) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrderById(customerOrderId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/filters")
    public ResponseEntity<List<CustomerOrderBase>> getFilteredCustomerOrders(@RequestBody CustomerOrderBase customerOrder) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrdersByCustomerFiltered(customerOrder));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<CustomerOrderBase> createCustomerOrder(@RequestBody @Valid CustomerOrderBase customerOrder) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerOrderService.createCustomerOrder(customerOrder));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PatchMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> updateCustomerOrder(@PathVariable Long customerOrderId, @RequestBody @Valid CustomerOrderBase customerOrder) {
        return ResponseEntity.ok(this.customerOrderService.updateCustomerOrder(customerOrder, customerOrderId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> deleteCustomerOrder(@PathVariable Long customerOrderId) {
        return ResponseEntity.ok(this.customerOrderService.deleteCustomerOrderById(customerOrderId));
    }
}
