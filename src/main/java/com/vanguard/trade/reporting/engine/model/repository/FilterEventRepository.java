package com.vanguard.trade.reporting.engine.model.repository;

import com.vanguard.trade.reporting.engine.model.FilterCriteriaExecutionGroup;
import com.vanguard.trade.reporting.engine.model.FilterEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilterEventRepository extends JpaRepository<FilterEvent, Long> {

    @Query("SELECT DISTINCT fe from FilterEvent fe WHERE fe.filter.id=:filterId")
    Optional<List<FilterEvent>> findFilterEventsByFilterId(Long filterId);

    Optional<List<FilterEvent>> findFilterEventsByEventName(String eventName);

    @Query("SELECT fe from FilterEvent fe WHERE fe.filter.id=:filterId AND fe.eventName=:eventName")
    Optional<FilterEvent> findFilterEventsByFilterIdAndEventName(Long filterId, String eventName);

    @Query("SELECT DISTINCT fe.eventName FROM FilterEvent fe where fe.eventName = ?1")
    Optional<String> findFilterEventByEventName(String eventName);

}
