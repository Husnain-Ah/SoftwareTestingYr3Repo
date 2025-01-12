import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class ScenarioConfigurationTest {

    private Historic historic;
    private ScenarioConfiguration scenario;

    @BeforeEach
    void setup(){
        historic = null;
        scenario = null;
    }

    static class TestRecycling extends Recycling {
        private final String generation;
        private final List<Double> rates;

        protected TestRecycling(Location location, int yearsActive, String generation, List<Double> rates) {
            super(location, yearsActive);
            this.generation = generation;
            this.rates = rates;
        }

        @Override
        public String getGeneration() {
            return generation;
        }

        @Override
        public List<Double> getRates() {
            return rates;
        }
    }

    @DisplayName("Testing getHistoric.")
    @Test
    void ScenarioConfiguration_getHistoric_ReturnsHistoric() {

        historic = new Historic(Location.A, 1000);

        List<Recycling> recycling  = new ArrayList<>(Arrays.asList());

        scenario = new ScenarioConfiguration(historic, recycling);

        Historic Result = scenario.getHistoric();

        assertEquals(historic, Result, "The result was not equal to the expected value that was set previously.");
    }

    @DisplayName("Testing setHistoric by overwriting a previously passed in historic site with a new one.")
    @Test
    void ScenarioConfiguration_OverWrittenHistoric_ReturnsNewHistoric() {

        historic = new Historic(Location.A, 1000);

        List<Recycling> recycling  = new ArrayList<>(Arrays.asList());

        scenario = new ScenarioConfiguration(historic, recycling);
        Historic historic2 = new Historic(Location.B, 500);
        scenario.setHistoric(historic2);

        Historic Result = scenario.getHistoric();

        assertEquals(historic2, Result, "The result was not overwritten properly.");
    }

    @DisplayName("Testing addRecycling and getRecycling.")
    @Test
    void ScenarioConfiguration_GetRecycling_ReturnsNewRecycling() {

        historic = new Historic(Location.A, 1000);

        List<Recycling> recycling  = new ArrayList<>(Arrays.asList());

        scenario = new ScenarioConfiguration(historic, recycling);
        Recycling rec1 = new TestRecycling(Location.A, 10, "Alpha", List.of(1.0, 1.0, 1.0));
        scenario.addRecycling(rec1);

        List<Recycling> Result = scenario.getRecycling();

        assertTrue(Result.contains(rec1), "The result was not equal to the expected value that was added previously.");
    }

    @DisplayName("Testing whether a scenarioConfiguration class that has taken no parameters contains an empty array list.")
    @Test
    void ScenarioConfiguration_NoParameters_ReturnsEmptyArray() {

        scenario = new ScenarioConfiguration();

        List<Recycling> Result = scenario.getRecycling();

        assertTrue(Result.isEmpty(), "The array list is not empty.");
    }


    @DisplayName("Testing whether addRecycling can be called multiple times.")
    @Test
    void ScenarioConfiguration_AddRecyclingMultipleTimes_AddsCorrectly() {

        scenario = new ScenarioConfiguration();
        Recycling rec1 = new TestRecycling(Location.A, 10, "Alpha", List.of(1.0, 1.0, 1.0));

        scenario.addRecycling(rec1); scenario.addRecycling(rec1); scenario.addRecycling(rec1);

        List<Recycling> Result = scenario.getRecycling();

        assertEquals(3, Result.size(), "The recycling centre has not been added 3 times as intended.");
    }

}


