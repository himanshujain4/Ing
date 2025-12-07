package com.ing.assessment.mortgage.service;

import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.dto.MortgageRate;

import java.util.List;

/**
 * Service class to manage mortgage-related operations
 */
public interface MortgageService {

    /**
     * Fetch all available mortgage interest rates.
     *
     * @return
     */
    List<MortgageRate> getAllRates();

    /**
     * Checks whether a mortgage request is feasible and calculates monthly costs.
     *
     * @param request
     * @return
     */
    MortgageCheckResponse checkMortgage(MortgageCheckRequest request);

    ;
}