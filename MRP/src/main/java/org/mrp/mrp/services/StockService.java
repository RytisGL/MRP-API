package org.mrp.mrp.services;

import jakarta.persistence.EntityNotFoundException;
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
import org.mrp.mrp.exceptions.customexceptions.ValidationConstraintException;
import org.mrp.mrp.repositories.PurchaseOrderRepository;
import org.mrp.mrp.repositories.RequisitionRepository;
import org.mrp.mrp.repositories.StockRepository;
import org.mrp.mrp.repositories.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private static final String STOCK_EXCEPTION_MESSAGE = "validation.constraints.stock.name";
    private static final String PURCHASE_ORDER_EX_MESSAGE = "validation.constraints.purchase_order.name";

    public StockBase getStockById(Long stockId) {
        return StockConverter.stockToDTO(
                this.stockRepository.findById(stockId)
                        .orElseThrow(() -> new EntityNotFoundException(STOCK_EXCEPTION_MESSAGE)),
                TypeDTO.FETCH
        );
    }

    public StockBase createStock(StockBase stockBase) {
        return StockConverter.stockToDTO(
                stockRepository.saveAndFlush(
                        StockConverter.stockDTOTostock(stockBase)), TypeDTO.FETCH);
    }


    public StockBase deleteStockById(Long stockId) throws ValidationConstraintException {
        Stock stock = this.stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException(STOCK_EXCEPTION_MESSAGE));

        validateStockConstraints(stock);

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
                this.stockRepository.findById(stockId)
                        .orElseThrow(() -> new EntityNotFoundException(STOCK_EXCEPTION_MESSAGE))
                        .getInventoryUsageRecordList(), TypeDTO.FETCH);
    }

    public PurchaseOrderBase deletePurchaseOrderById(Long purchaseOrderId) {
        PurchaseOrder order = this.purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException(PURCHASE_ORDER_EX_MESSAGE));

        this.purchaseOrderRepository.delete(order);
        return PurchaseOrderConverter.purchaseOrderToDTO(order, TypeDTO.FETCH);
    }


    public RequisitionBase deleteRequisitionById(Long requisitionId) {
        Requisition requisition = this.requisitionRepository
                .findById(requisitionId).orElseThrow(() -> new EntityNotFoundException("validation.constraints.requisition.name"));

        this.requisitionRepository.delete(requisition);
        return RequisitionConverter.requisitionToDTO(requisition, TypeDTO.FETCH);
    }

    public PurchaseOrderBase createPurchaseOrder(PurchaseOrderBase purchaseOrderBase, Long stockId) {
        Stock stock = this.stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException(STOCK_EXCEPTION_MESSAGE));

        PurchaseOrder purchaseOrder = PurchaseOrderConverter.purchaseOrderDTOToPurchaseOrder(purchaseOrderBase);
        purchaseOrder.setStock(stock);
        this.purchaseOrderRepository.saveAndFlush(purchaseOrder);
        return PurchaseOrderConverter.purchaseOrderToDTO(purchaseOrder, TypeDTO.FETCH);
    }

    public List<PurchaseOrderBase> getPurchaseOrdersByStockId(Long stockId) {
        return PurchaseOrderConverter.purchaseOrdersToPurchaseOrderDTOs(
                this.stockRepository.findById(stockId)
                        .orElseThrow(() -> new EntityNotFoundException(STOCK_EXCEPTION_MESSAGE))
                        .getPurchaseOrders(), TypeDTO.FETCH);
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

    public List<RequisitionBase> getRequisitions(String status) {
        if (status == null) {
            return RequisitionConverter.requisitionsToRequisitionDTOs(
                    this.requisitionRepository.findAll(), TypeDTO.FETCH
            );
        }
        return RequisitionConverter.requisitionsToRequisitionDTOs(
                this.requisitionRepository.findAllByStatus(status), TypeDTO.FETCH
        );
    }

    @Transactional
    public StockBase updateStock(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException(PURCHASE_ORDER_EX_MESSAGE));

        if (purchaseOrder.getStatus().equals(COMPLETE)) {
            throw new EntityNotFoundException(PURCHASE_ORDER_EX_MESSAGE);
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

        Requisition requisition = this.requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new EntityNotFoundException("validation.constraints.requisition.name"));

        Stock stock = requisition.getStock();

        inventoryUsageRecord.setUser(
                this.userRepository.findByEmail(authentication.getName())
                        .orElseThrow(() -> new EntityNotFoundException("validation.constraints.user.name")));

        inventoryUsageRecord.setStock(stock);
        inventoryUsageRecord.setJob(requisition.getJob());
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
        inventoryUsageRecord.setQuantity(requisition.getQuantity());
        inventoryUsageRecord.setStatus(COMPLETE);
        stock.getInventoryUsageRecordList().add(inventoryUsageRecord);
        this.stockRepository.saveAndFlush(stock);
        this.requisitionRepository.delete(requisition);
        return inventoryUsageRecord;
    }

    private void validateStockConstraints(Stock stock) throws ValidationConstraintException {
        if (!stock.getPurchaseOrders().isEmpty()) {
            throw new ValidationConstraintException("validation.constraints.purchase_order.message");
        }
        if (!stock.getRequisitions().isEmpty()) {
            throw new ValidationConstraintException("validation.constraints.requisition.message");
        }
        if (!stock.getInventoryUsageRecordList().isEmpty()) {
            throw new ValidationConstraintException("validation.constraints.inventory_usage_records.message");
        }
    }
}
