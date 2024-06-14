package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mrp.mrp.dto.customerorder.CustomerOrderBase;
import org.mrp.mrp.dto.customerorder.CustomerOrderFromTemp;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.template.customerorder.TemplateCustomerOrderFetch;
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
    @GetMapping()
    public ResponseEntity<List<CustomerOrderBase>> getCustomerOrders(@RequestBody (required = false) CustomerOrderBase customerOrder) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrders(customerOrder));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> getCustomerOrderById(@PathVariable Long customerOrderId) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrderById(customerOrderId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/{orderId}/jobs")
    public ResponseEntity<CustomerOrderBase> getCustomerOrderJobsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrderJobsById(orderId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/templates")
    public ResponseEntity<List<TemplateCustomerOrderFetch>> getCustomerOrderTemplates() {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrderTemplates());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping(value = "/templates/{templateId}")
    public ResponseEntity<TemplateCustomerOrderFetch> getCustomerOrderTemplateById(@PathVariable Long templateId) {
        return ResponseEntity.ok(this.customerOrderService.getCustomerOrderTemplateById(templateId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<CustomerOrderBase> createCustomerOrder(@RequestBody @Valid CustomerOrderBase customerOrder) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerOrderService.createCustomerOrder(customerOrder));
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping(value = "/{orderId}/templates")
    public ResponseEntity<TemplateCustomerOrderFetch> createTemplateFromCustomerOrder(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.CREATED).body( this.customerOrderService.createTemplateFromCustomerOrder(orderId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping(value = "/templates/{templateId}")
    public ResponseEntity<CustomerOrderBase> createCustomerOrderFromTemplate(@PathVariable Long templateId,
                                                                             @RequestBody @Valid CustomerOrderFromTemp customerOrder)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.customerOrderService.createCustomerOrderFromTemplate(templateId, customerOrder));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping(value = "/{orderId}/jobs")
    public ResponseEntity<JobBase> createJob(@PathVariable Long orderId,@RequestBody @Valid JobBase job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerOrderService.createJob(job, orderId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PatchMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> updateCustomerOrder(
            @PathVariable Long customerOrderId,
            @RequestBody @Valid CustomerOrderBase customerOrder)
    {
        return ResponseEntity.ok(this.customerOrderService.updateCustomerOrder(customerOrder, customerOrderId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/templates/{templateId}")
    public ResponseEntity<TemplateCustomerOrderFetch> deleteTemplateCustomerOrderById(@PathVariable Long templateId) {
        return ResponseEntity.ok(this.customerOrderService.deleteTemplateCustomerOrderById(templateId));
    }

    @SneakyThrows
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{customerOrderId}")
    public ResponseEntity<CustomerOrderBase> deleteCustomerOrder(@PathVariable Long customerOrderId) {
        return ResponseEntity.ok(this.customerOrderService.deleteCustomerOrderById(customerOrderId));
    }
}
