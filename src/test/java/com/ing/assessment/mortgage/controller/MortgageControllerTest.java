package com.ing.assessment.mortgage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.dto.MortgageRate;
import com.ing.assessment.mortgage.exception.DataNotAvailableException;
import com.ing.assessment.mortgage.exception.GlobalExceptionHandler;
import com.ing.assessment.mortgage.service.MortgageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MortgageControllerTest {

    @Mock
    private MortgageService mortgageService;

    @InjectMocks
    private MortgageController controller;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/interest-rates returns JSON list of interest rates")
    void getInterestRates() throws Exception {
        MortgageRate rate = MortgageRate.builder().interestRate(BigDecimal.TEN).maturityPeriod(1).lastUpdate(LocalDateTime.now()).build();
        when(mortgageService.getAllRates()).thenReturn(List.of(rate));
        ;

        mockMvc.perform(get("/api/interest-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].maturityPeriod").value(1))
                .andExpect(jsonPath("$[0].interestRate").value(10))
                .andExpect(jsonPath("$[0].lastUpdate").exists());
        ;
    }

    @Test
    @DisplayName("GET /api/interest-rates  returns empty list")
    void getInterestRatesReturnsEmpty() throws Exception {
        when(mortgageService.getAllRates()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/interest-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when mortgage is feasible")
    void testMortgageCheck() throws Exception {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(BigDecimal.valueOf(60000));
        request.setMaturityPeriod(15);
        request.setLoanValue(BigDecimal.valueOf(150000));
        request.setHomeValue(BigDecimal.valueOf(200000));
        MortgageCheckResponse response = new MortgageCheckResponse(true, BigDecimal.valueOf(1111.55));
        when(mortgageService.checkMortgage(any(MortgageCheckRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(true))
                .andExpect(jsonPath("$.monthlyCosts").value(1111.55));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when mortgage is not feasible")
    void testMortgageCheckNotFeasible() throws Exception {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(BigDecimal.valueOf(60000));
        request.setMaturityPeriod(15);
        request.setLoanValue(BigDecimal.valueOf(150000));
        request.setHomeValue(BigDecimal.valueOf(200000));
        MortgageCheckResponse response = new MortgageCheckResponse(false, BigDecimal.valueOf(0));
        when(mortgageService.checkMortgage(any(MortgageCheckRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(false))
                .andExpect(jsonPath("$.monthlyCosts").value(0));
    }


    @Test
    @DisplayName("POST /api/mortgage-check handles service exception -> 404")
    void mortgageCheckThrows() throws Exception {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(BigDecimal.valueOf(60000));
        request.setMaturityPeriod(15);
        request.setLoanValue(BigDecimal.valueOf(150000));
        request.setHomeValue(BigDecimal.valueOf(200000));
        when(mortgageService.checkMortgage(any(MortgageCheckRequest.class))).
                thenThrow(new DataNotAvailableException("No data for MaturityPeriod"));

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("No data")))
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when Income is null")
    void testMortgageCheckInvalidInput() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest();
        invalidRequest.setIncome(null);
        invalidRequest.setMaturityPeriod(1);
        invalidRequest.setLoanValue(BigDecimal.ZERO);
        invalidRequest.setHomeValue(BigDecimal.ZERO);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Income is required")));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when Income is negative")
    void testMortgageCheckInvalidInput1() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest();
        invalidRequest.setIncome(BigDecimal.valueOf(-1));
        invalidRequest.setMaturityPeriod(1);
        invalidRequest.setLoanValue(BigDecimal.ZERO);
        invalidRequest.setHomeValue(BigDecimal.ZERO);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Income cannot be negative")));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when MaturityPeriod is null")
    void testMortgageCheckInvalidInput2() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest();
        invalidRequest.setIncome(BigDecimal.valueOf(1));
        invalidRequest.setMaturityPeriod(null);
        invalidRequest.setLoanValue(BigDecimal.ZERO);
        invalidRequest.setHomeValue(BigDecimal.ZERO);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Maturity period is required")));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when MaturityPeriod is 0")
    void testMortgageCheckInvalidInput3() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest();
        invalidRequest.setIncome(BigDecimal.valueOf(1));
        invalidRequest.setMaturityPeriod(0);
        invalidRequest.setLoanValue(BigDecimal.ZERO);
        invalidRequest.setHomeValue(BigDecimal.ZERO);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Maturity period must be at least 1 year")));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when Loan Value is null")
    void testMortgageCheckInvalidInput4() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest();
        invalidRequest.setIncome(BigDecimal.valueOf(1));
        invalidRequest.setMaturityPeriod(1);
        invalidRequest.setLoanValue(null);
        invalidRequest.setHomeValue(BigDecimal.ZERO);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Loan value is required")));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when Loan Value is negative")
    void testMortgageCheckInvalidInput5() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest();
        invalidRequest.setIncome(BigDecimal.valueOf(1));
        invalidRequest.setMaturityPeriod(1);
        invalidRequest.setLoanValue(BigDecimal.valueOf(-1));
        invalidRequest.setHomeValue(BigDecimal.ZERO);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Loan value cannot be negative")));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when Home Value is null")
    void testMortgageCheckInvalidInput6() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest();
        invalidRequest.setIncome(BigDecimal.valueOf(1));
        invalidRequest.setMaturityPeriod(1);
        invalidRequest.setLoanValue(BigDecimal.valueOf(1));
        invalidRequest.setHomeValue(null);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Home value is required")));
    }

    @Test
    @DisplayName("POST /api/mortgage-check when Home Value is negative")
    void testMortgageCheckInvalidInput7() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest();
        invalidRequest.setIncome(BigDecimal.valueOf(1));
        invalidRequest.setMaturityPeriod(1);
        invalidRequest.setLoanValue(BigDecimal.valueOf(1));
        invalidRequest.setHomeValue(BigDecimal.valueOf(-1));

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Home value cannot be negative")));
    }


}
