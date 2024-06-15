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
public class TemplateCustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 50)
    @NotNull
    private String product;
    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL)
    private List<TemplateJob> jobs;
    @NotNull
    @CreationTimestamp
    private LocalDateTime createdAt;
    @NotNull
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
