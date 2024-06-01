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
public class TemplateJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String details;
    private String status;
    @ManyToOne ()
    TemplateCustomerOrder customerOrder;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<TemplateRequisition> requisitions;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TemplateJob> jobBlockers;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
