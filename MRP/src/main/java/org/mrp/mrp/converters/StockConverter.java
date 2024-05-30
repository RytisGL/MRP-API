package org.mrp.mrp.converters;

import org.mrp.mrp.dto.stock.StockBase;
import org.mrp.mrp.dto.stock.StockFetch;
import org.mrp.mrp.entities.Stock;
import org.mrp.mrp.enums.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class StockConverter {
    private StockConverter(){}

    public static Stock stockDTOTostock(StockBase dto) {
        Stock stock = new Stock();
        stock.setName(dto.getName());
        stock.setQuantity(dto.getQuantity());
        stock.setUnitOfMeasurement(dto.getUnitOfMeasurement());
        return stock;
    }

    public static StockBase stockToDTO(Stock stock, TypeDTO type) {
        StockBase dto;

        if (type == TypeDTO.BASE) {
            dto = new StockBase();
        } else if (type == TypeDTO.FETCH) {
            dto = new StockFetch();
            ((StockFetch) dto).setId(stock.getId());
        } else {
            throw new IllegalArgumentException("Invalid StockType");
        }

        dto.setName(stock.getName());
        dto.setQuantity(stock.getQuantity());
        dto.setUnitOfMeasurement(stock.getUnitOfMeasurement());

        return dto;
    }

    public static List<StockBase> stockListToStockDTOs(List<Stock> stockList, TypeDTO type) {
        List<StockBase> dtos = new ArrayList<>();
        for (Stock stock : stockList) {
            dtos.add(stockToDTO(stock, type));
        }
        return dtos;
    }

}
