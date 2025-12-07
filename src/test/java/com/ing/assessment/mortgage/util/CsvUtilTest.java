package com.ing.assessment.mortgage.util;

import com.ing.assessment.mortgage.exception.CsvLoadException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilTest {

    @Test
    @DisplayName("init loads mortgage rates from test-mortgage-rate.csv on classpath")
    void initLoadsMortgageRatesWhenCsvPresent() {
        Thread.currentThread().setContextClassLoader(new ClassLoader() {
            @Override
            public InputStream getResourceAsStream(String name) {
                return getClass().getResourceAsStream("/test-mortgage-rate.csv");
            }
        });
        CsvUtil util = new CsvUtil();
        util.init();
        assertNotNull(util.getMortgageRateList());
        assertEquals(4, util.getMortgageRateList().size());
    }

    @Test
    @DisplayName("init throws CsvLoadException when resource is missing")
    void initThrowsWhenResourceMissing() {
        Thread.currentThread().setContextClassLoader(new ClassLoader() {
            @Override
            public InputStream getResourceAsStream(String name) {
                return null;
            }
        });

        CsvUtil util = new CsvUtil();
        assertThrows(CsvLoadException.class, util::init);
    }

    @Test
    @DisplayName("init handles empty CSV")
    void initHandlesEmptyCsv() {
        Thread.currentThread().setContextClassLoader(new ClassLoader() {
            @Override
            public InputStream getResourceAsStream(String name) {
                return getClass().getResourceAsStream("/empty.csv");
            }
        });

        CsvUtil util = new CsvUtil();
        assertDoesNotThrow(util::init);
        assertEquals(0, util.getMortgageRateList().size());
    }
}
