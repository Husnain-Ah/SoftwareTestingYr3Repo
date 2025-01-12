package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class GammaTest {

    @DisplayName("Gamma.getGeneration returns 'Gamma'.")
    @Test
    void Gamma_getGeneration_ReturnsGamma() {

        Gamma G = new Gamma(Location.A, 10);

        String Result = G.getGeneration();

        assertEquals("Gamma", Result, "The result was not equal to the expected value.");

    }

    @DisplayName("Gamma.getRates returns (1.5, 2.0, 3.0).")
    @Test
    void Gamma_getRates_ReturnsRates() {

        Gamma G = new Gamma(Location.A, 10);
        List<Double> ExpectedResult = List.of(1.5, 2.0, 3.0);

        List<Double> Result = G.getRates();

        assertEquals(ExpectedResult, Result, "The result was not equal to the expected value.");

    }
}