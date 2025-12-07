package com.ing.assessment.mortgage.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlyPaymentCalculationUtilTest {

    @Test
    @DisplayName("Test calculate monthly payment with Interest")
    void testCalculateMonthlyPaymentWithInterest() {
        MonthlyPaymentCalculationUtil monthlyPaymentCalculationUtil = new MonthlyPaymentCalculationUtil();
        BigDecimal payment = monthlyPaymentCalculationUtil.calculateMonthlyPayment(BigDecimal.valueOf(10000), BigDecimal.valueOf(5), 10);
        assertEquals(BigDecimal.valueOf(106.07).setScale(2), payment);
    }

    @Test
    @DisplayName("Test calculate monthly payment with Zero Interest")
    void testCalculateMonthlyPaymentZeroInterest() {
        MonthlyPaymentCalculationUtil monthlyPaymentCalculationUtil = new MonthlyPaymentCalculationUtil();
        BigDecimal payment = monthlyPaymentCalculationUtil.calculateMonthlyPayment(BigDecimal.valueOf(12000), BigDecimal.ZERO, 1);
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), payment);
    }

    @Test
    @DisplayName("Test calculate monthly payment with Zero Principle")
    void testCalculateMonthlyPaymentZeroPrincipal() {
        MonthlyPaymentCalculationUtil monthlyPaymentCalculationUtil = new MonthlyPaymentCalculationUtil();
        BigDecimal payment = monthlyPaymentCalculationUtil.calculateMonthlyPayment(BigDecimal.ZERO, BigDecimal.valueOf(5), 10);
        assertEquals(BigDecimal.ZERO, payment);
    }
}
