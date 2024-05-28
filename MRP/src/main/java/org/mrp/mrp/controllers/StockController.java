package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.stock.StockBase;
import org.mrp.mrp.services.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping(value = "/filters")
    public ResponseEntity<List<StockBase>> getFilteredCustomerOrders(@RequestBody StockBase stockBase) {
        return ResponseEntity.ok(this.stockService.getStockFiltered(stockBase));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<StockBase> createStock(@RequestBody @Valid StockBase stockBase) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.stockService.createStock(stockBase));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PatchMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> updateStock(@PathVariable Long stockId, @RequestBody @Valid StockBase stock) {
        return ResponseEntity.ok(this.stockService.updateStock(stock, stockId));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PatchMapping(value = "/requisitions/{requisitionId}")
    public ResponseEntity<InventoryUsageRecordBase> stockRequisition(@PathVariable Long requisitionId) {
        return ResponseEntity.ok(this.stockService.stockRequisition(requisitionId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> deleteStock(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.deleteStockById(stockId));
    }
}
