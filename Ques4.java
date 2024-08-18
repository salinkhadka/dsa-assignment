import java.util.*;

public class Ques4 {

    // Inner class representing an edge in the graph
    static class Edge {
        int to;
        int weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // Method to perform Dijkstra's algorithm to find shortest paths from the source node
    private static int[] dijkstra(List<Edge>[] graph, int source) {
        int n = graph.length;
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int d = current[1];

            if (d > dist[u]) continue;

            for (Edge edge : graph[u]) {
                int v = edge.to;
                int weight = edge.weight;
                if (d + weight < dist[v]) {
                    dist[v] = d + weight;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }
        return dist;
    }

    // Method to modify road weights to achieve the target travel time between source and destination
    public static List<int[]> modifyRoads(int n, int[][] roads, int source, int destination, int target) {
        List<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        List<int[]> constructionEdges = new ArrayList<>();
        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            int w = road[2];
            if (w == -1) {
                // Collect roads under construction
                constructionEdges.add(new int[]{u, v});
            } else {
                // Add edges with predefined weights to the graph
                graph[u].add(new Edge(v, w));
                graph[v].add(new Edge(u, w)); // Assuming undirected graph
            }
        }

        // Compute the initial shortest path from source to destination
        int[] dist = dijkstra(graph, source);
        int initialDistance = dist[destination];

        // Calculate the additional time required to meet the target
        int additionalTime = target - initialDistance;

        if (additionalTime <= 0) {
            // Target already achieved or not achievable with current weights
            return Arrays.asList(roads);
        }

        // Adjust the weights of construction roads to achieve the target
        for (int[] edge : constructionEdges) {
            int u = edge[0];
            int v = edge[1];
            int weight = additionalTime;
            graph[u].add(new Edge(v, weight));
            graph[v].add(new Edge(u, weight)); // Assuming undirected graph
        }

        // Recalculate the shortest path with modified weights
        dist = dijkstra(graph, source);
        int newDistance = dist[destination];
        if (newDistance != target) {
            throw new RuntimeException("Unable to achieve the target travel time.");
        }

        // Prepare the output format with modified road weights
        List<int[]> modifiedRoads = new ArrayList<>();
        for (int[] road : roads) {
            if (road[2] == -1) {
                modifiedRoads.add(new int[]{road[0], road[1], additionalTime});
            } else {
                modifiedRoads.add(road);
            }
        }

        return modifiedRoads;
    }

    public static void main(String[] args) {
        // Test Case 1
        int n1 = 5;
        int[][] roads1 = {{4, 1, -1}, {2, 0, -1}, {0, 3, -1}, {4, 3, -1}};
        int source1 = 0;
        int destination1 = 1;
        int target1 = 5;

        List<int[]> result1 = modifyRoads(n1, roads1, source1, destination1, target1);
        System.out.println("Test Case 1:");
        for (int[] road : result1) {
            System.out.println(Arrays.toString(road));
        }

        // Test Case 2
        int n2 = 6;
        int[][] roads2 = {{0, 1, 2}, {1, 2, -1}, {2, 3, 1}, {3, 4, -1}, {4, 5, 2}};
        int source2 = 0;
        int destination2 = 5;
        int target2 = 6;

        List<int[]> result2 = modifyRoads(n2, roads2, source2, destination2, target2);
        System.out.println("Test Case 2:");
        for (int[] road : result2) {
            System.out.println(Arrays.toString(road));
        }
    }
}
