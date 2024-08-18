public class Ques1b {

    // Helper method to shift a character with wrap-around
    public static char rotateChar(char c, int shiftAmount, int direction) {
        final int BASE = 'a';
        final int ALPHABET_SIZE = 26;
        int currentPosition = c - BASE;
        int newPosition;
        if (direction == 1) { // Clockwise
            newPosition = (currentPosition + shiftAmount) % ALPHABET_SIZE;
        } else { // Counter-clockwise
            newPosition = (currentPosition - shiftAmount + ALPHABET_SIZE) % ALPHABET_SIZE;
        }
        return (char) (BASE + newPosition);
    }

    // Main method to decode the message
    public static String decodeMessage(String message, int[][] operations) {
        char[] chars = message.toCharArray();

        // Apply each rotation operation
        for (int[] operation : operations) {
            int startIndex = operation[0];
            int endIndex = operation[1];
            int direction = operation[2];

            // Rotate characters in the specified range
            int shiftAmount = 1; // Each operation rotates by 1 position
            for (int i = startIndex; i <= endIndex; i++) {
                chars[i] = rotateChar(chars[i], shiftAmount, direction);
            }
        }

        // Convert character array back to string
        return new String(chars);
    }

    public static void main(String[] args) {
        // Example of usage
        String message = "hello";
        int[][] operations = {{0, 1, 1}, {2, 3, 0}, {0, 2, 1}};
        System.out.println(decodeMessage(message, operations)); // Output: jglko
    }
}
