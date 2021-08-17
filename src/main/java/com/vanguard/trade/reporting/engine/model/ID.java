package com.vanguard.trade.reporting.engine.model;

import lombok.*;

import javax.persistence.*;

@MappedSuperclass
public class ID {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE
    )

    public Long id;


    // public Date creationDate;
    // public Date lastModifiedDate;

}
