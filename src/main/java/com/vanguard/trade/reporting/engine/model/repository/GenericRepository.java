package com.vanguard.trade.reporting.engine.model.repository;

import com.vanguard.trade.reporting.engine.model.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericRepository extends JpaRepository<ID, Long> {
}
