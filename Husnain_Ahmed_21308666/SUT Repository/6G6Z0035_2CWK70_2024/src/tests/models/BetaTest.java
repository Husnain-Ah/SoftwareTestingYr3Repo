package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class BetaTest {

    @DisplayName("Beta.getGeneration returns 'Beta'.")
    @Test
    void Beta_getGeneration_ReturnsBeta() {

        Beta B = new Beta(Location.A, 10);

        String Result = B.getGeneration();

        assertEquals("Beta", Result);

    }

    @DisplayName("Beta.getRates returns (1.5, 1.5, 1.5).")
    @Test
    void Beta_getRates_ReturnsRates() {

        Beta B = new Beta(Location.A, 10);
        List<Double> ExpectedResult = List.of(1.5, 1.5, 1.5);

        List<Double> Result = B.getRates();

        assertEquals(ExpectedResult, Result, "The result was not equal to the expected value.");

    }
}