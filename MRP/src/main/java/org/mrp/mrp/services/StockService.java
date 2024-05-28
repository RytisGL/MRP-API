package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.InventoryUsageRecordConverter;
import org.mrp.mrp.converters.StockConverter;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.stock.StockBase;
import org.mrp.mrp.entities.InventoryUsageRecord;
import org.mrp.mrp.entities.Requisition;
import org.mrp.mrp.entities.Stock;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.repositories.RequisitionRepository;
import org.mrp.mrp.repositories.StockRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockRepository stockRepository;
    private final RequisitionRepository requisitionRepository;

    public StockBase getStockById(Long stockId) {
        return StockConverter.stockToDTO(this.stockRepository.findById(stockId).orElseThrow(), TypeDTO.FETCH);
    }

    public StockBase createStock(StockBase stockBase) {
        return StockConverter.stockToDTO(stockRepository.saveAndFlush(StockConverter.stockDTOTostock(stockBase)), TypeDTO.FETCH);
    }

    public List<StockBase> getStock() {
        return StockConverter.stockListToStockDTOs(this.stockRepository.findAll(), TypeDTO.FETCH);
    }

    public StockBase deleteStockById(Long stockId) {
        Stock stock = this.stockRepository.findById(stockId).orElseThrow();
        this.stockRepository.delete(stock);
        return StockConverter.stockToDTO(stock, TypeDTO.FETCH);
    }

    public StockBase updateStock(StockBase stockBase, Long stockId) {
        Stock stock = this.stockRepository.findById(stockId).orElseThrow();
        StockConverter.updateStockDTOToStock(stockBase, stock);
        this.stockRepository.saveAndFlush(stock);
        return StockConverter.stockToDTO(stock, TypeDTO.FETCH);
    }

    public List<StockBase> getStockFiltered(StockBase filterStockBase) {
        Stock stock = StockConverter.stockDTOTostock(filterStockBase);
        return StockConverter.stockListToStockDTOs(this.stockRepository.findAll(Example.of(stock)), TypeDTO.FETCH);
    }

    @Transactional
    public InventoryUsageRecordBase stockRequisition(Long requisitionId) {
        InventoryUsageRecord inventoryUsageRecord = new InventoryUsageRecord();
        Requisition requisition = this.requisitionRepository.findById(requisitionId).orElseThrow();
        if (requisition.getStatus().equals("Complete")) {
            throw new NoSuchElementException();
        }
        Stock stock = requisition.getStock();
        inventoryUsageRecord.setStock(stock);
        inventoryUsageRecord.setRequisition(requisition);
        if (stock.getQuantity() < requisition.getQuantity()) {
            return InventoryUsageRecordConverter.inventoryUsageRecordToDTO(outOfStock(requisition, inventoryUsageRecord, stock),TypeDTO.BASE);
        }
        return InventoryUsageRecordConverter.inventoryUsageRecordToDTO(inStock(requisition, inventoryUsageRecord, stock),TypeDTO.BASE);
    }

    private InventoryUsageRecord outOfStock(Requisition requisition, InventoryUsageRecord inventoryUsageRecord, Stock stock) {
        requisition.setStatus("Out of stock");
        inventoryUsageRecord.setQuantity(null);
        inventoryUsageRecord.setStatus("Out of stock");
        stock.getInventoryUsageRecordList().add(inventoryUsageRecord);
        this.stockRepository.saveAndFlush(stock);
        this.requisitionRepository.saveAndFlush(requisition);
        return inventoryUsageRecord;
    }

    private InventoryUsageRecord inStock(Requisition requisition, InventoryUsageRecord inventoryUsageRecord, Stock stock) {
        stock.setQuantity(stock.getQuantity() - requisition.getQuantity());
        requisition.setStatus("Complete");
        inventoryUsageRecord.setQuantity(requisition.getQuantity());
        inventoryUsageRecord.setStatus("Complete");
        stock.getInventoryUsageRecordList().add(inventoryUsageRecord);
        this.stockRepository.saveAndFlush(stock);
        this.requisitionRepository.saveAndFlush(requisition);
        return inventoryUsageRecord;
    }
}
