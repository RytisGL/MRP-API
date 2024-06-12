package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    @GetMapping()
    public ResponseEntity<List<StockBase>> getStock(@RequestBody (required = false) StockBase stockBase) {
        return ResponseEntity.ok(this.stockService.getStock(stockBase));
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> getStockById(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.getStockById(stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/requisitions")
    public ResponseEntity<List<RequisitionBase>> getRequisitions(@RequestParam (required = false) String status) {
        return ResponseEntity.ok(this.stockService.getRequisitions(status));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "{stockId}/records")
    public ResponseEntity<List<InventoryUsageRecordBase>> getRequisitionsRecords(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.getRequisitionsHistoryByStockId(stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/{stockId}/porders")
    public ResponseEntity<List<PurchaseOrderBase>> getPurchaseOrdersByStockId(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.getPurchaseOrdersByStockId(stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('USER')")
    @GetMapping(value = "/porders")
    public ResponseEntity<List<PurchaseOrderBase>> getFilteredPurchaseOrders(
            @RequestBody (required = false) PurchaseOrderBase purchaseOrderBase)
    {
        return ResponseEntity.ok(this.stockService.getPurchaseOrders(purchaseOrderBase));
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
    @PutMapping(value = "/requisitions/{requisitionId}")
    public ResponseEntity<InventoryUsageRecordBase> stockRequisition(
            @PathVariable Long requisitionId,
            Authentication authentication)
    {
        return ResponseEntity.ok(this.stockService.stockRequisition(requisitionId, authentication));
    }

    @SneakyThrows
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> deleteStock(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.deleteStockById(stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/porder/{porderId}")
    public ResponseEntity<PurchaseOrderBase> deletePurchaseOrder(@PathVariable Long porderId) {
        return ResponseEntity.ok(this.stockService.deletePurchaseOrderById(porderId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/requisitions/{requisitionId}")
    public ResponseEntity<RequisitionBase> deleteRequisition(@PathVariable Long requisitionId) {
        return ResponseEntity.ok(this.stockService.deleteRequisitionById(requisitionId));
    }
}
