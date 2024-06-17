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
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 50)
    @NotNull
    private String customer;
    @Size(max = 50)
    @NotNull
    private String product;
    @Size(max = 50)
    @NotNull
    private String status;
    @OneToMany (mappedBy = "customerOrder", cascade = CascadeType.ALL)
    private List<Job> jobs;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
