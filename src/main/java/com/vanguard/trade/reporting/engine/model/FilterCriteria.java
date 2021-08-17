package com.vanguard.trade.reporting.engine.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Table
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class FilterCriteria extends ID {

    @NonNull
    @OneToOne
    private Filter filter;

    @NonNull
    private String value;

    @OneToMany
    private List<FilterCriteriaExecutionGroup> filterCriteriaExecutionGroups;

}
