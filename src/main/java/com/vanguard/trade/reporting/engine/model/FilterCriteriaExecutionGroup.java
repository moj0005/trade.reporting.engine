package com.vanguard.trade.reporting.engine.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class FilterCriteriaExecutionGroup extends ID {

    @NonNull
    private String groupName;

    @NonNull
    @OneToOne
    private FilterCriteria filterCriteria;

    @NonNull
    private Date applyDate;

}
