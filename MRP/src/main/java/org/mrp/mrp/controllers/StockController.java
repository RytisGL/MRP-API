package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.purchaseorder.PurchaseOrderBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.dto.stock.StockBase;
import org.mrp.mrp.services.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/stock")
@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<List<StockBase>> getStock() {
        return ResponseEntity.ok(this.stockService.getStock());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> getStockById(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.getStockById(stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/requisitions")
    public ResponseEntity<List<RequisitionBase>> getRequisitions() {
        return ResponseEntity.ok(this.stockService.getRequisitions());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "{stockId}/history")
    public ResponseEntity<List<InventoryUsageRecordBase>> getRequisitionsHistory(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.getRequisitionsHistoryByStockId(stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/requisitions/{requisitionId}/history")
    public ResponseEntity<List<InventoryUsageRecordBase>> getRequisitionsHistoryByRequisitionId(@PathVariable Long requisitionId) {
        return ResponseEntity.ok(this.stockService.getRequisitionsHistoryByRequisitionId(requisitionId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/requisitions/available")
    public ResponseEntity<List<RequisitionBase>> getAvailableRequisitions() {
        return ResponseEntity.ok(this.stockService.getAvailableRequisitions());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/filters")
    public ResponseEntity<List<StockBase>> getFilteredStock(@RequestBody StockBase stockBase) {
        return ResponseEntity.ok(this.stockService.getStockFiltered(stockBase));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/porders")
    public ResponseEntity<List<PurchaseOrderBase>> getPurchaseOrders() {
        return ResponseEntity.ok(this.stockService.getPurchaseOrders());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/{stockId}/porders")
    public ResponseEntity<List<PurchaseOrderBase>> getPurchaseOrdersByStockId(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.getPurchaseOrdersByStockId(stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/porders/filters")
    public ResponseEntity<List<PurchaseOrderBase>> getFilteredPurchaseOrders(@RequestBody PurchaseOrderBase purchaseOrderBase) {
        return ResponseEntity.ok(this.stockService.getFilteredPurchaseOrders(purchaseOrderBase));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<StockBase> createStock(@RequestBody @Valid StockBase stockBase) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.stockService.createStock(stockBase));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping(value = "/{stockId}/porders")
    public ResponseEntity<PurchaseOrderBase> createPurchaseOrder(
            @PathVariable Long stockId,
            @RequestBody @Valid PurchaseOrderBase purchaseOrderBase)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.stockService.createPurchaseOrder(purchaseOrderBase, stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PatchMapping(value = "/porders/{purchaseOrderId}")
    public ResponseEntity<StockBase> updateStock(@PathVariable Long purchaseOrderId) {
        return ResponseEntity.ok(this.stockService.updateStock(purchaseOrderId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER' or hasAuthority('USER'))")
    @PatchMapping(value = "/requisitions/{requisitionId}")
    public ResponseEntity<InventoryUsageRecordBase> stockRequisition(
            @PathVariable Long requisitionId,
            Authentication authentication)
    {
        return ResponseEntity.ok(this.stockService.stockRequisition(requisitionId, authentication));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> deleteStock(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.deleteStockById(stockId));
    }
}
