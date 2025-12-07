package com.ing.assessment.mortgage.util;

import com.ing.assessment.mortgage.dto.MortgageRate;
import com.ing.assessment.mortgage.exception.CsvLoadException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to load mortgage rate data from a CSV file mortgage-rates.csv.
 */
@Component
@Slf4j
public class CsvUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private List<MortgageRate> mortgageRateList = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.mortgageRateList = Collections.unmodifiableList(loadMortgageRateData());
        log.info("Loaded {} Mortgage Rates from CSV", mortgageRateList.size());
    }

    private List<MortgageRate> loadMortgageRateData() {
        List<MortgageRate> interestRatesList = new ArrayList<>();

        try (InputStream is = new ClassPathResource("mortgage-rates.csv").getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(reader, CSVFormat.Builder.create()
                     .setHeader("Maturity Period", "Interest Rate", "Last Update")
                     .setSkipHeaderRecord(true)
                     .setIgnoreSurroundingSpaces(true)
                     .build())) {

            parser.stream()
                    .forEach(record -> {
                        try {
                            Integer maturityPeriod = Integer.parseInt(record.get("Maturity Period").trim());
                            BigDecimal interestRate = new BigDecimal(record.get("Interest Rate").trim());
                            LocalDateTime localDateTime = LocalDateTime.parse(record.get("Last Update").trim(), DATE_FORMATTER);

                            interestRatesList.add(MortgageRate.builder().
                                    interestRate(interestRate).
                                    maturityPeriod(maturityPeriod).
                                    lastUpdate(localDateTime).
                                    build());
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Error parsing CSV row: " + String.join(",", record), e);
                        }
                    });

        } catch (IOException e) {
            log.error("Error loading Mortgage Rate CSV", e);
            throw new CsvLoadException("Failed to load Mortgage Rate data", e);
        }

        return interestRatesList;
    }

    /**
     * Returns the list of mortgage rates.
     *
     * @return
     */
    public List<MortgageRate> getMortgageRateList() {
        return mortgageRateList;
    }


}