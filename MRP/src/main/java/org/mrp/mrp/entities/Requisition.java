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
public class Requisition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float quantity;
    private String status;
    @ManyToOne
    private Job job;
    @ManyToOne
    private Stock stock;
    @OneToMany (mappedBy = "requisition", cascade = CascadeType.ALL)
    private List<InventoryUsageRecord> inventoryUsageRecordList;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
