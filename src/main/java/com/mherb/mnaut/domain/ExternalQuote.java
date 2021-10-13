package com.mherb.mnaut.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalQuote {

    private String symbol;
    private BigDecimal lastPrice;
    private BigDecimal volume;
}
