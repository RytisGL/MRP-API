package org.mrp.mrp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Float quantity;
    private String unitOfMeasurement;
    @OneToMany(mappedBy = "stock")
    private List<RequisitionStock> requisitionStockList;
    @OneToMany(mappedBy = "stock")
    private List<PurchaseOrder> purchaseOrders;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
