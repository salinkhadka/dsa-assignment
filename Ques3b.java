import java.util.*;

public class Ques3b {

    public static List<Integer> rearrangePassengers(List<Integer> passengers, int k) {
        // Convert the list to an array for easier manipulation
        Integer[] arr = passengers.toArray(new Integer[0]);
        int n = arr.length;

        // Traverse the array in chunks of size k
        for (int i = 0; i < n; i += k) {
            // Determine the end index for the current chunk
            int end = Math.min(i + k - 1, n - 1);

            // Reverse the current chunk
            reverseChunk(arr, i, end);
        }

        // Convert the array back to a list and return
        return Arrays.asList(arr);
    }

    // Helper method to reverse a chunk of the array from start to end
    private static void reverseChunk(Integer[] arr, int start, int end) {
        while (start < end) {
            // Swap elements at start and end indices
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }

    public static void main(String[] args) {
        // Test Case 1
        List<Integer> passengers1 = Arrays.asList(1, 2, 3, 4, 5);
        int k1 = 2;
        System.out.println("Test Case 1 result: " + rearrangePassengers(passengers1, k1)); // Output: [2, 1, 4, 3, 5]

        // Test Case 2
        List<Integer> passengers2 = Arrays.asList(1, 2, 3, 4, 5);
        int k2 = 3;
        System.out.println("Test Case 2 result: " + rearrangePassengers(passengers2, k2)); // Output: [3, 2, 1, 4, 5]
    }
}
