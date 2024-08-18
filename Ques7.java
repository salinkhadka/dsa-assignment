import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Ques7 extends JFrame {

    private Map<String, Point> cityPositions;
    private Map<String, Integer> cityIndexMap;
    private int[][] distances;

    private String startCity;
    private String endCity;

    private List<Integer> shortestPath;

    public Ques7() {
        setTitle("Route Optimization for Delivery Service");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeCitiesAndDistances();

        setLayout(new BorderLayout(10, 10));

        GraphPanel graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        pathPanel.setBackground(Color.LIGHT_GRAY);

        JLabel pathLabel = new JLabel("Shortest Path will be displayed here");
        pathLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pathLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pathPanel.add(pathLabel, BorderLayout.CENTER);
        add(pathPanel, BorderLayout.EAST);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel startLabel = new JLabel("Start City:");
        startLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(startLabel);

        JComboBox<String> startComboBox = new JComboBox<>(cityIndexMap.keySet().toArray(new String[0]));
        startComboBox.addActionListener(e -> {
            startCity = (String) startComboBox.getSelectedItem();
            graphPanel.repaint();
        });
        startComboBox.setToolTipText("Select the starting city");
        inputPanel.add(startComboBox);

        JLabel endLabel = new JLabel("End City:");
        endLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(endLabel);

        JComboBox<String> endComboBox = new JComboBox<>(cityIndexMap.keySet().toArray(new String[0]));
        endComboBox.addActionListener(e -> {
            endCity = (String) endComboBox.getSelectedItem();
            graphPanel.repaint();
        });
        endComboBox.setToolTipText("Select the ending city");
        inputPanel.add(endComboBox);

        JButton optimizeButton = new JButton("Optimize Route");
        optimizeButton.addActionListener(e -> {
            if (startCity != null && endCity != null && !startCity.equals(endCity)) {
                findShortestPath(startCity, endCity);
                pathLabel.setText("Shortest Path: " + shortestPathToString());
                graphPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please select different start and end cities.");
            }
        });
        optimizeButton.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(optimizeButton);

        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeCitiesAndDistances() {
        cityPositions = new HashMap<>();
        cityPositions.put("New York", new Point(200, 100));
        cityPositions.put("Los Angeles", new Point(100, 300));
        cityPositions.put("Chicago", new Point(400, 150));
        cityPositions.put("Houston", new Point(300, 400));
        cityPositions.put("Miami", new Point(500, 300));
        cityPositions.put("San Francisco", new Point(600, 200));

        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Miami", "San Francisco"};
        cityIndexMap = new HashMap<>();
        for (int i = 0; i < cities.length; i++) {
            cityIndexMap.put(cities[i], i);
        }

        distances = new int[cities.length][cities.length];
        for (int i = 0; i < cities.length; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
            distances[i][i] = 0;
        }

        // Add connections between cities with updated distances
        addConnection("New York", "Los Angeles", 2800);
        addConnection("Los Angeles", "Houston", 1500);
        addConnection("Houston", "Miami", 1300);
        addConnection("Miami", "San Francisco", 2500);
        addConnection("Chicago", "San Francisco", 2100);
        addConnection("Chicago", "Los Angeles", 2000);
    }

    private void addConnection(String city1, String city2, int distance) {
        int index1 = cityIndexMap.get(city1);
        int index2 = cityIndexMap.get(city2);
        distances[index1][index2] = distance;
        distances[index2][index1] = distance;
    }

    private void findShortestPath(String startCity, String endCity) {
        int startIndex = cityIndexMap.get(startCity);
        int endIndex = cityIndexMap.get(endCity);
        shortestPath = dijkstra(startIndex, endIndex);
    }

    private List<Integer> dijkstra(int start, int end) {
        int numCities = distances.length;
        int[] minDistances = new int[numCities];
        boolean[] visited = new boolean[numCities];
        int[] previous = new int[numCities];

        Arrays.fill(minDistances, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);
        minDistances[start] = 0;

        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(i -> minDistances[i]));
        queue.add(start);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            visited[u] = true;

            for (int v = 0; v < numCities; v++) {
                if (!visited[v] && distances[u][v] != Integer.MAX_VALUE) {
                    int alt = minDistances[u] + distances[u][v];
                    if (alt < minDistances[v]) {
                        minDistances[v] = alt;
                        previous[v] = u;
                        queue.add(v);
                    }
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        int current = end;
        while (current != -1) {
            path.add(current);
            current = previous[current];
        }
        Collections.reverse(path);
        return path;
    }

    private String shortestPathToString() {
        if (shortestPath == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shortestPath.size(); i++) {
            sb.append(getCityName(shortestPath.get(i)));
            if (i < shortestPath.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    private class GraphPanel extends JPanel {

        private static final int NODE_RADIUS = 20;
        private static final Color NODE_COLOR = Color.BLUE;
        private static final Color EDGE_COLOR = Color.BLACK;
        private static final Color PATH_COLOR = Color.RED;
        private static final Font EDGE_FONT = new Font("Arial", Font.PLAIN, 12);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(EDGE_COLOR);
            g2d.setFont(EDGE_FONT);
            for (int i = 0; i < distances.length; i++) {
                for (int j = i + 1; j < distances.length; j++) {
                    if (distances[i][j] != Integer.MAX_VALUE) {
                        Point city1Pos = cityPositions.get(getCityName(i));
                        Point city2Pos = cityPositions.get(getCityName(j));
                        g2d.drawLine(city1Pos.x, city1Pos.y, city2Pos.x, city2Pos.y);

                        int centerX = (city1Pos.x + city2Pos.x) / 2;
                        int centerY = (city1Pos.y + city2Pos.y) / 2;

                        String distanceLabel = String.valueOf(distances[i][j]);
                        g2d.drawString(distanceLabel, centerX, centerY);
                    }
                }
            }

            g2d.setColor(NODE_COLOR);
            for (String city : cityPositions.keySet()) {
                Point cityPos = cityPositions.get(city);
                g2d.fillOval(cityPos.x - NODE_RADIUS, cityPos.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                g2d.drawString(city, cityPos.x - NODE_RADIUS, cityPos.y - NODE_RADIUS);
            }

            if (startCity != null && endCity != null) {
                g2d.setColor(Color.RED);
                Point startCityPos = cityPositions.get(startCity);
                Point endCityPos = cityPositions.get(endCity);
                g2d.drawOval(startCityPos.x - NODE_RADIUS, startCityPos.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                g2d.drawOval(endCityPos.x - NODE_RADIUS, endCityPos.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                g2d.drawLine(startCityPos.x, startCityPos.y, endCityPos.x, endCityPos.y);
            }

            if (shortestPath != null && !shortestPath.isEmpty()) {
                g2d.setColor(PATH_COLOR);
                for (int i = 0; i < shortestPath.size() - 1; i++) {
                    int from = shortestPath.get(i);
                    int to = shortestPath.get(i + 1);
                    Point fromPos = cityPositions.get(getCityName(from));
                    Point toPos = cityPositions.get(getCityName(to));
                    g2d.drawLine(fromPos.x, fromPos.y, toPos.x, toPos.y);
                }
            }
        }
    }

    private String getCityName(int index) {
        for (Map.Entry<String, Integer> entry : cityIndexMap.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Ques7::new);
    }
}
