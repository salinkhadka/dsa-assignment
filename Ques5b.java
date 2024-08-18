import java.util.Deque;
import java.util.LinkedList;

public class Ques5b {

    public static int longestValidHike(int[] elevations, int maxDifference) {
        int n = elevations.length;
        if (n == 0) {
            return 0;
        }

        // Deques to maintain indices of the minimum and maximum elevations in the current window
        Deque<Integer> minDeque = new LinkedList<>();
        Deque<Integer> maxDeque = new LinkedList<>();

        int leftPointer = 0;  // Start index of the current window
        int maxLength = 0;    // To store the maximum length of a valid hike

        for (int rightPointer = 0; rightPointer < n; rightPointer++) {
            // Maintain the deque for minimum elevations
            while (!minDeque.isEmpty() && elevations[minDeque.peekLast()] >= elevations[rightPointer]) {
                minDeque.pollLast();
            }
            minDeque.addLast(rightPointer);

            // Maintain the deque for maximum elevations
            while (!maxDeque.isEmpty() && elevations[maxDeque.peekLast()] <= elevations[rightPointer]) {
                maxDeque.pollLast();
            }
            maxDeque.addLast(rightPointer);

            // Adjust the left pointer to ensure the difference between max and min elevations is within the allowed limit
            while (elevations[maxDeque.peekFirst()] - elevations[minDeque.peekFirst()] > maxDifference) {
                if (minDeque.peekFirst() == leftPointer) minDeque.pollFirst();
                if (maxDeque.peekFirst() == leftPointer) maxDeque.pollFirst();
                leftPointer++;
            }

            // Update the maximum length of the valid hike
            maxLength = Math.max(maxLength, rightPointer - leftPointer + 1);
        }

        return maxLength;
    }

    public static void main(String[] args) {
        // Test case 1
        int[] elevations1 = {4, 2, 1, 4, 3, 4, 8, 15};
        int maxDifference1 = 3;
        System.out.println("Example 1: Longest valid hike length = " + longestValidHike(elevations1, maxDifference1)); // Expected Output: 6

        // Additional test cases
        int[] elevations2 = {10, 13, 15, 18, 12, 8, 7, 14, 20};
        int maxDifference2 = 5;
        System.out.println("Example 2: Longest valid hike length = " + longestValidHike(elevations2, maxDifference2)); // Expected Output: 3

        int[] elevations3 = {1, 5, 6, 10, 15, 20};
        int maxDifference3 = 5;
        System.out.println("Example 3: Longest valid hike length = " + longestValidHike(elevations3, maxDifference3)); // Expected Output: 3

        int[] elevations4 = {3, 5, 8, 12, 15, 20, 25, 22};
        int maxDifference4 = 7;
        System.out.println("Example 4: Longest valid hike length = " + longestValidHike(elevations4, maxDifference4)); // Expected Output: 5

        int[] elevations5 = {1, 2, 2, 2, 3, 4, 5};
        int maxDifference5 = 1;
        System.out.println("Example 5: Longest valid hike length = " + longestValidHike(elevations5, maxDifference5)); // Expected Output: 3
    }
}
