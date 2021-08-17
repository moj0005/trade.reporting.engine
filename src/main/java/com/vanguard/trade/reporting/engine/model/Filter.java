package com.vanguard.trade.reporting.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Filter extends ID {

    @NonNull
    private String name;

    @NonNull
    private String expression;

    @JsonIgnore
    @OneToMany
    private List<FilterCriteria> filterCriterias;

    @JsonIgnore
    @OneToMany
    private List<FilterEvent> filterEvents;

}
