package org.mrp.mrp.controllers;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.entities.CustomerOrder;
import org.mrp.mrp.services.CustomerOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/admin")
@RestController
@RequiredArgsConstructor
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;
    //JobService
    //RequisitionService
    //StockService
    @GetMapping
    public List<CustomerOrder> getAllCustomerOrders() {
        return null;
    }
    @GetMapping (value = "/{customerOrderId}")
    public CustomerOrder getCustomerOrderById(@PathVariable Long customerOrderId) {
        return null;
    }
    @GetMapping(value = "/filter")
    public List<CustomerOrder> filterCustomerOrders(@RequestBody CustomerOrder customerOrder) {
        return null;
    }
    @PostMapping
    public CustomerOrder createCustomerOrder(@RequestBody CustomerOrder customerOrder) {
        return null;
    }
    @PatchMapping (value = "/{customerOrderId}")
    public CustomerOrder updateCustomerOrder(@PathVariable Long customerOrderId, @RequestBody CustomerOrder customerOrder) {
        return null;
    }
    @DeleteMapping (value = "/{customerOrderId}")
    public void deleteCustomerOrder(@PathVariable Long customerOrderId) {

    }
}
