package com.ing.assessment.mortgage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MortgageRate {
    private Integer maturityPeriod;
    private BigDecimal interestRate;
    private LocalDateTime lastUpdate;
}