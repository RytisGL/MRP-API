package org.mrp.mrp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50)
    @NotNull
    private String name;
    private Float quantity;
    @Size(max = 50)
    @NotNull
    private String unitOfMeasurement;
    @OneToMany(mappedBy = "stock")
    private List<Requisition> requisitions;
    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL)
    private List<InventoryUsageRecord> inventoryUsageRecordList;
    @OneToMany(mappedBy = "stock")
    private List<PurchaseOrder> purchaseOrders;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
