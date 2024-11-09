import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Dijkstra algorithm implementation with corrections.
 */

public class Dijkstra {

    public static void calculateShortestPathFromSource(Node sourceNode) {
        // Initialize distances
        sourceNode.setDistance(0);
        
        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(sourceNode);

        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Edge e = currentNode.getEdge(); e != null; e = e.getNext()) {
                Node adjacentNode = e.getTo();
                int edgeWeight = e.getDistance();
                if (!settledNodes.contains(adjacentNode) && adjacentNode != null) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }      
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode, int edgeWeight, Node sourceNode) {
        int sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }




    public static void main(String[] args) {
        Graph graph = new Graph();

        List<double[]> nodeList = graph.read_from_file("test-noder.txt");
        List<double[]> edgeList = graph.read_from_file("test-kanter.txt");

        graph.init_graph(nodeList, edgeList);
        System.out.println("Graph initialized");
        System.out.println("Total nodes: " + graph.getNodes().size());
        
        Node source = graph.getNodes().get(1); // Assuming 0-based index
        System.out.println("Source Node: " + source.getName());

        System.out.println("\n ------------- \n");

        Dijkstra.calculateShortestPathFromSource(source);

        graph.print_graph();
        Node destinationNode; // Assuming 0-based index
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n ------------- \n");
            System.out.println("Press x to exit");
            System.out.println("Select source node: ");
            try {
                String input = scanner.nextLine();
                if (input.equals("x")) {
                    break;
                }
                source = graph.getNodes().get(Integer.parseInt(input));
                System.out.println("Source Node: " + source.getName());
                Dijkstra.calculateShortestPathFromSource(source);
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }

            try {
                System.out.println("\n ------------- \n");
                System.out.println("Press x to exit");
                System.out.println("Select destination node: ");
                String input = scanner.nextLine();
                if (input.equals("x")) {
                    break;
                }
                destinationNode = graph.getNodes().get(Integer.parseInt(input));
                System.out.println("Destination Node: " + destinationNode.getName());
                if (destinationNode.getShortestPath().size() == 0) {
                    System.out.println("No path found");
                } else {
                    System.out.println("\nShortest path: ");
                    for (Node node : destinationNode.getShortestPath()) {
                        System.out.print(" ->  " + node.getName() + " (distance: " + node.getEdge().getDistance() +")");
                    }
                    System.out.println(" ->  " + destinationNode.getName());
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }
        scanner.close();
    }
}
