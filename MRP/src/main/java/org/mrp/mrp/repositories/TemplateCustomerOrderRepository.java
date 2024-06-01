package org.mrp.mrp.repositories;

import org.mrp.mrp.entities.TemplateCustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateCustomerOrderRepository extends JpaRepository<TemplateCustomerOrder, Long> {
}