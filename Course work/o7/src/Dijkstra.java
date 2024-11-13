import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Dijkstra algorithm implementation with corrections.
 */

public class Dijkstra {

    

    public static void calculateShortestPathFromSource(Node sourceNode, Node destinationNode, int numberOfNodes, HashMap<Integer, Node> nodes) {
        Pre pre = new Pre();
        pre.init_pre(sourceNode, numberOfNodes, nodes);
        // Initialize distances
        sourceNode.setDistance(0);
        sourceNode.setTime(0);
        
        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(sourceNode);

        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            if (currentNode.equals(destinationNode)) {
                System.out.println("Shortest path to destination found with distance: " + currentNode.getDistance());
                return;
            }

            for (Edge e = currentNode.getEdge(); e != null; e = e.getNext()) {
                Node adjacentNode = e.getTo();
                int edgeWeight = e.getDistance();
                int edgeTime = e.getDriveTime();
                if (!settledNodes.contains(adjacentNode) && adjacentNode != null) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, edgeTime, currentNode);
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

    private static void calculateMinimumDistance(Node evaluationNode, int edgeWeight, int edgeTime, Node sourceNode) {
        int sourceDistance = sourceNode.getDistance();
        int sourceTime = sourceNode.getTime();
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            evaluationNode.setTime(sourceTime + edgeTime);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }




    public static void main(String[] args) {
        Graph graph = new Graph();

        List<double[]> nodeList = graph.read_from_file("test-noder.txt");
        List<double[]> edgeList = graph.read_from_file("test-kanter.txt");
        Map<Integer, InterestPoint> interestPoints = graph.readInterestPoints("test-interessepkt.txt");

        graph.init_graph(nodeList, edgeList, interestPoints);
        System.out.println("Graph initialized");
        System.out.println("Total nodes: " + graph.getNodes().size());
        System.out.println("\n ------------- \n");
        Node source;
        Node destination;
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
                graph.setSourceNode(source);
                System.out.println("Source Node: " + source.getName());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Invalid input");
                e.getMessage();
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
                destination = graph.getNodes().get(Integer.parseInt(input));
                System.out.println("Destination Node: " + destination.getName());
                Dijkstra.calculateShortestPathFromSource(source, destination, graph.getNumberOfNodes(), graph.getNodes());

                if (destination.getShortestPath().size() == 0) {
                    System.out.println("No path found");
                } else {
                    System.out.println("\nShortest path: ");
                    for (Node node : destination.getShortestPath()) {
                        System.out.print(" ->  " + node.getName() + " (distance: " + node.getEdge().getDistance() +")");
                    }
                    System.out.println(" ->  " + destination.getName());
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }
        scanner.close();
    }
}
