package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.InventoryUsageRecordConverter;
import org.mrp.mrp.converters.PurchaseOrderConverter;
import org.mrp.mrp.converters.RequisitionConverter;
import org.mrp.mrp.converters.StockConverter;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.purchaseorder.PurchaseOrderBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.dto.stock.StockBase;
import org.mrp.mrp.entities.InventoryUsageRecord;
import org.mrp.mrp.entities.PurchaseOrder;
import org.mrp.mrp.entities.Requisition;
import org.mrp.mrp.entities.Stock;
import org.mrp.mrp.enums.TypeDTO;
import org.mrp.mrp.repositories.PurchaseOrderRepository;
import org.mrp.mrp.repositories.RequisitionRepository;
import org.mrp.mrp.repositories.StockRepository;
import org.mrp.mrp.repositories.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockRepository stockRepository;
    private final RequisitionRepository requisitionRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    private static final String COMPLETE = "Complete";
    private static final String OUT_OF_STOCK = "Out of stock";
    private static final String IN_PROGRESS = "In progress";

    public StockBase getStockById(Long stockId) {
        return StockConverter.stockToDTO(
                this.stockRepository.findById(stockId).orElseThrow(), TypeDTO.FETCH
        );
    }

    public StockBase createStock(StockBase stockBase) {
        return StockConverter.stockToDTO(
                stockRepository.saveAndFlush(
                        StockConverter.stockDTOTostock(stockBase)), TypeDTO.FETCH);
    }


    public StockBase deleteStockById(Long stockId) {
        Stock stock = this.stockRepository.findById(stockId).orElseThrow();
        this.stockRepository.delete(stock);
        return StockConverter.stockToDTO(stock, TypeDTO.FETCH);
    }

    public List<StockBase> getStock(StockBase filterStockBase) {
        if (filterStockBase == null) {
            return StockConverter.stockListToStockDTOs(this.stockRepository.findAll(), TypeDTO.FETCH);
        }
        Stock stock = StockConverter.stockDTOTostock(filterStockBase);
        return StockConverter.stockListToStockDTOs(this.stockRepository.findAll(Example.of(stock)), TypeDTO.FETCH);
    }

    public List<InventoryUsageRecordBase> getRequisitionsHistoryByStockId(Long stockId) {
        return InventoryUsageRecordConverter.inventoryUsageRecordsToInventoryUsageRecordDTOs(
                this.stockRepository.findById(stockId).orElseThrow().getInventoryUsageRecordList(), TypeDTO.FETCH);
    }

    public PurchaseOrderBase createPurchaseOrder(PurchaseOrderBase purchaseOrderBase, Long stockId) {
        Stock stock = this.stockRepository.findById(stockId).orElseThrow();
        List<PurchaseOrder> purchaseOrders = stock.getPurchaseOrders();
        PurchaseOrder purchaseOrder = PurchaseOrderConverter.purchaseOrderDTOToPurchaseOrder(purchaseOrderBase);
        purchaseOrders.add(purchaseOrder);
        this.stockRepository.saveAndFlush(stock);
        return PurchaseOrderConverter.purchaseOrderToDTO(purchaseOrder, TypeDTO.FETCH);
    }

    public List<PurchaseOrderBase> getPurchaseOrdersByStockId(Long stockId) {
        return PurchaseOrderConverter.purchaseOrdersToPurchaseOrderDTOs(
                this.stockRepository.findById(stockId).orElseThrow().getPurchaseOrders(), TypeDTO.FETCH);
    }

    public List<PurchaseOrderBase> getPurchaseOrders(PurchaseOrderBase purchaseOrderBase) {
        if (purchaseOrderBase == null) {
            return PurchaseOrderConverter.purchaseOrdersToPurchaseOrderDTOs(
                    this.purchaseOrderRepository.findAll(), TypeDTO.FETCH);
        }
        PurchaseOrder purchaseOrder = PurchaseOrderConverter.purchaseOrderDTOToPurchaseOrder(purchaseOrderBase);
        return PurchaseOrderConverter.purchaseOrdersToPurchaseOrderDTOs(
                this.purchaseOrderRepository
                        .findAll(Example.of(purchaseOrder)), TypeDTO.FETCH);
    }

    public List<RequisitionBase> getRequisitions() {
        return RequisitionConverter.requisitionsToRequisitionDTOs(
                this.requisitionRepository.findAll(), TypeDTO.FETCH
        );
    }

    public List<RequisitionBase> getAvailableRequisitions() {
        List<RequisitionBase> requisitionBaseList = new ArrayList<>();
        for (Requisition requisition : this.requisitionRepository.findAll()) {
            if (requisition.getStatus().equals(COMPLETE) || requisition.getStatus().equals(OUT_OF_STOCK)) {
                continue;
            }
            requisitionBaseList.add(
                    RequisitionConverter.requisitionToDTO(requisition, TypeDTO.FETCH)
            );
        }
        return requisitionBaseList;
    }

    @Transactional
    public StockBase updateStock(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(purchaseOrderId).orElseThrow();
        if (purchaseOrder.getStatus().equals(COMPLETE)) {
            throw new NoSuchElementException();
        }
        Stock stock = purchaseOrder.getStock();
        stock.setQuantity(stock.getQuantity() + purchaseOrder.getQuantity());
        purchaseOrder.setStock(stock);
        purchaseOrder.setStatus(COMPLETE);
        this.purchaseOrderRepository.saveAndFlush(purchaseOrder);
        this.changeRequisitionStatus(this.requisitionRepository.findAllByStockId(stock.getId()));
        return StockConverter.stockToDTO(stock, TypeDTO.FETCH);
    }

    @Transactional
    public InventoryUsageRecordBase stockRequisition(Long requisitionId, Authentication authentication) {
        InventoryUsageRecord inventoryUsageRecord = new InventoryUsageRecord();
        Requisition requisition = this.requisitionRepository.findById(requisitionId).orElseThrow();
        if (requisition.getStatus().equals(COMPLETE)) {
            throw new NoSuchElementException();
        }
        Stock stock = requisition.getStock();
        inventoryUsageRecord.setUser(
                this.userRepository.findByEmail(authentication.getName()).orElseThrow()
        );
        inventoryUsageRecord.setStock(stock);
        inventoryUsageRecord.setRequisition(requisition);
        if (stock.getQuantity() < requisition.getQuantity()) {
            return InventoryUsageRecordConverter.inventoryUsageRecordToDTO(
                    outOfStock(requisition, inventoryUsageRecord, stock), TypeDTO.BASE
            );
        }
        return InventoryUsageRecordConverter.inventoryUsageRecordToDTO(
                inStock(requisition, inventoryUsageRecord, stock), TypeDTO.BASE);
    }

    private void changeRequisitionStatus(List<Requisition> requisitions) {
        for (Requisition requisition : requisitions) {
            if (requisition.getStatus().equals(OUT_OF_STOCK)) {
                requisition.setStatus(IN_PROGRESS);
            }
        }
        this.requisitionRepository.saveAllAndFlush(requisitions);
    }

    private InventoryUsageRecord outOfStock(Requisition requisition, InventoryUsageRecord inventoryUsageRecord, Stock stock) {
        requisition.setStatus(OUT_OF_STOCK);
        inventoryUsageRecord.setQuantity(null);
        inventoryUsageRecord.setStatus(OUT_OF_STOCK);
        stock.getInventoryUsageRecordList().add(inventoryUsageRecord);
        this.stockRepository.saveAndFlush(stock);
        this.requisitionRepository.saveAndFlush(requisition);
        return inventoryUsageRecord;
    }

    private InventoryUsageRecord inStock(Requisition requisition, InventoryUsageRecord inventoryUsageRecord, Stock stock) {
        stock.setQuantity(stock.getQuantity() - requisition.getQuantity());
        requisition.setStatus(COMPLETE);
        inventoryUsageRecord.setQuantity(requisition.getQuantity());
        inventoryUsageRecord.setStatus(COMPLETE);
        stock.getInventoryUsageRecordList().add(inventoryUsageRecord);
        this.stockRepository.saveAndFlush(stock);
        this.requisitionRepository.saveAndFlush(requisition);
        return inventoryUsageRecord;
    }

    public List<InventoryUsageRecordBase> getRequisitionsHistoryByRequisitionId(Long requisitionId) {
        return InventoryUsageRecordConverter.inventoryUsageRecordsToInventoryUsageRecordDTOs(
                this.requisitionRepository
                        .findById(requisitionId)
                        .orElseThrow()
                        .getInventoryUsageRecordList(), TypeDTO.FETCH);
    }
}
