package com.ing.assessment.mortgage.repo;

import com.ing.assessment.mortgage.dto.MortgageRate;
import com.ing.assessment.mortgage.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CsvMortgageRateRepository implements MortgageRateRepository {

    private final CsvUtil csvUtil;

    @Override
    public List<MortgageRate> findAll() {
        return csvUtil.getMortgageRateList();
    }

    @Override
    public Optional<MortgageRate> findByMaturityPeriod(Integer period) {
        return csvUtil.getMortgageRateList().stream()
                .filter(rate -> rate.getMaturityPeriod().equals(period))
                .findFirst();
    }
}
