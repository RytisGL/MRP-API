package org.mrp.mrp.utils;

import org.mrp.mrp.dto.auth.AuthenticationRequest;
import org.mrp.mrp.dto.auth.RegistrationRequest;
import org.mrp.mrp.dto.job.JobBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.entities.*;
import org.mrp.mrp.enums.Role;
import org.springframework.security.core.Authentication;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class TestUtils {
    private TestUtils() {
    }

    public static Requisition getTestRequisition() {
        Requisition testRequisition = new Requisition();
        testRequisition.setId(1L);
        testRequisition.setStatus("In progress");
        testRequisition.setQuantity(1f);
        testRequisition.setCreatedAt(LocalDateTime.now());
        testRequisition.setUpdatedAt(LocalDateTime.now());
        return testRequisition;
    }

    public static Job getTestJob() {
        Job testJob = new Job();
        testJob.setJobBlockers(new ArrayList<>());
        testJob.setRequisitions(new ArrayList<>());
        testJob.setJobRecord(new ArrayList<>());
        testJob.setJobBlockers(new ArrayList<>());
        testJob.setCreatedAt(LocalDateTime.now());
        testJob.setUpdatedAt(LocalDateTime.now());
        testJob.setInventoryUsageRecord(new ArrayList<>());
        testJob.setId(1L);
        testJob.setDetails("Details");
        testJob.setStatus("In Progress");
        testJob.setType("Type");
        return testJob;
    }

    public static Stock getTestStock() {
        Stock testStock = new Stock();
        testStock.setId(1L);
        testStock.setQuantity(1f);
        testStock.setCreatedAt(LocalDateTime.now());
        testStock.setUpdatedAt(LocalDateTime.now());
        testStock.setRequisitions(new ArrayList<>());
        testStock.setUnitOfMeasurement("unit");
        testStock.setName("name");
        testStock.setInventoryUsageRecordList(new ArrayList<>());
        testStock.setPurchaseOrders(new ArrayList<>());
        return testStock;
    }

    public static InventoryUsageRecord getTestInventoryUsageRecord() {
        InventoryUsageRecord testInventoryUsageRecord = new InventoryUsageRecord();
        testInventoryUsageRecord.setId(1L);
        testInventoryUsageRecord.setQuantity(1f);
        testInventoryUsageRecord.setCreatedAt(LocalDateTime.now());
        testInventoryUsageRecord.setUpdatedAt(LocalDateTime.now());
        testInventoryUsageRecord.setStatus("In Progress");
        return testInventoryUsageRecord;
    }

    public static User getTestUser() {
        return User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();
    }

    public static AuthenticationRequest getTestAuthenticationRequest() {
        AuthenticationRequest testAuthenticationRequest = new AuthenticationRequest();
        testAuthenticationRequest.setEmail("john.doe@example.com");
        testAuthenticationRequest.setPassword("password");
        return testAuthenticationRequest;
    }

    public static RegistrationRequest getTestRegistrationRequest() {
        RegistrationRequest testRegistrationRequest = new RegistrationRequest();
        testRegistrationRequest.setFirstname("John");
        testRegistrationRequest.setLastname("Doe");
        testRegistrationRequest.setEmail("john.doe@example.com");
        testRegistrationRequest.setPassword("password");
        return testRegistrationRequest;
    }

    public static JobRecord getTestJobRecord() {
        JobRecord testJobRecord = new JobRecord();
        testJobRecord.setStatus("In progress");
        testJobRecord.setId(1L);
        testJobRecord.setCreatedAt(LocalDateTime.now());
        return testJobRecord;
    }

    public static RequisitionBase getTestRequisitionBase() {
        RequisitionBase testRequisitionBase = new RequisitionBase();
        testRequisitionBase.setQuantity(1f);
        testRequisitionBase.setStatus("In progress");
        return testRequisitionBase;
    }

    public static CustomerOrder getTestCustomerOrder() {
        CustomerOrder testCustomerOrder = new CustomerOrder();
        testCustomerOrder.setId(1L);
        testCustomerOrder.setProduct("product");
        testCustomerOrder.setStatus("status");
        testCustomerOrder.setCreatedAt(LocalDateTime.now());
        testCustomerOrder.setUpdatedAt(LocalDateTime.now());
        return testCustomerOrder;
    }

    public static PurchaseOrder getTestPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setStatus("In progress");
        purchaseOrder.setId(1L);
        purchaseOrder.setQuantity(1f);
        purchaseOrder.setDeliveryDate(Date.valueOf(LocalDate.now()));
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        purchaseOrder.setUpdatedAt(LocalDateTime.now());
        return purchaseOrder;
    }
}