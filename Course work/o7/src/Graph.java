import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Graph {
    private Node sourceNode;
    private Node destinationNode;
    private int numberOfNodes;
    private int numberOfEdges;
    private int numberOfLandmarks;
    private int numberOfProcessed = 0;
    private PriorityQueue<Node> pq;
    private HashMap<Integer, Node> nodes;
        public Graph(){};


        public void dijkstra(Node source, Node destination){
            this.numberOfProcessed = 0;
            pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a.getData().get_time()));
            ((Pre) source.getData()).time = 0;
            pq.add((Node) source);
            while (!pq.isEmpty()){
                Node n = pq.poll();
                numberOfProcessed++;
                if (n.equals(destination)){
                    break;
                }
                for (Edge e = n.getEdge(); e != null; e = e.getNext()){
                    shorten_dijk(n, e, pq);
                }
            }
        }
    
        public void alt(Node source, Node destination){
            this.numberOfProcessed = 0;
            pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a.getData().get_full_time() - a.getData().get_time()));
            ((Pre) source.getData()).time = 0;
            pq.add((Node) source);
            while (!pq.isEmpty()){
                numberOfProcessed++;
                Node n = pq.poll();
                if (n.equals(destination)){
                    break;
                }
                for (Edge e = n.getEdge(); e != null; e = e.getNext()){
                    shorten_alt(n, e, pq, destination);
                }
            }
        }
        
    
        public InterestPoint[] find_closest_interestpoints(Node source, int interest_point_code, HashMap<Integer, Node> nodes){
            pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a.getData().get_time()));
            InterestPoint[] closestInterestPoints = new InterestPoint[4];
            Set<Node> visitedNodes = new HashSet<>();
            int count = 0;
            // Reset all nodes
        
    
            // Initialize distances
            /* sourceNode.setDistance(0);
            sourceNode.setTime(0); */
            sourceNode.getData().time = 0;
    
            pq.add(sourceNode);
            while (!pq.isEmpty() && count < 4) {
                Node currentNode = pq.poll();
    
                if (visitedNodes.contains(currentNode)) {
                    continue;
                }
                visitedNodes.add(currentNode);
                if (currentNode.isInterestPoint() && !currentNode.equals(sourceNode)) {
                    InterestPoint interestPoint = (InterestPoint) currentNode;
                    if ((interestPoint.getType() & interest_point_code) != 0) {
                        closestInterestPoints[count] = interestPoint;
                        count++;
                    }
                }
                for (Edge e = currentNode.getEdge(); e != null; e = e.getNext()) {
                    shorten_dijk(currentNode, e, pq);
                }
            }
            return closestInterestPoints;
        }
    
    
        public void shorten_dijk(Node node, Edge edge, PriorityQueue<Node> pq){
            Pre pre = node.getData(), edgeEnd = edge.getTo().getData();
            int new_time = pre.get_time() + edge.getDriveTime();
            if (new_time < edgeEnd.get_time()){
                edgeEnd.time = new_time;
                edgeEnd.pre = node;
                edge.getTo().setDistance(node.getDistance() + edge.getDistance());
                edge.getTo().setTime(node.getTime() + edge.getDriveTime());
                pq.add(edge.getTo());
            }
        }
    
        public void shorten_alt(Node node, Edge edge, PriorityQueue<Node> pq, Node destination){
            Pre pre = node.getData(), edgeEnd = edge.getTo().getData();
            int new_time = pre.get_time() + edge.getDriveTime();
            System.out.println("New time: " + new_time);
            if (new_time < edgeEnd.get_time()){
                System.out.println("New time is less than edgeEnd time");
                if(edgeEnd.time_to_end == -1){
                    edgeEnd.time_to_end = (int) Math.round(euclideanDistance(edge.getTo(), destination));
                }
                edgeEnd.time = new_time;
                edgeEnd.pre = node;
                edge.getTo().setDistance(node.getDistance() + edge.getDistance());
                edge.getTo().setTime(node.getTime() + edge.getDriveTime());
                edgeEnd.full_time = edgeEnd.time + edgeEnd.time_to_end;
                pq.add(edge.getTo());
            }
        }
    
        public static double euclideanDistance(Node source, Node destination){
            return Math.sqrt(Math.pow(source.getVector().getLatitude() - destination.getVector().getLatitude(), 2) + Math.pow(source.getVector().getLongitude() - destination.getVector().getLongitude(), 2));
        }
    
    
        /**
         * Initiates a graph using the {@link #read_from_file(String path)}
         * method. Initiates N, amount of nodes, and E, amount of edges,
         * and the array of nodes, nodes.
         * @param path
         */
        public void init_graph(List<double[]> nodeList, List<double[]> edgeList, Map<Integer, InterestPoint> interestPoints){
        
            this.numberOfNodes = (int) nodeList.get(0)[0];
            this.numberOfEdges = (int) edgeList.get(0)[0];
            this.nodes = new HashMap<>();
            for (int i = 1; i < numberOfNodes + 1; i++) {
                int key = (int) nodeList.get(i)[0];
                double latitude = nodeList.get(i)[1] * Math.PI / 180;
                double longitude = nodeList.get(i)[2] * Math.PI / 180;
                if(interestPoints.containsKey(key)) {
                    InterestPoint interestPoint = interestPoints.get(key);
                    nodes.put(key, new InterestPoint(key, latitude, longitude, interestPoint.getType(), interestPoint.getDescription()));
                    nodes.get(key).setIsInterestPoint(true);;
                } else {
                    nodes.put(key, new Node(key, latitude, longitude));
                }
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

    public int getNumberOfProcessed(){
        return numberOfProcessed;
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

    public Map<Integer, InterestPoint> read_interest_points(String path) {
        Map<Integer, InterestPoint> interestPoints = new HashMap<>();

        try (BufferedReader b = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = b.readLine()) != null) {
                List<String> filteredTokens = filterEmptyTokens(line);

                if (filteredTokens.size() >= 3) {
                    int nodeNumber = Integer.parseInt(filteredTokens.get(0));
                    int code = Integer.parseInt(filteredTokens.get(1));
                    String name = filteredTokens.get(2).replace("\"", ""); // Fjern anførselstegn
                    
                    InterestPoint interestPoint = new InterestPoint(nodeNumber, code, name);
                    interestPoints.put(nodeNumber, interestPoint);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return interestPoints;
    }
    

    public void reset(){
        for (Node n : nodes.values()){
            n.setData(new Pre());
            n.setTime(0);
            n.setDistance(0);
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        List<double[]> nodeList = graph.read_from_file("noder.txt");
        List<double[]> edgeList = graph.read_from_file("kanter.txt");
        Map<Integer, InterestPoint> interestPoints = graph.read_interest_points("interessepkt.txt");

        System.out.println("Graph initialized");
        System.out.println("\n ------------- \n");
        Node source;
        Node destination;
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (true) {
            graph = new Graph();
            graph.init_graph(nodeList, edgeList, interestPoints);
            System.out.println("\n ------------- \n");
            System.out.println("Press 'x' to exit, 'm' to make landmark files");
            System.out.println("Select source node: ");
            try {
                String input = scanner.nextLine();
                if (input.equals("x")) {
                    break;
                } else if(input.equals("m")) {
                    graph.makeLandmarkFiles();
                    continue;
                } 
                source = graph.getNodes().get(Integer.parseInt(input));
                graph.setSourceNode(source);
                System.out.println("Source Node: " + source);
            } catch (Exception e) {
                System.out.println("Invalid input");
                e.printStackTrace();
                continue;
            }

            try {
                System.out.println("\n ------------- \n");
                System.out.println("Press 's' for shortest path, 'i' for interest points, 'x' to exit");
                String input = scanner.nextLine();
                if(input.equals("x")){
                    break;
                } else if(input.equals("i")){
                    System.out.println("Select interest point type: ");
                    System.out.println("1: Stedsnavn");
                    System.out.println("2: Bensinstasjon");
                    System.out.println("4: Ladestasjon");
                    System.out.println("8: spisested");
                    System.out.println("16: drikkested");
                    System.out.println("32: overnattingssted");
                    input = scanner.nextLine();
                    Node[] closestInterestPoints = graph.find_closest_interestpoints(source, Integer.parseInt(input), graph.getNodes());
                    for (Node node : closestInterestPoints) {
                        if (node != null) {
                            System.out.println("Interest point: " + node.getName());
                        }
                    }
                    continue;
                } 
                else if (!input.equals("s")) {
                    System.out.println("Invalid input");
                    continue;
                }

                System.out.println("Select destination node: ");
                input = scanner.nextLine();
                destination = graph.getNodes().get(Integer.parseInt(input));
                System.out.println("Press 'd' or 'a', x to exit");
                input = scanner.nextLine();
                if (input.equals("x")) {
                    break;
                } else if (!input.equals("d") && !input.equals("a")) {
                    System.out.println("Invalid input");
                    continue;
                }
                Date tid1 = new Date();
                graph.setDestinationNode(destination);
                String tur = "Kjøretur " + source.getName() + " — " + destination.getName() + ": ";
                String alg = "Algoritme: ";
                if (input.equals("d")) {
                    graph.dijkstra(source, destination);
                    alg = alg + "Dijkstra. ";                  
                } else if (input.equals("a")) {
                    graph.alt(source, destination);
                    alg = alg + "ALT. ";                  
                }
                Date tid2 = new Date();
                System.out.println("Destination Node: " + destination.getName());
                int count = 0;
                System.out.println("Shortest path: ");
                Node node = destination;
                while (node != null) {
/*                     System.out.print(" ->  " + node.getName());
 */                    node = (Node) ((Pre) node.getData()).get_pre();
                    count++;
                }
                System.out.println();
                if (graph.getDestinationNode().getData() == null || count == 1) {
                    System.out.println("No path found");
                } else {
                    int tid = graph.getDestinationNode().getTime();
                    int tt = tid / 360000; tid -= 360000 * tt;
                    int mm = tid / 6000; tid -= 6000 * mm;
                    int ss = tid / 100;
                    int hs = tid % 100;
                    tur = String.format("%s Kjøretid %d:%02d:%02d,%02d   ()", tur, tt, mm, ss, hs);
                }
                float sek = (float)(tid2.getTime() - tid1.getTime()) / 1000;
                alg = String.format("%s prosesserte %,d noder på %2.3fs. %2.0f noder/ms", alg, count, sek, count/sek/1000);
                System.out.println(tur);
                System.out.println(alg);
                System.out.println();
                
            } catch (Exception e) {
                System.out.println("Invalid input");
                e.printStackTrace();
                continue;
            }
        }
        scanner.close();
    }

    public void makeLandmarkFiles(){
        Map<String, Node> landmarks = new HashMap<>();
        landmarks.put("MOHKO", nodes.get(478452));
        /* landmarks.put("BERGEN", nodes.get(5542364));
        landmarks.put("NØRREMØLLE", nodes.get(1361309));
        landmarks.put("NORDKAPP", nodes.get(2531818)); */
        List<int[]> landmark_data = new ArrayList<>();
        try {
            FileOutputStream fraOut = new FileOutputStream("tillandemarker.txt");
            Node sourceNode = nodes.get(0);
            Node lastNode = nodes.get(nodes.size() - 1);
            this.dijkstra(sourceNode, lastNode);
            for (Node node : nodes.values()){
                while(node.getData().get_pre() != null){
                    node = (Node) node.getData().get_pre();
                }
                System.out.println("Node: " + node.getName() + " Landmark data: " + node.getData().get_time());
            }
            
            fraOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}