import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Graph {
    private Node sourceNode;
    private Node destinationNode;
    private int numberOfNodes;
    private int numberOfEdges;
    private HashMap<Integer, Node> nodes;
    public Graph(){};

    

    public void dijkstra(Node source){
        Pre pre = new Pre();
        pre.init_pre(source, numberOfNodes, nodes);
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for(int i = numberOfNodes; i > 1; --i){
            Node n = extract_min();
            for (Edge e = n.getEdge(); e != null; e = e.getNext()){
                shorten(n , e);
            }
        }
    }

    public Node extract_min(){
        Node min = null;
        for (Node n : nodes.values()){
            if (min == null || ((Pre) n.getData()).get_depth() < ((Pre) min.getData()).get_depth()){
                min = n;
            }
        }
        return min;
    }

    public void shorten(Node n, Edge e){
        Node m = e.getTo();
        if (((Pre) m.getData()).get_depth() > ((Pre) n.getData()).get_depth() + e.getDriveTime()){
            ((Pre) m.getData()).depth = ((Pre) n.getData()).depth + e.getDriveTime();
            ((Pre) m.getData()).pre = n;
        }
    }


    /**
     * Initiates a graph using the {@link #read_from_file(String path)}
     * method. Initiates N, amount of nodes, and E, amount of edges,
     * and the array of nodes, nodes.
     * @param path
     */
    public void init_graph(List<double[]> nodeList, List<double[]> edgeList){

        this.numberOfNodes = (int) nodeList.get(0)[0];
        this.numberOfEdges = (int) edgeList.get(0)[0];
        this.nodes = new HashMap<>();
        for (int i = 1; i < numberOfNodes + 1; i++) {
            int key = (int) nodeList.get(i)[0];
            double latitude = nodeList.get(i)[1] * Math.PI / 180;
            double longitude = nodeList.get(i)[2] * Math.PI / 180;
            nodes.put(key, new Node(key, latitude, longitude));

        }

        for (int i = 1; i < numberOfEdges + 1; i++){
            int from = (int) edgeList.get(i)[0];
            int to = (int) edgeList.get(i)[1];

            Node toNode = nodes.get(to);
            Edge fromEdge = nodes.get(from).getEdge();
            int driveTime = (int) edgeList.get(i)[2];
            int distance = (int) edgeList.get(i)[3];
            int speedLimit = (int) edgeList.get(i)[4];
            Edge e = new Edge(toNode, fromEdge, driveTime, distance, speedLimit);
            nodes.get(from).setEdge(e);
        }
    }

    public void print_graph(){
        nodes.forEach((k, v) -> {
            System.out.print(k + ": ");
            for (Edge e = v.getEdge(); e != null; e = e.getNext()){
                System.out.print("-> " + e.getTo().getName() + " ");
            }
            System.out.println();
        });
    }

    
    
    public void addNode(Node nodeA) {
        nodes.put(nodeA.getName(), nodeA);
    }

    // getters and setters 

    public int getNumberOfNodes(){
        return this.numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes){
        this.numberOfNodes = numberOfNodes;
    }

    public int getNumberOfEdges(){
        return this.numberOfEdges;
    }

    public void setNumberOfEdges(int numberOfEdges){
        this.numberOfEdges = numberOfEdges;
    }

    public HashMap<Integer, Node> getNodes(){
        return this.nodes;
    }
    
    public void setNodes(HashMap<Integer, Node> nodes){
        this.nodes = nodes;
    }

    public Node getSourceNode(){
        return this.sourceNode;
    }

    public void setSourceNode(Node sourceNode){
        this.sourceNode = sourceNode;
    }

    public Node getDestinationNode(){
        return this.destinationNode;
    }

    public void setDestinationNode(Node destinationNode){
        this.destinationNode = destinationNode;
    }

    private double[] parseLine(String line){
        List<String> filteredTokens = filterEmptyTokens(line);
        
        double[] parsedLine = new double[filteredTokens.size()];

        for (int i = 0; i < filteredTokens.size(); i++) {
            try {
                parsedLine[i] = Double.parseDouble(filteredTokens.get(i));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format, skipping: " + filteredTokens.get(i));
                return null;
            }
        }
        return parsedLine;
    }
    private List<String> filterEmptyTokens(String line) {
        String[] tokens = line.split("\\s+");
        List<String> nonEmptyTokens = new ArrayList<>();
        for (String token : tokens) {
            if (!token.isEmpty()) {
                nonEmptyTokens.add(token);
            }
        }
        return nonEmptyTokens;
    }
    public List<double[]> read_from_file(String path) {
        
        List<double[]> list = new ArrayList<>();
        try {
            BufferedReader b = new BufferedReader(new FileReader(path));
            String line;
            while ((line = b.readLine()) != null) {
                double[] parsedLine = parseLine(line);
                
                list.add(parsedLine);
            }
            b.close();
        } catch (FileNotFoundException f){
            System.out.println("File not found: " + f.getMessage());
        } catch (IOException e) {
            System.out.println("An error occured: " + e.getMessage());
        }
        return list;
    }

    public List<InterestPoint> readInterestPoints(String path) {
        List<InterestPoint> interestPoints = new ArrayList<>();

        try (BufferedReader b = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = b.readLine()) != null) {
                List<String> filteredTokens = filterEmptyTokens(line);

                if (filteredTokens.size() >= 3) {
                    int nodeNumber = Integer.parseInt(filteredTokens.get(0));
                    int code = Integer.parseInt(filteredTokens.get(1));
                    String name = filteredTokens.get(2).replace("\"", ""); // Fjern anførselstegn

                    InterestPoint interestPoint = (InterestPoint) this.nodes.get(nodeNumber);
                    interestPoint.setType((byte) code);
                    interestPoint.setDescription(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return interestPoints;
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        List<double[]> nodeList = graph.read_from_file("test-noder.txt");
        List<double[]> edgeList = graph.read_from_file("test-kanter.txt");

        graph.init_graph(nodeList, edgeList);
        System.out.println("Graph initialized");
        System.out.println("Total nodes: " + graph.getNodes().size());
        System.out.println("\n ------------- \n");
        Node source;
        Node destination;
        java.util.Scanner scanner = new java.util.Scanner(System.in);
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
                graph.dijkstra(source);
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
                destination = graph.getNodes().get(Integer.parseInt(input));
                System.out.println("Destination Node: " + destination.getName());
                if (destination.getShortestPath().size() == 0) {
                    System.out.println("No path found");
                } else {
                    System.out.println("Shortest path: ");
                    for (Node n : destination.getShortestPath()) {
                        System.out.print(n.getName() + " ");
                    }
                    System.out.println();
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }
        }
        scanner.close();
    }
}