package com.ing.assessment.mortgage.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class MortgageConstants {

    public static final BigDecimal MAX_INCOME_MULTIPLIER = BigDecimal.valueOf(4);
    public static final BigDecimal PERCENT_DIVISOR = BigDecimal.valueOf(100);
    public static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    public static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);
}
