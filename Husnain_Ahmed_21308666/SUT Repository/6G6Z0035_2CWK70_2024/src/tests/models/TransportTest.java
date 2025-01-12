package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class TransportTest {

    @DisplayName("Testing setPaperWaste and getPaperWaste")
    @Test
    void Transport_PaperWaste_ReturnsValue() {

        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(500);
        double Result = transport.getPaperWaste();

        assertEquals(500, Result, "The result was not equal to the expected value.");
    }

    @DisplayName("Testing setPlasticGlassWaste and getPlasticGlassWaste")
    @Test
    void Transport_PlasticGlass_ReturnsValue() {

        Transport transport = new Transport(Location.A, Location.B);
        transport.setPlasticGlassWaste(500);
        double Result = transport.getPlasticGlassWaste();

        assertEquals(500, Result, "The result was not equal to the expected value.");
    }

    @DisplayName("Testing setMetallicWaste and getMetallicWaste")
    @Test
    void Transport_Metallic_ReturnsValue() {

        Transport transport = new Transport(Location.A, Location.B);
        transport.setMetallicWaste(500);
        double Result = transport.getMetallicWaste();

        assertEquals(500, Result, "The result was not equal to the expected value.");
    }

    @DisplayName("Testing getTotalWaste")
    @Test
    void Transport_getTotalWaste_ReturnsValue() {

        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(500);
        transport.setPlasticGlassWaste(500);
        transport.setMetallicWaste(500);
        double Result = transport.getTotalWaste();

        assertEquals(1500, Result, "The result was not equal to the expected value.");
    }

    @ParameterizedTest(name = "Parameterised test for getTravelTime.")
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
    @DisplayName("Testing getTravelTime from all locations")
    void Transport_getTravelTime_AllSites_ReturnsTravelTimes(Double ExpectedTime, String From, String To) {

        Location Start = Location.valueOf(From);
        Location End = Location.valueOf(To);
        Transport transport = new Transport(Start, End);

        double Result = transport.getTravelTime();

        assertEquals(ExpectedTime, Result, "The result was not equal to the expected value.");
    }


    @DisplayName("Testing whether waste types can be set as negative values.")
    @Test
    void Transport_SetWasteTypesAsNegative_DoesNotInitialise() {

        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(-500);
        transport.setPlasticGlassWaste(-500);
        transport.setMetallicWaste(-500);
        double Result = transport.getTotalWaste();

        assertFalse(Result == -1500, "The result was not equal to the expected value.");
    }

}


//    @ParameterizedTest(name = "Parameterised test for getTravelTime 1/4.")
//    @CsvSource({
//            "A,B",
//            "B,A"
//    })
//    @DisplayName("Testing getTravelTime from location A to B and vice versa.")
//    void Transport_getTravelTime_AB_Returns2(Location to, Location from) {
//
//        Transport transport = new Transport(to, from);
//
//        double Result = transport.getTravelTime();
//
//        assertEquals(2.0, Result);
//    }
//
//    @ParameterizedTest(name = "Parameterised test for getTravelTime 2/4.")
//    @CsvSource({
//            "B,C",
//            "C,B" //Defect
//    })
//    @DisplayName("Testing getTravelTime from location B to C and vice versa.")
//    void Transport_getTravelTime_BC_Returns3(Location to, Location from) {
//
//        Transport transport = new Transport(to, from);
//
//        double Result = transport.getTravelTime();
//
//        assertEquals(3.0, Result);
//    }
//
//    @ParameterizedTest(name = "Parameterised test for getTravelTime 3/4.")
//    @CsvSource({
//            "A,C",
//            "C,A"
//    })
//    @DisplayName("Testing getTravelTime from location A to C and vice versa.")
//    void Transport_getTravelTime_AC_Returns4(Location to, Location from) {
//
//        Transport transport = new Transport(to, from);
//
//        double Result = transport.getTravelTime();
//
//        assertEquals(4.0, Result);
//    }
//
//    @ParameterizedTest(name = "Parameterised test for getTravelTime 4/4.")
//    @CsvSource({
//            "A,A",
//            "B,B", //Defect
//            "C,C"
//    })
//    @DisplayName("Testing getTravelTime within the same location.")
//    void Transport_getTravelTime_SameLocation_Returns1(Location to, Location from) {
//
//        Transport transport = new Transport(to, from);
//
//        double Result = transport.getTravelTime();
//
//        assertEquals(1.0, Result);
//    }
