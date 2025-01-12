package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class LocationTest {

    @ParameterizedTest(name = "Parameterised test for calculating travel time between all locations.")
    @CsvSource({
            "2.0, A, B",
            "2.0, B, A",
            "3.0, B, C",
            "3.0, C, B",
            "4.0, A, C",
            "4.0, C, A",
            "1.0, A, A",
            "1.0, B, B",
            "1.0, C, C"
    })
    @DisplayName("Testing travelTime between location All location, should return the expected traveltimes.")
    void Location_TravelTime_AllSites_ReturnsTravelTimes(Double ExpectedTime, String From, String To) {

        Location Start = Location.valueOf(From);
        Location End = Location.valueOf(To);

        Double Result = Start.travelTime(End);

        assertEquals(ExpectedTime, Result, "The travelTime was not equal to the expected value.");
    }

}


//    @DisplayName("Testing travelTime between location A and B, should return 2.0")
//    @Test
//    void Location_TravelTime_AB_Returns2() {
//
//        assertEquals(2.0, Location.A.travelTime(Location.B));
//        // Test the same but in reverse
//        assertEquals(2.0, Location.B.travelTime(Location.A));
//    }
//
//    @DisplayName("Testing travelTime between location B and C, should return 3.0")
//    @Test
//    void Location_TravelTime_BC_Returns3() {
//
//        assertEquals(3.0, Location.B.travelTime(Location.C));
//
//        assertEquals(3.0, Location.C.travelTime(Location.B));
//    }
//
//    @DisplayName("Testing travelTime between location A and C, should return 4.0")
//    @Test
//    void Location_TravelTime_AC_Returns4() {
//
//        assertEquals(4.0, Location.A.travelTime(Location.C));
//
//        assertEquals(4.0, Location.C.travelTime(Location.A));
//    }
//
//    @DisplayName("Testing travelTime within the same location, should return 1.0")
//    @Test
//    void Location_TravelTime_SameLocation_Returns1() {
//
//        // Test all 3 locations
//        assertEquals(1.0, Location.A.travelTime(Location.A));
//
//        assertEquals(1.0, Location.B.travelTime(Location.B));
//
//        assertEquals(1.0, Location.C.travelTime(Location.C));
//    }
