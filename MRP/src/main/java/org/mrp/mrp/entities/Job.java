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
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 50)
    @NotNull
    private String type;
    @Size(max = 50)
    @NotNull
    private String details;
    @Size(max = 50)
    @NotNull
    private String status;
    @OneToMany (mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobRecord> jobRecord;
    @ManyToOne
    CustomerOrder customerOrder;
    @OneToMany (mappedBy = "job")
    List<InventoryUsageRecord> inventoryUsageRecord;
    @OneToMany (mappedBy = "job", cascade = CascadeType.ALL)
    private List<Requisition> requisitions;
    @OneToMany (cascade = CascadeType.ALL)
    private List<Job> jobBlockers;
    @NotNull
    @CreationTimestamp
    private LocalDateTime createdAt;
    @NotNull
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
