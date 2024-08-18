import java.util.Arrays;
import java.util.Random;

public class Ques5 {

    // Method to calculate the total distance of a given tour
    public static int calculateTourLength(int[][] distanceMatrix, int[] tour) {
        int totalDistance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            totalDistance += distanceMatrix[tour[i]][tour[i + 1]];
        }
        totalDistance += distanceMatrix[tour[tour.length - 1]][tour[0]]; // Return to start
        return totalDistance;
    }

    // Method to generate a random permutation of the cities
    public static int[] generateRandomTour(int numCities) {
        int[] tour = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            tour[i] = i;
        }
        Random rand = new Random();
        for (int i = 0; i < numCities; i++) {
            int j = rand.nextInt(numCities);
            // Swap tour[i] and tour[j]
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
        }
        return tour;
    }

    // Hill Climbing algorithm to find a near-optimal solution to TSP
    public static int[] hillClimbing(int[][] distanceMatrix, int[] initialTour) {
        int numCities = distanceMatrix.length;
        int[] currentTour = Arrays.copyOf(initialTour, numCities);
        int currentLength = calculateTourLength(distanceMatrix, currentTour);
        boolean improved = true;

        while (improved) {
            improved = false;
            // Try to improve the tour by swapping two cities
            for (int i = 0; i < numCities - 1; i++) {
                for (int j = i + 1; j < numCities; j++) {
                    int[] newTour = Arrays.copyOf(currentTour, numCities);
                    // Swap cities i and j
                    int temp = newTour[i];
                    newTour[i] = newTour[j];
                    newTour[j] = temp;

                    int newLength = calculateTourLength(distanceMatrix, newTour);
                    if (newLength < currentLength) {
                        currentTour = newTour;
                        currentLength = newLength;
                        improved = true;
                    }
                }
            }
        }

        return currentTour;
    }

    public static void main(String[] args) {
        // Example 1: Distance matrix for a simple 4-city example
        int[][] distanceMatrix1 = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        // Number of cities
        int numCities1 = distanceMatrix1.length;

        // Generate initial random tour
        int[] initialTour1 = generateRandomTour(numCities1);
        System.out.println("Example 1:");
        System.out.println("Initial tour: " + Arrays.toString(initialTour1));
        System.out.println("Initial tour length: " + calculateTourLength(distanceMatrix1, initialTour1));

        // Apply Hill Climbing algorithm
        int[] optimalTour1 = hillClimbing(distanceMatrix1, initialTour1);
        System.out.println("Optimized tour: " + Arrays.toString(optimalTour1));
        System.out.println("Optimized tour length: " + calculateTourLength(distanceMatrix1, optimalTour1));
        System.out.println();

        // Example 2: Distance matrix for a different 5-city example
        int[][] distanceMatrix2 = {
                {0, 29, 20, 21, 16},
                {29, 0, 15, 17, 28},
                {20, 15, 0, 35, 25},
                {21, 17, 35, 0, 30},
                {16, 28, 25, 30, 0}
        };

        int numCities2 = distanceMatrix2.length;

        // Generate initial random tour
        int[] initialTour2 = generateRandomTour(numCities2);
        System.out.println("Example 2:");
        System.out.println("Initial tour: " + Arrays.toString(initialTour2));
        System.out.println("Initial tour length: " + calculateTourLength(distanceMatrix2, initialTour2));

        // Apply Hill Climbing algorithm
        int[] optimalTour2 = hillClimbing(distanceMatrix2, initialTour2);
        System.out.println("Optimized tour: " + Arrays.toString(optimalTour2));
        System.out.println("Optimized tour length: " + calculateTourLength(distanceMatrix2, optimalTour2));
    }
}
