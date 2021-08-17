package com.vanguard.trade.reporting.engine.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class was initially designed but not used because of dynamicity issues
 *
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EventResponse {

    @JsonProperty("buyer_party")
    private String buyerParty;

    @JsonProperty("seller_party")
    private String sellerParty;

    @JsonProperty("premium_amount")
    private Double premiumAmount;

    @JsonProperty("premium_currency")
    private String premiumCurrency;
}
