import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Dijkstra algorithm implementation with corrections.
 */

public class Dijkstra {
    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
        // Initialize distances
        source.setDistance(0);
        
        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Edge e = currentNode.getEdge(); e != null; e = e.getNext()) {
                Node adjacentNode = e.getTo();
                int edgeWeight = e.getDistance();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }      
        return graph;
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

        List<double[]> nodeList = graph.read_from_file("test-nodes.txt");
        List<double[]> edgeList = graph.read_from_file("test-edges.txt");

        graph.init_graph(nodeList, edgeList);
        System.out.println("Graph initialized");
        System.out.println("Total nodes: " + graph.getNodes().size());
        
        Node sourceNode = graph.getNodes().get(1); // Assuming 0-based index
        System.out.println("Source Node: " + sourceNode.getName());

        System.out.println("\n ------------- \n");

        graph = Dijkstra.calculateShortestPathFromSource(graph, sourceNode);

        graph.print_graph();
        System.out.println();
        Node destinationNode = graph.getNodes().get(3); // Assuming 0-based index
        
        for (Node node : destinationNode.getShortestPath()) {
            System.out.print(" ->  " + node.getName() + "(distance)" + node.getEdge().getDistance());
        }
    }
}
