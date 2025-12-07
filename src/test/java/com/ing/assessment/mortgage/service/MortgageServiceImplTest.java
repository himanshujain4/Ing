package com.ing.assessment.mortgage.service;

import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.dto.MortgageRate;
import com.ing.assessment.mortgage.exception.DataNotAvailableException;
import com.ing.assessment.mortgage.repo.MortgageRateRepository;
import com.ing.assessment.mortgage.util.MonthlyPaymentCalculationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MortgageServiceImplTest {

    @Mock
    private MortgageRateRepository mortgageRateRepository;

    @Mock
    private MonthlyPaymentCalculationUtil monthlyPaymentCalculationUtil;

    @InjectMocks
    private MortgageServiceImpl mortgageService;

    @Test
    @DisplayName("Test getAllRates")
    void testGetAllRates() {
        MortgageRate rate = MortgageRate.builder().interestRate(BigDecimal.TEN).maturityPeriod(1).build();
        when(mortgageRateRepository.findAll()).thenReturn(List.of(rate));

        List<MortgageRate> rates = mortgageService.getAllRates();

        assertThat(rates).contains(rate);
    }

    @Test
    void testCheckMortgage_Feasible() {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(BigDecimal.valueOf(50000));
        request.setMaturityPeriod(10);
        request.setLoanValue(BigDecimal.valueOf(100000));
        request.setHomeValue(BigDecimal.valueOf(120000));

        MortgageRate rate = MortgageRate.builder().interestRate(BigDecimal.TEN).maturityPeriod(1).build();

        when(mortgageRateRepository.findByMaturityPeriod(10)).thenReturn(Optional.of(rate));
        when(monthlyPaymentCalculationUtil.calculateMonthlyPayment(
                any(BigDecimal.class), any(BigDecimal.class), anyInt()))
                .thenReturn(BigDecimal.valueOf(800));

        MortgageCheckResponse response = mortgageService.checkMortgage(request);

        assertTrue(response.isFeasible());
        assertEquals(BigDecimal.valueOf(800), response.getMonthlyCosts());
    }

    @Test
    void testCheckMortgage_NotFeasibleByIncome() {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(BigDecimal.valueOf(10000));
        request.setMaturityPeriod(10);
        request.setLoanValue(BigDecimal.valueOf(100000));
        request.setHomeValue(BigDecimal.valueOf(120000));

        MortgageCheckResponse response = mortgageService.checkMortgage(request);

        assertFalse(response.isFeasible());
    }

    @Test
    void testCheckMortgage_NotFeasibleByHomeValue() {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(BigDecimal.valueOf(50000));
        request.setMaturityPeriod(10);
        request.setLoanValue(BigDecimal.valueOf(150000));
        request.setHomeValue(BigDecimal.valueOf(100000));

        MortgageCheckResponse response = mortgageService.checkMortgage(request);

        assertFalse(response.isFeasible());
    }

    @Test
    void testCheckMortgage_RateNotFound() {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(BigDecimal.valueOf(50000));
        request.setMaturityPeriod(5);
        request.setLoanValue(BigDecimal.valueOf(100000));
        request.setHomeValue(BigDecimal.valueOf(120000));

        when(mortgageRateRepository.findByMaturityPeriod(5)).thenReturn(Optional.empty());

        assertThrows(DataNotAvailableException.class, () -> mortgageService.checkMortgage(request));
    }
}
