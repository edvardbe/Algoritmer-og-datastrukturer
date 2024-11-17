import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
    private List<int[]> from_landmarks;
    private List<int[]> to_landmarks;
    private int numberOfProcessed = 0;
    private PriorityQueue<Node> pq;
    private Node[] nodes;
    public Graph(){};


    public void dijkstra(Node source, Node destination){
        this.numberOfProcessed = 0;
        pq = create_dijkstra_queue();
        source.getData().time = 0;
        pq.add(source);
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

    private void dijkstra_all_nodes(Node source){
        this.numberOfProcessed = 0;
        pq = create_dijkstra_queue();
        source.getData().time = 0;
        pq.add(source);
        while (!pq.isEmpty()){
            Node n = pq.poll();
            numberOfProcessed++;
            for (Edge e = n.getEdge(); e != null; e = e.getNext()){
                shorten_dijk(n, e, pq);
            }
        }
    }

    

    public void alt(Node source, Node destination){
        this.numberOfProcessed = 0;
        
        boolean from = false;
        
        List<int[]> landmarks = from ? from_landmarks : to_landmarks;

        pq = create_alt_queue(numberOfLandmarks, numberOfEdges, landmarks);
        source.getData().time = 0;
        pq.add(source);
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

    private PriorityQueue<Node> create_dijkstra_queue(){
        return new PriorityQueue<>(Comparator.comparingDouble(a -> a.getData().get_time()));
    }
    
    private PriorityQueue<Node> create_alt_queue(int landmarkId, int destinationId , List<int[]> landmarks){
        return new PriorityQueue<>(Comparator.comparingDouble(a -> landmarks.get(landmarkId)[destinationId] - landmarks.get(a.getId())[landmarkId]));
    }
    

    public InterestPoint[] find_closest_interestpoints(Node source, int interest_point_code, Node[] nodes){
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
        this.nodes = new Node[numberOfNodes];
        for (int i = 1; i < numberOfNodes + 1; i++) {
            int key = (int) nodeList.get(i)[0];
            double latitude = nodeList.get(i)[1] * Math.PI / 180;
            double longitude = nodeList.get(i)[2] * Math.PI / 180;
            if(interestPoints.containsKey(key)) {
                InterestPoint interestPoint = interestPoints.get(key);
                nodes[key] = (Node) new InterestPoint(key, latitude, longitude, interestPoint.getType(), interestPoint.getDescription());
                ((InterestPoint) nodes[key]).setIsInterestPoint(true);
            } else {
                nodes[key] = new Node(key, latitude, longitude);
            }
        }

        for (int i = 1; i < numberOfEdges + 1; i++){
            int from = (int) edgeList.get(i)[0];
            int to = (int) edgeList.get(i)[1];

            Node toNode = nodes[to];
            Edge fromEdge = nodes[from].getEdge();
            int driveTime = (int) edgeList.get(i)[2];
            int distance = (int) edgeList.get(i)[3];
            int speedLimit = (int) edgeList.get(i)[4];
            Edge e = new Edge(toNode, fromEdge, driveTime, distance, speedLimit);
            nodes[from].setEdge(e);
        }
    }

    public void init_graph(List<double[]> nodeList, List<double[]> edgeList, Map<Integer, InterestPoint> interestPoints, List<int[]> from_landmarks, List<int[]> to_landmarks){
        this.numberOfLandmarks = from_landmarks.size();
        this.from_landmarks = from_landmarks;
        this.to_landmarks = to_landmarks;
        init_graph(nodeList, edgeList, interestPoints);
    }

    public void inverse_graph() {
        // Create an array to temporarily store the reversed edges
        List<Edge>[] reversedEdges = new ArrayList[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            reversedEdges[i] = new ArrayList<>();
        }
    
        // Traverse all nodes and reverse their edges
        for (int from = 0; from < numberOfNodes; from++) {
            Node currentNode = nodes[from];
            if (currentNode != null) {
                Edge edge = currentNode.getEdge();
                while (edge != null) {
                    int to = edge.getTo().getId();
                    // Add the reversed edge to the appropriate node
                    reversedEdges[to].add(new Edge(currentNode, null, edge.getDriveTime(), edge.getDistance(), edge.getSpeedLimit()));
                    edge = edge.getNext();
                }
            }
        }
    
        // Clear the original edges and set the reversed edges
        for (int i = 0; i < numberOfNodes; i++) {
            Node currentNode = nodes[i];
            if (currentNode != null) {
                currentNode.setEdge(null); // Clear original edges
                for (Edge reversedEdge : reversedEdges[i]) {
                    reversedEdge.setNext(currentNode.getEdge());
                    currentNode.setEdge(reversedEdge);
                }
            }
        }
    }
    

    public void print_graph(){
        for (Node node : nodes) {
            System.out.println(node.getId() + ": ");
            for (Edge e = node.getEdge(); e != null; e = e.getNext()){
                System.out.print("-> " + e.getTo().getId() + " ");
            }
        }
    }

    
    
    public void addNode(Node nodeA) {
        nodes[nodeA.getId()] = nodeA;
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

    public Node[] getNodes(){
        return this.nodes;
    }
    
    public void setNodes(Node[] nodes){
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
    
    public void makeLandmarks(int[] landmarkIds, String path){
        /*  Map<String, Node> landmarks = new HashMap<>();
        landmarks.put("MOHKO", nodes[478452]);
        landmarks.put("BERGEN", nodes[5542364]);
        landmarks.put("NØRREMØLLE", nodes[1361309]);
        landmarks.put("NORDKAPP", nodes[2531818]);
*/
        numberOfLandmarks = landmarkIds.length;//landmarks.size();
        List<int[]> landmark_data = new ArrayList<>();
        try {
            FileOutputStream fraOut = new FileOutputStream(path);
            for (int id : landmarkIds){
                dijkstra_all_nodes(nodes[id]);
                int[] landmark_times = new int[nodes.length];
                for (int i = 0; i < nodes.length; i++){
                    int time = nodes[i].getData().get_time();
                    if (i == 0) System.out.println("Time after calculations: " + time);
                    landmark_times[i] = time;
                }
                landmark_data.add(landmark_times);
                reset();
            }
            for (int i = 0; i < nodes.length; i++){
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < numberOfLandmarks; j++){
                    int time = landmark_data.get(j)[i];
                    if(i == 0) System.out.println("Time in file: " + time);
                    sb.append(time);
                    sb.append(" ");
                }
                sb.append("\n");
                fraOut.write(sb.toString().getBytes());
            }
            fraOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
    }

    public void reset(){
        for (Node n : nodes){
            n.setData(new Pre());
            n.setTime(0);
            n.setDistance(0);
        }
    }
}