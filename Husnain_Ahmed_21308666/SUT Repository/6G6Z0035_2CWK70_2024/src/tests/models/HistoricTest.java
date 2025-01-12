package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class HistoricTest {

    private Historic historic;

    @BeforeEach
    void setup(){
         historic = null;
    }


    @DisplayName("Testing setRemainingWaste and getRemainingWaste")
    @Test
    void Historic_RemainingWaste_ReturnsValue() {

        historic = new Historic(Location.A, 1000);
        historic.setRemainingWaste(500);
        double Result = historic.getRemainingWaste();

        assertEquals(500, Result, "The result was not equal to the '500'.");
    }

    @DisplayName("Testing setPlasticGlass and getPlasticGlass")
    @Test
    void Historic_PlasticGlass_ReturnsValue() {

        historic = new Historic(Location.A, 1000);
        historic.setPlasticGlass(500);
        double Result = historic.getPlasticGlass();

        assertEquals(500, Result);
    }

    @DisplayName("Testing setPaper and getPaper")
    @Test
    void Historic_Paper_ReturnsValue() {

        historic = new Historic(Location.A, 1000);
        historic.setPaper(500);
        double Result = historic.getPaper();

        assertEquals(500, Result, "The result was not equal to the '500'.");
    }

    @DisplayName("Testing setMetallic and getMetallic")
    @Test
    void Historic_Metallic_ReturnsValue() {

        historic = new Historic(Location.A, 1000);
        historic.setMetallic(500);
        double Result = historic.getMetallic();

        assertEquals(500, Result, "The result was not equal to the '500'.");
    }

    @ParameterizedTest(name = "Parameterised test for waste split.")
    @CsvSource({
            "1000",
            "1250",
            "2000",
            "0"
    })
    @DisplayName("Testing EstimateWasteSplit for multiple values. Below threshold, at threshold, over threshold and 0")
    void History_EstimateWasteSplit_ReturnsSplit(double initialWaste) {

        historic = new Historic(Location.A, initialWaste);


        double paper = historic.getPaper();
        double plasticGlass = historic.getPlasticGlass();
        double metallic = historic.getMetallic();

        double ExpectedPaper = initialWaste * 0.5 ; //Paper waste should be 50% of the initial waste
        double ExpectedplasticGlass;
        double Expectedmetallic;

        if (initialWaste > 1250) { // Metallic threshold is 1250
            ExpectedplasticGlass = initialWaste * 0.3; // Plastic/Glass waste should be 30% of the initial waste
            Expectedmetallic = initialWaste * 0.2; // Metallic waste should be 20% of the initial waste
        } else {
            ExpectedplasticGlass = initialWaste * 0.5; // Plastic/Glass waste should be 50% of the initial waste
            Expectedmetallic = 0; // Metallic waste should be 0% of the initial waste
        }

        assertEquals(ExpectedPaper, paper, "The value of paper waste was not equal to the expected value.");
        assertEquals(ExpectedplasticGlass, plasticGlass, "The value of plasticGlass waste was not equal to the expected value.");
        assertEquals(Expectedmetallic, metallic, "The value of metallic waste was not equal to the expected value.");
    }

    @DisplayName("Testing whether waste can be set as negative values.")
    @Test
    void Historic_SettingValuesToNegative_DoesNotInitialise() {

        historic = new Historic(Location.A, -500);
        historic.setMetallic(-500);
        double Result = historic.getMetallic();


        historic.setRemainingWaste(-500);
        double Result2 = historic.getRemainingWaste();


        historic.setPlasticGlass(-500);
        double Result3 = historic.getPlasticGlass();


        historic.setPaper(-500);
        double Result4 = historic.getPaper();


        assertFalse(Result == -500 && Result2 == -500 && Result3 == -500 && Result4 == -500 , "The waste values have been set to a negative value.");
    }

}
