package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class SiteTest {

    @ParameterizedTest(name = "Parameterised test for getLocation.")
    @CsvSource({
            "A",
            "B",
            "C"
    })
    @DisplayName("Testing getLocation for all locations.")
    void Site_GetLocation_AllLocations_ReturnsLocation(String input) {

        Location setter = Location.valueOf(input);
        Site site = new Site(setter);

        Location Result = site.getLocation();

        assertEquals(setter, Result, "The get location method did not work successfully.");
    }

}

//@DisplayName("Testing getLocation for Location A")
//@Test
//void Site_GetLocation_ReturnsA() {
//
//    Site site = new Site(Location.A);
//
//    Location Result = site.getLocation();
//
//    assertEquals(Location.A, Result);
//}
//
//@DisplayName("Testing getLocation for Location B")
//@Test
//void Site_GetLocation_ReturnsB() {
//
//    Site site = new Site(Location.B);
//
//    Location Result = site.getLocation();
//
//    assertEquals(Location.B, Result);
//}
//
//@DisplayName("Testing getLocation for Location C")
//@Test
//void Site_GetLocation_ReturnsC() {
//
//    Site site = new Site(Location.C);
//
//    Location Result = site.getLocation();
//
//    assertEquals(Location.C, Result);
//}
