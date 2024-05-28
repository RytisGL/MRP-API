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
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String details;
    private String status;
    @OneToMany (mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobStatusHistory> jobStatusHistory;
    @ManyToOne
    CustomerOrder customerOrder;
    @OneToMany (mappedBy = "job", cascade = CascadeType.ALL)
    private List<Requisition> requisitions;
    @OneToMany
    private List<Job> jobBlockers;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
