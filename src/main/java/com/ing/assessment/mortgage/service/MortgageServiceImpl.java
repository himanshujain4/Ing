package com.ing.assessment.mortgage.service;

import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.dto.MortgageRate;
import com.ing.assessment.mortgage.exception.DataNotAvailableException;
import com.ing.assessment.mortgage.repo.MortgageRateRepository;
import com.ing.assessment.mortgage.util.MonthlyPaymentCalculationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.ing.assessment.mortgage.util.MortgageConstants.MAX_INCOME_MULTIPLIER;

@Service
@RequiredArgsConstructor
@Slf4j
public class MortgageServiceImpl implements MortgageService {
    private final MortgageRateRepository mortgageRateRepository;
    private final MonthlyPaymentCalculationUtil monthlyPaymentCalculationUtil;


    public List<MortgageRate> getAllRates() {
        return mortgageRateRepository.findAll();
    }

    public MortgageCheckResponse checkMortgage(MortgageCheckRequest request) {
        boolean isIncomeFeasible = request.getLoanValue()
                .compareTo(request.getIncome().multiply(MAX_INCOME_MULTIPLIER)) <= 0;

        boolean isValueFeasible = request.getLoanValue()
                .compareTo(request.getHomeValue()) <= 0;

        if (!isIncomeFeasible || !isValueFeasible) {
            return new MortgageCheckResponse(false, BigDecimal.ZERO);
        }
        MortgageRate rate = mortgageRateRepository.findByMaturityPeriod(request.getMaturityPeriod())
                .orElseThrow(() -> new DataNotAvailableException("No interest rate found for maturity period: " + request.getMaturityPeriod()));

        BigDecimal monthlyCost = monthlyPaymentCalculationUtil.calculateMonthlyPayment(
                request.getLoanValue(),
                rate.getInterestRate(),
                request.getMaturityPeriod()
        );

        return new MortgageCheckResponse(true, monthlyCost);
    }

}