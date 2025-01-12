package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class AlphaTest {

    @DisplayName("Alpha.getGeneration returns 'Alpha'.")
    @Test
    void Alpha_getGeneration_ReturnsAlpha() {

        Alpha A = new Alpha(Location.A, 10);

        String Result = A.getGeneration();

        assertEquals("Alpha", Result, "The result was not equal to the expected value.");

    }

    @DisplayName("Alpha.getRates returns (1.0, 1.0, 1.0).")
    @Test
    void Alpha_getRates_ReturnsRates() {

        Alpha A = new Alpha(Location.A, 10);
        List<Double> ExpectedResult = List.of(1.0, 1.0, 1.0);

        List<Double> Result = A.getRates();

        assertEquals(ExpectedResult, Result, "The result was not equal to the expected value.");

    }
}