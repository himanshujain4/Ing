package com.ing.assessment.mortgage.controller;

import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.dto.MortgageRate;
import com.ing.assessment.mortgage.service.MortgageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Handles requests related to Mortgage.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Validated
public class MortgageController {
    private final MortgageService mortgageService;

    /**
     * Fetch a list of all interest rates.
     *
     * @return
     */
    @GetMapping("/interest-rates")
    public ResponseEntity<List<MortgageRate>> getInterestRates() {
        log.info("Fetching list of all available interst rates");
        return ResponseEntity.ok(mortgageService.getAllRates());
    }

    /**
     * Checks if a mortgage request is feasible based on income, loan value,
     * home value and maturity period.Calculates monthly costs if feasible.
     *
     * @param request
     * @return
     */
    @PostMapping("/mortgage-check")
    public ResponseEntity<MortgageCheckResponse> mortgageCheck(@Valid @RequestBody MortgageCheckRequest request) {
        log.info("Received request for mortgage check - MaturityPeriod {}, LoanValue {}, HomeValue {}, Income {}",
                request.getMaturityPeriod(), request.getLoanValue(), request.getHomeValue(), request.getIncome());
        return ResponseEntity.ok(mortgageService.checkMortgage(request));
    }
}