import java.util.TreeSet;

public class Ques2b {
    // Method to determine if two friends can sit together
    public static boolean canSitTogether(int[] seatNumbers, int maxIndexDiff, int maxValueDiff) {
        // TreeSet to maintain the current window of seat numbers
        TreeSet<Integer> seatWindow = new TreeSet<>();

        // Iterate through each seat in the array
        for (int i = 0; i < seatNumbers.length; i++) {
            // Remove seats that are out of the current index range
            if (i > maxIndexDiff) {
                seatWindow.remove(seatNumbers[i - maxIndexDiff - 1]);
            }

            // Check if there is any seat within maxValueDiff in the current window
            Integer floor = seatWindow.floor(seatNumbers[i] + maxValueDiff);
            Integer ceiling = seatWindow.ceiling(seatNumbers[i] - maxValueDiff);

            // If any seat is found within the acceptable value difference, return true
            if ((floor != null && floor >= seatNumbers[i] - maxValueDiff) ||
                    (ceiling != null && ceiling <= seatNumbers[i] + maxValueDiff)) {
                return true;
            }

            // Add the current seat to the TreeSet
            seatWindow.add(seatNumbers[i]);
        }

        // If no suitable pair is found, return false
        return false;
    }

    public static void main(String[] args) {
        // Example seat numbers and constraints
        int[] seatNumbers = {2, 3, 5, 4, 9};
        int maxIndexDiff = 2;
        int maxValueDiff = 1;

        // Check if two friends can sit together based on the constraints
        boolean result = canSitTogether(seatNumbers, maxIndexDiff, maxValueDiff);
        System.out.println(result); // Output: true


        // Example 2: Additional example
        int[] seatNumbers2 = {10, 15, 20, 25, 30};
        int maxIndexDiff2 = 3;
        int maxValueDiff2 = 5;
        boolean result2 = canSitTogether(seatNumbers2, maxIndexDiff2, maxValueDiff2);
        System.out.println("Example 2 result: " + result2); // Output: false
    }
}
