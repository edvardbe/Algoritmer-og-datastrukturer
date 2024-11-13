import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * This class is used to store the information of the A-star, , algorithm.
 */
public class ALT{
    public static void calculateShortestPathFromSource(Node source, Node destination, int numberOfNodes, HashMap<Integer, Node> nodes){
        Pre pre = new Pre();
        pre.init_pre(source, numberOfNodes, nodes);
        
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.getDistance() + n.getHeuristic()));
        source.setDistance(0);
        source.setHeuristic(euclideanDistance(source, destination)); // Set initial heuristic
        priorityQueue.add(source);
    
        while (!priorityQueue.isEmpty()){
            Node currentNode = priorityQueue.poll();
            if (currentNode.equals(destination)) {
                System.out.println("Shortest path to destination found with distance: " + currentNode.getDistance());
                return;
            }
    
            for (Edge e = currentNode.getEdge(); e != null; e = e.getNext()){
                Node adjacentNode = e.getTo();
                int edgeWeight = e.getDistance();
                int edgeTime = e.getDriveTime();
    
                if (adjacentNode != null) {
                    boolean updated = calculateMinimumDistance(adjacentNode, edgeWeight, edgeTime, currentNode);
                    if (updated) {
                        priorityQueue.remove(adjacentNode);  // Remove to reinsert with updated priority
                        adjacentNode.setHeuristic(euclideanDistance(adjacentNode, destination));  // Set heuristic for adjacent
                        priorityQueue.add(adjacentNode);
                    }
                }
            }
        }
    }

    private static boolean calculateMinimumDistance(Node evaluationNode, int edgeWeight, int edgeTime, Node sourceNode){
        int sourceDistance = sourceNode.getDistance();
        int sourceTime = sourceNode.getTime();
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()){
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            evaluationNode.setTime(sourceTime + edgeTime);
            List<Node> shortestPath = new ArrayList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
            return true;
        }
        return false;
    }

    private static PriorityQueue makePriorityQueue(HashMap<Integer, Node> nodes, Node source, Node destination){
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(a -> a.getHeuristic() + a.getDistance()));
        for (Node n : nodes.values()){
            n.setHeuristic(euclideanDistance(n, destination));
            priorityQueue.add(n);
        }
        source.setDistance(0);
        source.setTime(0);
        return priorityQueue;
    }
    public static double euclideanDistance(Node source, Node destination){
        return Math.sqrt(Math.pow(source.getVector().getLatitude() - destination.getVector().getLatitude(), 2) + Math.pow(source.getVector().getLongitude() - destination.getVector().getLongitude(), 2));
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        List<double[]> nodeList = graph.read_from_file("test-noder.txt");
        List<double[]> edgeList = graph.read_from_file("test-kanter.txt");
        Map<Integer, InterestPoint> interestPoints = graph.readInterestPoints("interessepkt.txt");

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
                ALT.calculateShortestPathFromSource(source, destination, graph.getNumberOfNodes(), graph.getNodes());

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

