package com.vanguard.trade.reporting.engine.config;

import com.vanguard.trade.reporting.engine.model.*;
import com.vanguard.trade.reporting.engine.model.repository.FilterRepository;
import com.vanguard.trade.reporting.engine.model.repository.GenericRepository;
import com.vanguard.trade.reporting.engine.util.EventParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class EventConfig {

    @Bean
    CommandLineRunner setupDB(GenericRepository genericRepository) {
        return args -> {
            List<Filter> filterList = new ArrayList();
            List<FilterCriteria> filterCriteriaList = new ArrayList();
            List<FilterCriteriaExecutionGroup> filterCriteriaExecutionGroupList = new ArrayList();

            Filter filter1 = new Filter("buyer_party", "//buyerPartyReference/@href");

            Filter filter2 = new Filter("seller_party", "//sellerPartyReference/@href");
            FilterCriteria filterCriteria1 = new FilterCriteria(filter2, "EMU_BANK");
            FilterCriteria filterCriteria2 = new FilterCriteria(filter2, "BISON_BANK");

            Filter filter3 = new Filter("premium_amount", "//paymentAmount/amount");

            Filter filter4 = new Filter("premium_currency", "//paymentAmount/currency");
            FilterCriteria filterCriteria3 = new FilterCriteria(filter4, "AUD");
            FilterCriteria filterCriteria4 = new FilterCriteria(filter4, "USD");

            filterList.add(filter1);
            filterList.add(filter2);
            filterList.add(filter3);
            filterList.add(filter4);
            genericRepository.saveAll(filterList);

            filterCriteriaList.add(filterCriteria1);
            filterCriteriaList.add(filterCriteria2);
            filterCriteriaList.add(filterCriteria3);
            filterCriteriaList.add(filterCriteria4);
            genericRepository.saveAll(filterCriteriaList);

            FilterCriteriaExecutionGroup filterCriteriaExecutionGroup1 = new FilterCriteriaExecutionGroup("GROUP1", filterCriteria1, new Date());
            FilterCriteriaExecutionGroup filterCriteriaExecutionGroup2 = new FilterCriteriaExecutionGroup("GROUP1", filterCriteria3, new Date());

            FilterCriteriaExecutionGroup filterCriteriaExecutionGroup3 = new FilterCriteriaExecutionGroup("GROUP2", filterCriteria2, new Date());
            FilterCriteriaExecutionGroup filterCriteriaExecutionGroup4 = new FilterCriteriaExecutionGroup("GROUP2", filterCriteria4, new Date());

            filterCriteriaExecutionGroupList.add(filterCriteriaExecutionGroup1);
            filterCriteriaExecutionGroupList.add(filterCriteriaExecutionGroup2);
            filterCriteriaExecutionGroupList.add(filterCriteriaExecutionGroup3);
            filterCriteriaExecutionGroupList.add(filterCriteriaExecutionGroup4);
            genericRepository.saveAll(filterCriteriaExecutionGroupList);

            log.info(filterCriteriaList.toString(), "Basic DB Setup is Done");
        };
    }

    @Bean
    CommandLineRunner parseEvents(EventParser eventParser, FilterRepository filterRepository, GenericRepository genericRepository) {
        return args -> {

//            List<Filter> filterList = filterRepository.findAll();
//
//            File f = new File("src/main/resources/xml");
//            String[] fileNames = f.list();
//            log.info(Arrays.toString(fileNames));
//            if (filterList != null && !filterList.isEmpty() && fileNames != null && fileNames.length > 0) {
//                List<String> expressionList = filterList.stream().map(p -> p.getExpression()).collect(Collectors.toList());
//                for (String fileName : fileNames) {
//                    Map<String, String> extracedValus = eventParser.getExpressionValues(expressionList, "src/main/resources/xml/" + fileName);
//                    List<FilterEvent> filterEventList = extracedValus.entrySet().stream().map(p -> new FilterEvent(fileName, filterRepository.findFilterByExpression(p.getKey()), p.getValue())).collect(Collectors.toList());
//                    genericRepository.saveAll(filterEventList);
//                }
//                log.info("All Events Parsed");
//            }
        };
    }
}
