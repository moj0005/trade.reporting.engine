package com.vanguard.trade.reporting.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vanguard.trade.reporting.engine.exception.ResourceNotFoundException;
import com.vanguard.trade.reporting.engine.model.Filter;
import com.vanguard.trade.reporting.engine.model.FilterCriteria;
import com.vanguard.trade.reporting.engine.model.FilterCriteriaExecutionGroup;
import com.vanguard.trade.reporting.engine.model.FilterEvent;
import com.vanguard.trade.reporting.engine.model.repository.FilterCriteriaExecutionGroupRepository;
import com.vanguard.trade.reporting.engine.model.repository.FilterEventRepository;
import com.vanguard.trade.reporting.engine.model.repository.FilterRepository;
import com.vanguard.trade.reporting.engine.util.EventParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class EventService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FilterCriteriaExecutionGroupRepository filterCriteriaExecutionGroupRepository;

    @Autowired
    private FilterEventRepository filterEventRepository;

    @Autowired
    FilterRepository filterRepository;

    @Autowired
    EventParser eventParser;

    public boolean fileAlreadyParsed(String fileName) {
        Optional<String> filterEventOptional = filterEventRepository.findFilterEventByEventName(fileName);
        if (filterEventOptional.isPresent() && fileName.equals(filterEventOptional.get())) {
            return true;
        }
        return false;
    }

    public List<FilterEvent> processFile(String fileName, InputStream inputStream) {
        List<Filter> filterList = filterRepository.findAll();
        List<FilterEvent> filterEventList = null;

        if (filterList != null && !filterList.isEmpty()) {
            List<String> expressionList = filterList.stream().map(p -> p.getExpression()).collect(Collectors.toList());

            Map<String, String> extracedValus = eventParser.getExpressionValues(expressionList, fileName, inputStream);
            filterEventList = extracedValus.entrySet().stream().map(p -> new FilterEvent(fileName, filterRepository.findFilterByExpression(p.getKey()), p.getValue())).collect(Collectors.toList());

            filterEventRepository.saveAll(filterEventList);

            log.info("File " + fileName + " parsed successfully...");
        }
        return filterEventList;
    }

    public ArrayNode getEventsBasedOnCriteria() {

        ArrayNode arrayNode = objectMapper.createArrayNode();

        List<FilterCriteriaExecutionGroup> filterCriteriaExecutionGroupList = filterCriteriaExecutionGroupRepository.findAll();
        List<FilterEvent> filterEventRepositoryList = filterEventRepository.findAll();

        // Probably come up with a group by query instead of using filters to enhance performance
        Map<String, List<FilterCriteriaExecutionGroup>> groupsMap = filterCriteriaExecutionGroupList.stream().collect(Collectors.groupingBy(FilterCriteriaExecutionGroup::getGroupName));
        Map<String, List<FilterEvent>> eventsMap = filterEventRepositoryList.stream().collect(Collectors.groupingBy(FilterEvent::getEventName));

        eventsMap.forEach((eventName, filterEvents) -> {
            final AtomicReference<Boolean> criteriaSatisfied = new AtomicReference<>(false);

            List<Filter> filtersOfThisEvent = filterEvents.stream().map(FilterEvent::getFilter).collect(Collectors.toList());

            groupsMap.forEach((groupName, executionGroup) -> {

                List<FilterCriteria> filterCriteriaListOfThisGroup = executionGroup.stream().map(FilterCriteriaExecutionGroup::getFilterCriteria).collect(Collectors.toList());
                List<Filter> filtersOfThisGroup = filterCriteriaListOfThisGroup.stream().map(FilterCriteria::getFilter).collect(Collectors.toList());

                if (filtersOfThisEvent.containsAll(filtersOfThisGroup)) {

                    for (FilterCriteria filterCriteria : filterCriteriaListOfThisGroup) {
                        FilterEvent filterEvent = filterEvents.stream().filter(fltrEvnts -> fltrEvnts.getFilter().equals(filterCriteria.getFilter())).findAny().orElse(null);
                        if (filterEvent != null && filterEvent.getValue().equals(filterCriteria.getValue())) {
                            log.info("Group Name " + groupName + " and criteria name " + filterCriteria.getFilter().getName() + " and criteria value " + filterCriteria.getValue() + " and filter Event value " + filterEvent.getValue());
                            criteriaSatisfied.set(true);
                        } else {
                            log.error("Group Name " + groupName + " and criteria name " + filterCriteria.getFilter().getName() + " and criteria value " + filterCriteria.getValue() + " and filter Event value " + filterEvent.getValue());
                            criteriaSatisfied.set(false);
                            break;
                        }
                    }
                } else {
                    criteriaSatisfied.set(false);
                }

                if (criteriaSatisfied.get()) {
                    ObjectNode objectNode = arrayNode.addObject();
                    objectNode.put(eventName, groupName);
                    filterEvents.forEach(filterEvent -> {
                        objectNode.put(filterEvent.getFilter().getName(), filterEvent.getValue());
                    });
                }

            });
        });

        return arrayNode;
    }

    public ArrayNode getEventBasedOnCriteria(String eventName) throws ResourceNotFoundException {

        ArrayNode arrayNode = objectMapper.createArrayNode();

        List<FilterCriteriaExecutionGroup> filterCriteriaExecutionGroupList = filterCriteriaExecutionGroupRepository.findAll();
        Optional<List<FilterEvent>> filterEventListOptional = filterEventRepository.findFilterEventsByEventName(eventName);

        if (!filterEventListOptional.isPresent() || filterEventListOptional.get().isEmpty()) {
            throw new ResourceNotFoundException("File with name '" + eventName + "' not found in the system");
        }

        List<FilterEvent> filterEvents = filterEventListOptional.get();

        // Probably come up with a group by query instead of using filters to enhance performance
        Map<String, List<FilterCriteriaExecutionGroup>> groupsMap = filterCriteriaExecutionGroupList.stream().collect(Collectors.groupingBy(FilterCriteriaExecutionGroup::getGroupName));

        final AtomicReference<Boolean> criteriaSatisfied = new AtomicReference<>(false);

        List<Filter> filtersOfThisEvent = filterEvents.stream().map(FilterEvent::getFilter).collect(Collectors.toList());

        groupsMap.forEach((groupName, executionGroup) -> {

            List<FilterCriteria> filterCriteriaListOfThisGroup = executionGroup.stream().map(FilterCriteriaExecutionGroup::getFilterCriteria).collect(Collectors.toList());
            List<Filter> filtersOfThisGroup = filterCriteriaListOfThisGroup.stream().map(FilterCriteria::getFilter).collect(Collectors.toList());

            if (filtersOfThisEvent.containsAll(filtersOfThisGroup)) {

                for (FilterCriteria filterCriteria : filterCriteriaListOfThisGroup) {
                    FilterEvent filterEvent = filterEvents.stream().filter(fltrEvnts -> fltrEvnts.getFilter().equals(filterCriteria.getFilter())).findAny().orElse(null);
                    if (filterEvent != null && filterEvent.getValue().equals(filterCriteria.getValue())) {
                        log.info("Group Name " + groupName + " and criteria name " + filterCriteria.getFilter().getName() + " and criteria value " + filterCriteria.getValue() + " and filter Event value " + filterEvent.getValue());
                        criteriaSatisfied.set(true);
                    } else {
                        log.error("Group Name " + groupName + " and criteria name " + filterCriteria.getFilter().getName() + " and criteria value " + filterCriteria.getValue() + " and filter Event value " + filterEvent.getValue());
                        criteriaSatisfied.set(false);
                        break;
                    }
                }
            } else {
                criteriaSatisfied.set(false);
            }

            if (criteriaSatisfied.get()) {
                ObjectNode objectNode = arrayNode.addObject();
//                objectNode.put(eventName, groupName);
                filterEvents.forEach(filterEvent -> {
                    objectNode.put(filterEvent.getFilter().getName(), filterEvent.getValue());
                });
            }

        });

        return arrayNode;
    }
}
