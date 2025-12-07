package com.ing.assessment.mortgage.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.ing.assessment.mortgage.util.MortgageConstants.*;

/**
 * Utility class to calculate monthly mortgage payments using the standard mortgage formula available on wikipedia:
 * M = P [ i(1 + i)^n ] / [ (1 + i)^n – 1 ]
 */
@Component
public class MonthlyPaymentCalculationUtil {
    /**
     * Calculates the monthly payment for a given loan amount, annual interest rate, and loan term.
     *
     * @param principal
     * @param annualRatePercent
     * @param years
     * @return
     */
    public BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRatePercent, int years) {
        if (principal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        int totalMonths = years * 12;
        if (annualRatePercent.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(totalMonths), 2, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = annualRatePercent
                .divide(PERCENT_DIVISOR, MATH_CONTEXT)
                .divide(MONTHS_IN_YEAR, MATH_CONTEXT);

//        M = P [ i(1 + i)^n ] / [ (1 + i)^n – 1 ]

//         (1 + i)^n
        BigDecimal multiplier = monthlyRate.add(BigDecimal.ONE).pow(totalMonths, MATH_CONTEXT);

//         Numerator: i * (1 + i)^n
        BigDecimal numerator = monthlyRate.multiply(multiplier, MATH_CONTEXT);

//         Denominator: (1 + i)^n - 1
        BigDecimal denominator = multiplier.subtract(BigDecimal.ONE, MATH_CONTEXT);

//         M = P * (Numerator / Denominator)
        return principal
                .multiply(numerator.divide(denominator, MATH_CONTEXT))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
