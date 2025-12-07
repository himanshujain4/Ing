package com.ing.assessment.mortgage.repo;

import com.ing.assessment.mortgage.dto.MortgageRate;
import com.ing.assessment.mortgage.util.CsvUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvMortgageRateRepositoryTest {

    @Mock
    private CsvUtil csvUtil;

    @InjectMocks
    private CsvMortgageRateRepository csvMortgageRateRepository;


    @Test
    @DisplayName("Test findAll method happy path")
    void testFindAll() {
        List<MortgageRate> rates = List.of(new MortgageRate(10, BigDecimal.TEN, LocalDateTime.now()));
        when(csvUtil.getMortgageRateList()).thenReturn(rates);

        assertEquals(1, csvMortgageRateRepository.findAll().size());
    }

    @Test
    @DisplayName("Test findByMaturityPeriod when MaturityPeriod is present")
    void testFindByMaturityPeriod() {
        List<MortgageRate> rates = List.of(new MortgageRate(10, BigDecimal.TEN, LocalDateTime.now()));
        when(csvUtil.getMortgageRateList()).thenReturn(rates);

        Optional<MortgageRate> rate = csvMortgageRateRepository.findByMaturityPeriod(10);

        assertThat(rate).isPresent();
    }

    @Test
    @DisplayName("Test findByMaturityPeriod when MaturityPeriod is not present")
    void testFindByMaturityPeriodWhenNotPresent() {
        when(csvUtil.getMortgageRateList()).thenReturn(new ArrayList<>());

        Optional<MortgageRate> rate = csvMortgageRateRepository.findByMaturityPeriod(10);

        assertThat(rate).isNotPresent();
    }
}
