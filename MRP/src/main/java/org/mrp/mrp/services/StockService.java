package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.StockConverter;
import org.mrp.mrp.dto.StockBase;
import org.mrp.mrp.entities.Stock;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.repositories.StockRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockRepository stockRepository;

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
}
