package com.vanguard.trade.reporting.engine.model.repository;

import com.vanguard.trade.reporting.engine.model.FilterCriteriaExecutionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilterCriteriaExecutionGroupRepository extends JpaRepository<FilterCriteriaExecutionGroup, Long> {

    @Query("SELECT DISTINCT fceg.groupName from FilterCriteriaExecutionGroup fceg")
    Optional<List<String>> findDistinctGroupNames();

    Optional<List<FilterCriteriaExecutionGroup>> findFilterCriteriaExecutionGroupByGroupName(String groupName);

}
