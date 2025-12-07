package com.ing.assessment.mortgage.repo;

import com.ing.assessment.mortgage.dto.MortgageRate;

import java.util.List;
import java.util.Optional;

/**
 * Provides data access methods to access mortgage rate data.
 */
public interface MortgageRateRepository {
    /**
     * Fetch all available mortgage rates
     *
     * @return
     */
    List<MortgageRate> findAll();

    /**
     * Finds a mortgage rate by the specified maturity period.
     *
     * @param period
     * @return
     */
    Optional<MortgageRate> findByMaturityPeriod(Integer period);
}
