package com.vanguard.trade.reporting.engine.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class FilterEvent extends ID {

    // TODO It has to move to separate table to store future events and to make sure that we are not duplicating events otherwise it would override existing events
    @NonNull
    private String eventName;

    @NonNull
    @OneToOne
    private Filter filter;

    @NonNull
    private String value;

}
