package org.mrp.mrp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class InventoryUsageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float quantity;
    @Size(max = 50)
    @NotNull
    private String status;
    @ManyToOne
    private Job job;
    @ManyToOne
    private Stock stock;
    @ManyToOne @JoinColumn(referencedColumnName="email")
    private User user;
    @NotNull
    @CreationTimestamp
    private LocalDateTime createdAt;
    @NotNull
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
