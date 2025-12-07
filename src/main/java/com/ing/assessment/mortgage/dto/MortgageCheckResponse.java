package com.ing.assessment.mortgage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MortgageCheckResponse {
    private boolean feasible;
    private BigDecimal monthlyCosts;
}