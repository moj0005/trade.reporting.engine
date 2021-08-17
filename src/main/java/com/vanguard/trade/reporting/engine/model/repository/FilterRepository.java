package com.vanguard.trade.reporting.engine.model.repository;

import com.vanguard.trade.reporting.engine.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {

    @Query("SELECT f.name FROM Filter f")
    List<String> getAllFilterNames();

    Filter findFilterByExpression(String expression);
}
