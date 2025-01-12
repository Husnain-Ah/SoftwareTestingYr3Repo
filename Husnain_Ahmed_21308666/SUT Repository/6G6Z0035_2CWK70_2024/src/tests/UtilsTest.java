import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// All tests written by Husnain Ahmed (21308666@stu.mmu.ac.uk)

class UtilsTest {

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

    private Historic historic;
    private List<Recycling> candidateCentres = Collections.emptyList();

    private Recycling centre1;
    private Recycling centre2;
    private Recycling centre3;

    @BeforeEach
    void setup(){
        historic = null;
        candidateCentres = null;

        centre1 = new TestRecycling(Location.B, 5, "Alpha", List.of(1.0, 1.0, 1.0));
        centre2 = new TestRecycling(Location.B, 10, "Beta", List.of(1.5, 1.5, 1.5));
        centre3 = new TestRecycling(Location.C, 15, "Gamma", List.of(1.5, 2.0, 3.0));
    }

    //<editor-fold desc="findViableCentres Tests">

        @DisplayName("Finding the possible viable centres when metallic waste is not present, making sure that any 'Gamma' generation centres have been removed.")
        @Test
        void Utils_findViableCentres_CheckForMetallicWaste_HasNoMetallicWaste_ReturnsNoGamma() {

            historic = new Historic(Location.A, 10);
            historic.setMetallic(0);

            Alpha A = new Alpha(Location.A, 10);
            Gamma G = new Gamma(Location.B, 5);
            candidateCentres  = new ArrayList<>(Arrays.asList(
                    A,
                    G)
            );

            List<Recycling> Result = Utils.findViableCentres(historic, candidateCentres);

            assertEquals(1, Result.size(), "The size of the array list is not 1.");
            assertFalse(Result.contains(G), "The array list contains a gamma site.");

        }

        @DisplayName("Finding the possible viable centres when metallic waste is present, making sure that any 'Gamma' generation centres have not been removed.")
        @Test
        void Utils_findViableCentres_CheckForMetallicWaste_HasMetallicWaste_ReturnsGamma() {

            historic = new Historic(Location.A, 10);
            historic.setMetallic(5);

            Alpha A = new Alpha(Location.A, 10);
            Gamma G = new Gamma(Location.B, 5);
            candidateCentres  = new ArrayList<>(Arrays.asList(
                    A,
                    G)
            );

            List<Recycling> Result = Utils.findViableCentres(historic, candidateCentres);

            assertEquals(2, Result.size(), "The size of the array list is not 2.");
            assertTrue((Result.contains(G)), "The array list does not contain a gamma site.");
            assertTrue(Result.containsAll(candidateCentres), "The array list does not contain the candidate list.");

        }

        @ParameterizedTest(name = "Parameterised test for travel times.")
        @CsvSource({
                "A,B",
                "B,A",
                "B,C",
                "C,B",
                "A,C",
                "C,A"
        })
        @DisplayName("Finding the possible viable centres where travel time between locations is less than 3.")
        void Utils_findViableCentres_TravelTimeIsLessThan3_ReturnsTrue(Location to, Location from) {

            historic = new Historic(to, 10);

            Alpha A = new Alpha(from, 10);
            candidateCentres  = new ArrayList<>(Arrays.asList(
                    A)
            );

            double travelTime = new Transport(to, from).getTravelTime();

            List<Recycling> Result = Utils.findViableCentres(historic, new ArrayList<>(candidateCentres));

            if(travelTime > 3.0){
                assertTrue(Result.isEmpty(), "There are centres that are further than 3 hours away.=");
            } else {
                assertFalse(Result.isEmpty(), "There are no available centres.");
            }
        }

    //<editor-fold

    //<editor-fold desc="FindNearestCentres Tests">

        @DisplayName("Finding the single nearest centre from a list of 2 centres with differing travel times.")
        @Test
        void Utils_FindNearestCentres_GetSingleNearestCentre_ReturnsOneNearestCentre() {

            historic = new Historic(Location.A, 10);
//            Recycling centre1 = new TestRecycling(Location.B, 10, "Alpha", List.of(1.0, 1.0, 1.0)); // Travel time is 2
//            Recycling centre2 = new TestRecycling(Location.C, 10, "Beta", List.of(1.5, 1.5, 1.5)); // Travel time is 4
            candidateCentres = List.of(centre1, centre3);

            List<Recycling> nearestCentres =  Utils.findNearestCentres(historic, candidateCentres);

            assertEquals(1, nearestCentres.size(), "The size of the array list is not 1.");
            assertTrue(nearestCentres.contains(centre1), "The array list does not contain centre1.");
        }

        @DisplayName("Finding the 2 nearest centre from a list of 3 centres with 2 identical travel times and 1 longer travel time.")
        @Test
        void Utils_FindNearestCentres_GetMultipleNearestCentres_ReturnsTwoClosestCentres() {

            historic = new Historic(Location.A, 10);

//            Recycling centre1 = new TestRecycling(Location.B, 5, "Alpha", List.of(1.0, 1.0, 1.0));
//            Recycling centre2 = new TestRecycling(Location.B, 5, "Beta", List.of(1.5, 1.5, 1.5));
//            Recycling centre3 = new TestRecycling(Location.C, 5, "Gamma", List.of(1.5, 2.0, 3.0));
            candidateCentres = List.of(centre1, centre2, centre3);

            List<Recycling> nearestCentres = Utils.findNearestCentres(historic, candidateCentres);

            assertEquals(2, nearestCentres.size(), "The size of the array list is not 2.");
            assertTrue(nearestCentres.contains(centre1)&& nearestCentres.contains(centre2), "The array list does not contain centre1 and centre2.");
        }

        @DisplayName("Checking if 'findNearestCentres' throws an exception when no centres are passed in as parameters.")
        @Test
        void Utils_FindNearestCentres_NoCandidatesProvided_DoesNotThrowException() {

            historic = new Historic(Location.A, 10);
            candidateCentres = new ArrayList<>();

            assertDoesNotThrow(() -> {
                Utils.findNearestCentres(historic, candidateCentres);
            }, "An exception was thrown.");

//            assertThrows(RuntimeException.class, () -> {
//                Utils.findNearestCentres(historic, candidateCentres);
//            });
        }

        @DisplayName("Testing if 'findNearestCentres' returns all passed in centres when the travel time is equal for all of them. ")
        @Test
        void Utils_FindNearestCentres_GetAllCentresWhenTravelTimeIsEqual_ReturnsAllCentres() {

            historic = new Historic(Location.A, 10);
//            Recycling centre1 = new TestRecycling(Location.B, 5, "Alpha", List.of(1.0, 1.0, 1.0));
            candidateCentres = List.of(centre1, centre1, centre1);

            List<Recycling> nearestCentres = Utils.findNearestCentres(historic, candidateCentres);

            assertEquals(3, nearestCentres.size(), "The size of the array list is not 3.");
        }

    //</editor-fold>

    //<editor-fold desc="findLeastYearsActive Tests">

        @DisplayName("Testing if 'findLeastYearsActive' returns an empty list when the candidate centres arraylist is null.")
        @Test
        void Utils_FindLeastYearsActive_NullList_ReturnsEmptyList() {

            List<Recycling> result = Utils.findLeastYearsActive(null);

            assertNotNull(result, "The result is null.");
            assertTrue(result.isEmpty(), "The result is not empty.");
        }

        @DisplayName("Testing if 'findLeastYearsActive' returns an empty list when the candidate centres arraylist is empty.")
        @Test
        void Utils_FindLeastYearsActive_EmptyList_ReturnsEmptyList() {

            candidateCentres = new ArrayList<>(List.of()); //2 different ways of showing an empty list
            List<Recycling> candidateCentres2 = Collections.emptyList(); // this is how the code returns it, i will check both

            List<Recycling> result = Utils.findLeastYearsActive(candidateCentres);
            List<Recycling> result2 = Utils.findLeastYearsActive(candidateCentres2);

            assertNotNull(result, "The result is null.");
            assertTrue(result.isEmpty(), "The result is not empty.");

            assertNotNull(result2, "The result is null.");
            assertTrue(result2.isEmpty(), "The result is not empty.");
        }

        @DisplayName("Testing if 'findLeastYearsActive' returns the same list when a single candidate centre is passed in.")
        @Test
        void Utils_FindLeastYearsActive_SingleCandidate_ReturnsSameList() {

//            Recycling centre1 = new TestRecycling(Location.B, 10, "Alpha", List.of(1.0, 1.0, 1.0));
            candidateCentres = List.of(centre1);

            List<Recycling> result = Utils.findLeastYearsActive(candidateCentres);

            assertEquals(1, result.size(), "The size of the array list is not 1.");
            assertTrue(result.contains(centre1), "The array list does not contain centre1.");
        }

        @DisplayName("Testing if 'findLeastYearsActive' works as intended and returns the centre with the least years active from a list of multiple centres with differing years active values.")
        @Test
        void Utils_FindLeastYearsActive_MultipleCandidates_ReturnsCorrectCentres() {

            candidateCentres = List.of(centre1, centre2, centre3);

            List<Recycling> result = Utils.findLeastYearsActive(candidateCentres);

            assertEquals(1, result.size(), "The size of the array list is not 1.");
            assertTrue(result.contains(centre1), "The array list does not contain centre1.");
        }

        @DisplayName("Testing if 'findLeastYearsActive' returns more than 1 centre when multiple centres have the same amount of years active.")
        @Test
        void Utils_FindLeastYearsActive_MultipleCandidatesSameYearsActive_ReturnsAllMatchingCentres() {

            candidateCentres = List.of(centre1, centre1, centre2);

            List<Recycling> result = Utils.findLeastYearsActive(candidateCentres);

            assertEquals(2, result.size(), "The size of the array list is not 2.");
            assertFalse(result.contains(centre2), "The array list contains centre2.");
        }


    //</editor-fold>

    //<editor-fold desc="findHighestGenerations Tests">

        @DisplayName("Testing if 'findHighestGenerations' returns an empty list when the candidate centres list is null.")
        @Test
        void Utils_FindHighestGenerations_NullList_ReturnsEmptyList() {

            List<Recycling> result = Utils.findHighestGenerations(null);

            assertNotNull(result, "The result is null.");
            assertTrue(result.isEmpty(), "The result is not empty.");
        }

        @DisplayName("Testing if 'findHighestGenerations' Returns an empty list when an empty candidate centres list is passed in.")
        @Test
        void Utils_FindHighestGenerations_EmptyList_ReturnsEmptyList() {

            candidateCentres = Collections.emptyList();

            List<Recycling> result = Utils.findHighestGenerations(candidateCentres);

            assertNotNull(result, "The result is null.");
            assertTrue(result.isEmpty(), "The result is not empty.");
        }

        @DisplayName("Testing if 'findHighestGenerations' returns the same list when a single candidate centre is passed in.")
        @Test
        void Utils_FindHighestGenerations_SingleCandidate_ReturnsSameList() {

            candidateCentres = List.of(centre1);

            List<Recycling> result = Utils.findHighestGenerations(candidateCentres);

            assertEquals(1, result.size(), "The size of the array list is not 1.");
            assertTrue(result.contains(centre1), "The array list does not contain centre1.");
        }

        @DisplayName("Testing if 'findHighestGenerations' returns the both highest generating centers when multiple candidate centres are passed in.")
        @Test
        void Utils_FindHighestGenerations_MultipleSameHighestGeneration_ReturnsAllMatching() {

            candidateCentres = List.of(centre1, centre2, centre2);

            List<Recycling> result = Utils.findHighestGenerations(candidateCentres);

            assertEquals(2, result.size(), "The size of the array list is not 2.");
            assertFalse(result.contains(centre1), "The array list does not contain centre1.");
        }

        @DisplayName("Testing if 'findHighestGenerations' correctly returns the centre with highest generations when multiple different centres are passed in.")
        @Test
        void Utils_FindHighestGenerations_MultipleCandidates_ReturnsHighestGeneration() {

            candidateCentres = List.of(centre1, centre2, centre3);

            List<Recycling> result = Utils.findHighestGenerations(candidateCentres);

            assertEquals(1, result.size(), "The size of the array list is not 1.");
            assertTrue(result.contains(centre3), "The array list does not contain centre3.");
        }

    //</editor-fold>

    //<editor-fold desc="findOptimalCentre Tests">

        @DisplayName("Testing if 'findOptimalCentre' throws an exception when no candidate centres are provided.")
        @Test
        void Utils_FindOptimalCentre_NoCandidatesProvided_DoesNotThrowException() {

            historic = new Historic(Location.A, 10);
            candidateCentres = Collections.emptyList();

            assertDoesNotThrow(() -> {
                Utils.findOptimalCentre(historic, candidateCentres);
            }, "An exception was thrown.");

        }

        @DisplayName("Testing if 'findOptimalCentre' returns the same list when a single candidate centre is passed in.")
        @Test
        void Utils_FindOptimalCentre_SingleCandidate_ReturnsCandidate() {

            historic = new Historic(Location.A, 10);
            List<Recycling> candidateCentres = List.of(centre1);

            Recycling result = Utils.findOptimalCentre(historic, candidateCentres);

            assertEquals(centre1, result, "Centre1 was not returned.");
        }

        @DisplayName("Testing if 'findOptimalCentre' returns the correct centre when multiple centres are passed in.")
        @Test
        void Utils_FindOptimalCentre_MultipleCandidates_ReturnsOptimalCentre() {

            historic = new Historic(Location.A, 10);
            candidateCentres = List.of(centre1, centre2, centre3);

            Recycling result = Utils.findOptimalCentre(historic, candidateCentres);

            assertEquals(centre1, result, "Centre1 was not returned.");
        }

        @DisplayName("Testing if 'findOptimalCentre' returns the youngest centre when 2 identical candidates with differing ages are passed in.")
        @Test
        void Utils_FindOptimalCentre_SameGeneration_ReturnsYoungestCentre() {

            historic = new Historic(Location.A, 10);
            Recycling youngCentre = new TestRecycling(Location.B, 1, "Alpha", List.of(1.0, 1.0, 1.0));

            candidateCentres = List.of(centre1, youngCentre); // 5 yrs vs 1 yr

            Recycling result = Utils.findOptimalCentre(historic, candidateCentres);

            assertEquals(youngCentre, result, "The younger centre was not returned.");
        }

    //</editor-fold>

    //<editor-fold desc="compareGenerations Tests">

        @DisplayName("Testing if 'compareGenerations' returns the correct comparison values for All generations.")
        @ParameterizedTest
        @CsvSource({
                "'Alpha', 'Beta', -1",
                "'Beta', 'Alpha', 1",

                "'Beta', 'Gamma', -1",
                "'Gamma', 'Beta', 1",

                "'Alpha', 'Gamma', -1",
                "'Gamma', 'Alpha', 1",

                "'Alpha', 'Alpha', 0",
                "'Beta', 'Beta', 0",
                "'Gamma', 'Gamma', 0"
        })
        void Utils_CompareGenerations_AllValidGenerations_ReturnsExpectedComparison(String generation1, String generation2, int expectedResult) {

            int Result = Utils.compareGenerations(generation1, generation2);

            assertEquals(expectedResult, Result, "The result was not equal to the expected value.");

        }

        @DisplayName("Testing if 'compareGenerations' returns 0 when invalid (non existing) generations are passed in.")
        @ParameterizedTest
        @CsvSource({
                "'Alpha', 'xxx'",
                "'xxx', 'Alpha'",
                "'xxx', 'xxx'"
        })
        void Utils_CompareGenerations_AllValidGenerations_ReturnsZero(String generation1, String generation2) {

            int Result = Utils.compareGenerations(generation1, generation2); // for debugging

            assertEquals(0, Result, "");
//            assertThrows(IllegalArgumentException.class,
//                    () -> Utils.compareGenerations(generation1, generation2));

        }

        @DisplayName("Testing if 'compareGenerations' returns 0 when empty generations are passed in.")
        @ParameterizedTest
        @CsvSource({
                "'', 'Alpha'",
                "'Alpha', ''",
                "'', ''"
        })
        void Utils_CompareGenerations_EmptyInputGeneration_ReturnsZero(String generation1, String generation2) {

            int Result = Utils.compareGenerations(generation1, generation2);

            assertEquals(0, Result, "The result was not equal to '0''.");

        }


        @DisplayName("Testing if 'compareGenerations' returns 0 when null generations are passed in.")
        @ParameterizedTest
        @CsvSource({
                "null, 'Alpha'",
                "'Alpha', null",
                "null, null"
        })
        void Utils_CompareGenerations_NullInputGeneration_ReturnsZero(String generation1, String generation2) {

            int Result = Utils.compareGenerations(generation1, generation2);

            assertEquals(0, Result, "The result was not equal to '0''.");

        }

    //</editor-fold>

    //<editor-fold desc="calculateProcessDuration Tests">

        @DisplayName("Testing if 'calculateProcessDuration' calculates the correct processing duration for each waste type for Alpha sites.")
        @ParameterizedTest
        @CsvSource({

                "1000.0 ,0.0 ,500.0 , 500.0, 1000.0", // no paper
                "1000.0 ,500.0 , 0.0 , 500.0, 1000.0", // no plastic or glass
                "1000.0 ,500.0 ,500.0 , 0.0, 1000.0", // no metallic
                "15000.0 ,500.0 ,500.0 , 500.0, 1500.0", // all valid input
                "0.0 ,0.0 ,0.0 , 0.0, 0.0" // no input

        })
        void Utils_CalculateProcessDuration_AlphaSite_ReturnsExpectedDuration(double initialWaste, double paper, double plasticGlass, double metallic, double expectedDuration) {

            historic = new Historic(Location.A, initialWaste);
            historic.setPaper(paper);
            historic.setPlasticGlass(plasticGlass);
            historic.setMetallic(metallic);

            double result = Utils.calculateProcessDuration(historic, centre1);

            assertEquals(expectedDuration, result, "The result was not equal to the expected value.");
        }

        @DisplayName("Testing if 'calculateProcessDuration' calculates the correct processing duration for each waste type for Beta sites.")
        @ParameterizedTest
        @CsvSource({

                "1000.0 ,0.0 ,500.0 , 500.0, 666.6666666666666", // no paper
                "1000.0 ,500.0 , 0.0 , 500.0, 666.6666666666666", // no plastic or glass
                "1000.0 ,500.0 ,500.0 , 0.0, 666.6666666666666", // no metallic
                "15000.0 ,500.0 ,500.0 , 500.0, 1000.0", // all valid input
                "0.0 ,0.0 ,0.0 , 0.0, 0.0" // no input

        })
        void Utils_CalculateProcessDuration_BetaSite_ReturnsExpectedDuration(double initialWaste, double paper, double plasticGlass, double metallic, double expectedDuration) {

            historic = new Historic(Location.A, initialWaste);
            historic.setPaper(paper);
            historic.setPlasticGlass(plasticGlass);
            historic.setMetallic(metallic);

            double result = Utils.calculateProcessDuration(historic, centre2);

            assertEquals(expectedDuration, result, "The result was not equal to the expected value.");
        }

        @DisplayName("Testing if 'calculateProcessDuration' calculates the correct processing duration for each waste type for Gamma sites.")
        @ParameterizedTest
        @CsvSource({

                "1000.0, 0.0 ,500.0 , 500.0, 500.0", // no paper
                "1000.0, 500.0 , 0.0 , 500.0, 416.66666666666663", // no plastic or glass
                "1000.0, 500.0 ,500.0 , 0.0, 583.3333333333333", // no metallic
                "15000.0, 500.0 ,500.0 , 500.0, 749.9999999999999", // all valid input
                "0.0 ,0.0 ,0.0 , 0.0, 0.0" // no input

        })
        void Utils_CalculateProcessDuration_GammaSite_ReturnsExpectedDuration(double initialWaste, double paper, double plasticGlass, double metallic, double expectedDuration) {

            historic = new Historic(Location.A, initialWaste);
            historic.setPaper(paper);
            historic.setPlasticGlass(plasticGlass);
            historic.setMetallic(metallic);

            double result = Utils.calculateProcessDuration(historic, centre3);

            assertEquals(expectedDuration, result, "The result was not equal to the expected value.");
        }

    //</editor-fold>

    //<editor-fold desc="calculateTravelDuration Tests">


        @DisplayName("Testing if calculateTravelDuration returns -1.0 when the initial waste is below the transport capacity (20).")
        @ParameterizedTest
        @CsvSource({
                "0.0, 0.0, 0.0, 0.0, -1",
                "1.0, 1.0, 0.0, 0.0, -1",
                "5.0, 5.0, 0.0, 0.0, -1",
                "10.0, 10.0, 0.0, 0.0, -1",
                "19.0, 19.0, 0.0, 0.0, -1",
                "-10.0,-10.0, 0.0, 0.0, -1"
        })
        void Utils_CalculateTravelDuration_InputIsBelowThreshold_ReturnsNegativeOne(double initialWaste, double paper, double plasticGlass, double metallic, double expectedTravelDuration) {

            historic = new Historic(Location.A, initialWaste);
            historic.setPaper(paper);
            historic.setPlasticGlass(plasticGlass);
            historic.setMetallic(metallic);

            double result = Utils.calculateTravelDuration(historic, centre1);

            assertEquals(expectedTravelDuration, result, "The result was not equal to the expected value (-1).");
        }

        @DisplayName("Testing if calculateTravelDuration returns the correct travel duration when valid data is inputted.")
        @ParameterizedTest
        @CsvSource({
                "2000, 1000, 600, 400, 200"
        })
        void Utils_CalculateTravelDuration_ValidWaste_ReturnsCorrectTravelDuration(double initialWaste, double paper, double plasticGlass, double metallic, double expectedTravelDuration) {

            historic = new Historic(Location.A, initialWaste);
            historic.setPaper(paper);
            historic.setPlasticGlass(plasticGlass);
            historic.setMetallic(metallic);

            double result = Utils.calculateTravelDuration(historic, centre1);

            assertEquals(expectedTravelDuration, result, "The result was not equal to the expected value.");
        }

        @DisplayName("Testing if calculateTravelDuration returns the correct travel duration when valid data when the metallic waste is both above and below the threshold.")
        @ParameterizedTest
        @CsvSource({
                "3000, 1500, 900, 600, 300",
                "1000, 500, 300, 200, 100"
        })
        void Utils_CalculateTravelDuration_MetallicThreshold_ReturnsCorrectTravelDuration(double initialWaste, double paper, double plasticGlass, double metallic, double expectedTravelDuration) {

            historic = new Historic(Location.A, initialWaste);
            historic.setPaper(paper);
            historic.setPlasticGlass(plasticGlass);
            historic.setMetallic(metallic);

            double result = Utils.calculateTravelDuration(historic, centre1);

            assertEquals(expectedTravelDuration, result, "The result was not equal to the expected value.");
        }

        @DisplayName("Testing if calculateTravelDuration returns the correct travel duration when certain waste types are empty.")
        @ParameterizedTest
        @CsvSource({
                "1000, 0, 500, 500, 100",
                "1000, 500, 0, 500, 100",
                "1000, 500, 500, 0, 100"
        })
        void Utils_CalculateTravelDuration_EmptyWasteTypes_ReturnsCorrectTravelDuration(double initialWaste, double paper, double plasticGlass, double metallic, double expectedTravelDuration) {

            historic = new Historic(Location.A, initialWaste);
            historic.setPaper(paper);
            historic.setPlasticGlass(plasticGlass);
            historic.setMetallic(metallic);

            double result = Utils.calculateTravelDuration(historic, centre1);

            assertEquals(expectedTravelDuration, result, "The result was not equal to the expected value.");
        }

        @DisplayName("Testing if calculateTravelDuration returns the correct travel duration when only one waste type is used.")
        @ParameterizedTest
        @CsvSource({
                "1000, 1000, 0, 0, 100",
                "1000, 0, 1000, 0, 100",
                "1000, 0, 0, 1000, 100"
        })
        void Utils_CalculateTravelDurationOnly1WasteType_ReturnsCorrectTravelDuration(double initialWaste, double paper, double plasticGlass, double metallic, double expectedTravelDuration) {

            historic = new Historic(Location.A, initialWaste);
            historic.setPaper(paper);
            historic.setPlasticGlass(plasticGlass);
            historic.setMetallic(metallic);

            double result = Utils.calculateTravelDuration(historic, centre1);

            assertEquals(expectedTravelDuration, result, "The result was not equal to the expected value (100).");
        }

    //</editor-fold>

    @DisplayName("Testing the system with code instead of using the CLI.")
    @Test
    void FullSystem_ValidInputData_RunsCorrectly() {

        historic = new Historic(Location.A, 1000);
        ScenarioConfiguration scenario = new ScenarioConfiguration();

        candidateCentres  = new ArrayList<>(Arrays.asList(centre1, centre1));

        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, candidateCentres);

        List<Recycling> viable = Utils.findViableCentres(historic, candidateCentres);

        Recycling optimal = Utils.findOptimalCentre(historic, viable);

        double travelTime = Utils.calculateTravelDuration(historic, optimal);
        double processTime = Utils.calculateProcessDuration(historic, optimal);
        double totalTime = (travelTime + processTime);

        assertNotNull(viable, "Viable centres is null.");
        assertNotNull(optimal, "Optimal centres is null.");
        assertTrue(travelTime > 0, "Travel time < 0.");
        assertTrue(processTime > 0, "Process time < 0.");

    }



}