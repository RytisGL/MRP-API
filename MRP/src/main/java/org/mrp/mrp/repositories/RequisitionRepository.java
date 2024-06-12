package org.mrp.mrp.repositories;

import org.mrp.mrp.entities.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequisitionRepository extends JpaRepository <Requisition, Long> {
    List<Requisition> findAllByStockId(Long stockId);
    List<Requisition> findAllByStatus(String status);
}
