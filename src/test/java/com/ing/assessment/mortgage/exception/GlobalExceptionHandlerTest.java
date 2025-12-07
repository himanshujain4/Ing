package com.ing.assessment.mortgage.exception;

import com.ing.assessment.mortgage.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Test CsvLoadException")
    void csvLoadException() {
        CsvLoadException ex = new CsvLoadException("csv fail", new RuntimeException("io"));
        ResponseEntity<ErrorResponse> resp = handler.handleCsvLoadException(ex, mock(HttpServletRequest.class));
        assertEquals(500, resp.getStatusCodeValue());
        assertEquals("csv fail", resp.getBody().getMessage());
    }

    @Test
    @DisplayName("Test DataNotAvailableException")
    void dataNotAvailable() {
        DataNotAvailableException ex = new DataNotAvailableException("no data");
        ResponseEntity<ErrorResponse> resp = handler.handleDataNotAvailableException(ex, mock(HttpServletRequest.class));
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("no data", resp.getBody().getMessage());
    }

    @Test
    @DisplayName("Test Generic exceptions")
    void genericException() {
        Exception ex = new RuntimeException("uh-oh");
        ResponseEntity<ErrorResponse> resp = handler.handleGenericException(ex, mock(HttpServletRequest.class));
        assertEquals(500, resp.getStatusCodeValue());
        assertEquals("An unexpected error occurred", resp.getBody().getMessage());
    }
}
