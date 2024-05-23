package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.StockBase;
import org.mrp.mrp.services.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/stock")
@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockBase>> getStock() {
        return ResponseEntity.ok(this.stockService.getStock());
    }

    @GetMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> getStockById(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.getStockById(stockId));
    }

    @GetMapping(value = "/filters")
    public ResponseEntity<List<StockBase>> getFilteredCustomerOrders(@RequestBody StockBase stockBase) {
        return ResponseEntity.ok(this.stockService.getStockFiltered(stockBase));
    }

    @PostMapping
    public ResponseEntity<StockBase> createCustomerOrder(@RequestBody @Valid StockBase stockBase) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.stockService.createStock(stockBase));
    }

    @PatchMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> updateStock(@PathVariable Long stockId, @RequestBody @Valid StockBase stock) {
        return ResponseEntity.ok(this.stockService.updateStock(stock, stockId));
    }

    @DeleteMapping(value = "/{stockId}")
    public ResponseEntity<StockBase> deleteStock(@PathVariable Long stockId) {
        return ResponseEntity.ok(this.stockService.deleteStockById(stockId));
    }
}
