package com.ing.assessment.mortgage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MortgageCheckRequest {
    @NotNull(message = "Income is required")
    @Min(value = 0, message = "Income cannot be negative")
    private BigDecimal income;

    @NotNull(message = "Maturity period is required")
    @Min(value = 1, message = "Maturity period must be at least 1 year")
    private Integer maturityPeriod;

    @NotNull(message = "Loan value is required")
    @Min(value = 0, message = "Loan value cannot be negative")
    private BigDecimal loanValue;

    @NotNull(message = "Home value is required")
    @Min(value = 0, message = "Home value cannot be negative")
    private BigDecimal homeValue;
}