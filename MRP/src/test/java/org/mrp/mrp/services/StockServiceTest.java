package org.mrp.mrp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mrp.mrp.dto.inventoryusagerecord.InventoryUsageRecordBase;
import org.mrp.mrp.dto.purchaseorder.PurchaseOrderBase;
import org.mrp.mrp.dto.requisition.RequisitionBase;
import org.mrp.mrp.dto.stock.StockBase;
import org.mrp.mrp.entities.*;
import org.mrp.mrp.exceptions.customexceptions.ValidationConstraintException;
import org.mrp.mrp.repositories.PurchaseOrderRepository;
import org.mrp.mrp.repositories.RequisitionRepository;
import org.mrp.mrp.repositories.StockRepository;
import org.mrp.mrp.repositories.UserRepository;
import org.mrp.mrp.utils.TestUtils;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private RequisitionRepository requisitionRepository;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private UserRepository userRepository;

    private Stock stock;
    private StockBase stockBase;
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderBase purchaseOrderBase;
    private Requisition requisition;
    private final Long id = 1L;
    private User user;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stock = TestUtils.getTestStock();
        stockBase = new StockBase();
        purchaseOrder = TestUtils.getTestPurchaseOrder();
        user = TestUtils.getTestUser();
        authentication = mock(Authentication.class);
        purchaseOrderBase = new PurchaseOrderBase();
        requisition = TestUtils.getTestRequisition();
    }

    @Test
    void testGetStockById() {
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        StockBase result = stockService.getStockById(id);
        assertNotNull(result);
    }

    @Test
    void testGetStockByIdNotFound() {
        when(stockRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.getStockById(id));
        assertEquals("validation.constraints.stock.name", exception.getMessage());
    }

    @Test
    void testCreateStock() {
        when(stockRepository.saveAndFlush(any(Stock.class))).thenReturn(stock);

        StockBase result = stockService.createStock(stockBase);

        assertNotNull(result);
        verify(stockRepository).saveAndFlush(any(Stock.class));
    }

    @Test
    void testDeleteStockById() throws ValidationConstraintException {
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));

        StockBase result = stockService.deleteStockById(id);

        assertNotNull(result);
        verify(stockRepository).findById(id);
        verify(stockRepository).delete(any(Stock.class));
    }

    @Test
    void testDeleteStockByIdNotFound() {
        when(stockRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.deleteStockById(id));
        assertEquals("validation.constraints.stock.name", exception.getMessage());
    }

    @Test
    void testGetStock() {
        when(stockRepository.findAll()).thenReturn(Collections.singletonList(stock));

        List<StockBase> result = stockService.getStock(null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(stockRepository).findAll();
    }

    @Test
    void testGetStockWithFilterFiltered() {
        when(stockRepository.findAll(ArgumentMatchers.<Example<Stock>>any())).thenReturn(Collections.singletonList(stock));

        List<StockBase> result = stockService.getStock(stockBase);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(stockRepository).findAll(ArgumentMatchers.<Example<Stock>>any());
    }

    @Test
    void testGetPurchaseOrders() {
        when(purchaseOrderRepository.findAll()).thenReturn(Collections.singletonList(purchaseOrder));

        List<PurchaseOrderBase> result = stockService.getPurchaseOrders(null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(purchaseOrderRepository).findAll();
    }

    @Test
    void testGetPurchaseOrdersWithFilter() {
        when(purchaseOrderRepository.findAll(ArgumentMatchers.<Example<PurchaseOrder>>any()))
                .thenReturn(Collections.singletonList(purchaseOrder));

        List<PurchaseOrderBase> result = stockService.getPurchaseOrders(purchaseOrderBase);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(purchaseOrderRepository).findAll(ArgumentMatchers.<Example<PurchaseOrder>>any());
    }

    @Test
    void testGetRequisitions() {
        when(requisitionRepository.findAll()).thenReturn(Collections.singletonList(requisition));

        List<RequisitionBase> result = stockService.getRequisitions(null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(requisitionRepository).findAll();
    }

    @Test
    void testGetRequisitionsWithStatus() {
        String status = "Complete";
        when(requisitionRepository.findAllByStatus(status)).thenReturn(Collections.singletonList(requisition));

        List<RequisitionBase> result = stockService.getRequisitions(status);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(requisitionRepository).findAllByStatus(status);
    }

    @Test
    void testGetRequisitionsHistoryByStockId() {
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));

        List<InventoryUsageRecordBase> result = stockService.getRequisitionsHistoryByStockId(id);

        assertNotNull(result);
        verify(stockRepository).findById(id);
    }

    @Test
    void testDeletePurchaseOrderById() {
        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.of(purchaseOrder));

        PurchaseOrderBase result = stockService.deletePurchaseOrderById(id);

        assertNotNull(result);
        verify(purchaseOrderRepository).findById(id);
        verify(purchaseOrderRepository).delete(any(PurchaseOrder.class));
    }

    @Test
    void testDeleteRequisitionById() {
        when(requisitionRepository.findById(id)).thenReturn(Optional.of(requisition));

        RequisitionBase result = stockService.deleteRequisitionById(id);

        assertNotNull(result);
        verify(requisitionRepository).findById(id);
        verify(requisitionRepository).delete(any(Requisition.class));
    }

    @Test
    void testCreatePurchaseOrder() {
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        when(purchaseOrderRepository.saveAndFlush(any(PurchaseOrder.class))).thenReturn(purchaseOrder);

        PurchaseOrderBase result = stockService.createPurchaseOrder(purchaseOrderBase, id);

        assertNotNull(result);
        verify(stockRepository).findById(id);
        verify(purchaseOrderRepository).saveAndFlush(any(PurchaseOrder.class));
    }

    @Test
    void testGetPurchaseOrdersByStockId() {
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        List<PurchaseOrderBase> result = stockService.getPurchaseOrdersByStockId(1L);
        assertNotNull(result);
        verify(stockRepository).findById(id);
    }

    @Test
    void testUpdateStock() {
        purchaseOrder.setStock(stock);
        float testQty = purchaseOrder.getQuantity() + stock.getQuantity();

        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.of(purchaseOrder));

        StockBase result = stockService.updateStock(id);

        assertEquals(testQty, result.getQuantity());
        assertNotNull(result);
        verify(purchaseOrderRepository).findById(id);
        verify(purchaseOrderRepository).saveAndFlush(any(PurchaseOrder.class));
    }

    @Test
    void testUpdateStockUpdateRequisitionsOutOfStockStatusUpdates() {
        purchaseOrder.setStock(stock);
        requisition.setStatus("Out of stock");
        List<Requisition> requisitions = new ArrayList<>();
        requisitions.add(requisition);

        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.of(purchaseOrder));
        when(requisitionRepository.findAllByStockId(id)).thenReturn(requisitions);

        stockService.updateStock(id);

        assertEquals("In progress", requisitions.get(0).getStatus());
    }

    @Test
    void testStockRequisition() {
        requisition.setStock(stock);

        when(authentication.getName()).thenReturn(user.getEmail());
        when(requisitionRepository.findById(id)).thenReturn(Optional.of(requisition));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        InventoryUsageRecordBase result = stockService.stockRequisition(id, authentication);

        assertNotNull(result);
        verify(requisitionRepository).findById(id);
        verify(userRepository).findByEmail(anyString());
    }

    @Test
    void testGetRequisitionsHistoryByStockIdNotFound() {
        when(stockRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.getRequisitionsHistoryByStockId(id));

        assertEquals("validation.constraints.stock.name", exception.getMessage());
    }

    @Test
    void testDeletePurchaseOrderByIdNotFound() {
        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.deletePurchaseOrderById(id));

        assertEquals("validation.constraints.purchase_order.name", exception.getMessage());
    }

    @Test
    void testDeleteRequisitionByIdNotFound() {
        when(requisitionRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.deleteRequisitionById(id));

        assertEquals("validation.constraints.requisition.name", exception.getMessage());
    }

    @Test
    void testCreatePurchaseOrderStockNotFound() {
        when(stockRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.createPurchaseOrder(purchaseOrderBase, id));

        assertEquals("validation.constraints.stock.name", exception.getMessage());
    }

    @Test
    void testGetPurchaseOrdersByStockIdNotFound() {
        when(stockRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.getPurchaseOrdersByStockId(id));

        assertEquals("validation.constraints.stock.name", exception.getMessage());
    }

    @Test
    void testUpdateStockPurchaseOrderNotFound() {
        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.updateStock(id));

        assertEquals("validation.constraints.purchase_order.name", exception.getMessage());
    }

    @Test
    void testUpdateStockPurchaseOrderComplete() {
        purchaseOrder.setStatus("Complete");

        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.of(purchaseOrder));
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.updateStock(id));

        assertEquals("validation.constraints.purchase_order.name", exception.getMessage());
    }

    @Test
    void testStockRequisitionRequisitionNotFound() {
        when(requisitionRepository.findById(id)).thenReturn(Optional.empty());
        when(authentication.getName()).thenReturn(user.getEmail());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.stockRequisition(id, authentication));

        assertEquals("validation.constraints.requisition.name", exception.getMessage());
    }

    @Test
    void testStockRequisition_UserNotFound() {
        when(requisitionRepository.findById(id)).thenReturn(Optional.of(requisition));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> stockService.stockRequisition(id, authentication));

        assertEquals("validation.constraints.user.name", exception.getMessage());
    }
}
