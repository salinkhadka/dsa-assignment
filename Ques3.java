import java.util.*;

public class Ques3 {
    private int[] parent;
    private int[] rank;
    private Set<Integer>[] restrictions;

    public Ques3(int numberOfHouses, List<int[]> restrictionPairs) {
        parent = new int[numberOfHouses];
        rank = new int[numberOfHouses];
        restrictions = new HashSet[numberOfHouses];

        // Initialize the union-find data structure
        for (int i = 0; i < numberOfHouses; i++) {
            parent[i] = i;
            rank[i] = 0;
            restrictions[i] = new HashSet<>();
        }

        // Process and store the restrictions
        for (int[] pair : restrictionPairs) {
            int houseA = pair[0];
            int houseB = pair[1];
            restrictions[houseA].add(houseB);
            restrictions[houseB].add(houseA);
        }
    }

    // Find the root of a house with path compression
    private int findRoot(int house) {
        if (parent[house] != house) {
            parent[house] = findRoot(parent[house]);
        }
        return parent[house];
    }

    // Union two sets by rank
    private void unionSets(int houseA, int houseB) {
        int rootA = findRoot(houseA);
        int rootB = findRoot(houseB);
        if (rootA != rootB) {
            if (rank[rootA] > rank[rootB]) {
                parent[rootB] = rootA;
            } else if (rank[rootA] < rank[rootB]) {
                parent[rootA] = rootB;
            } else {
                parent[rootB] = rootA;
                rank[rootA]++;
            }
        }
    }

    // Check if two houses are in the same set
    private boolean areConnected(int houseA, int houseB) {
        return findRoot(houseA) == findRoot(houseB);
    }

    public List<String> evaluateRequests(int[][] requests) {
        List<String> results = new ArrayList<>();

        for (int[] request : requests) {
            int requester = request[0];
            int receiver = request[1];

            if (areConnected(requester, receiver)) {
                results.add("denied");
            } else if (restrictions[requester].contains(receiver) || restrictions[receiver].contains(requester)) {
                results.add("denied");
            } else {
                // Approve the request and union the sets
                results.add("approved");
                unionSets(requester, receiver);
            }
        }
        return results;
    }

    public static void main(String[] args) {
        // Test Case 1
        int numberOfHouses1 = 3;
        List<int[]> restrictions1 = Arrays.asList(new int[]{0, 1});
        int[][] requests1 = { {0, 2}, {2, 1} };
        Ques3 manager1 = new Ques3(numberOfHouses1, restrictions1);
        System.out.println("Test Case 1 result: " + manager1.evaluateRequests(requests1)); // Output: [approved, denied]

        // Test Case 2
        int numberOfHouses2 = 5;
        List<int[]> restrictions2 = Arrays.asList(new int[]{0, 1}, new int[]{1, 2}, new int[]{2, 3});
        int[][] requests2 = { {0, 4}, {1, 2}, {3, 1}, {3, 4} };
        Ques3 manager2 = new Ques3(numberOfHouses2, restrictions2);
        System.out.println("Test Case 2 result: " + manager2.evaluateRequests(requests2)); // Output: [approved, denied, approved, denied]
    }
}
