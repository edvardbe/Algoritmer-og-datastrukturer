import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class Node {
    
    private Object data;

    private String name;
    
    private List<Node> shortestPath = new LinkedList<>();
    
    private Integer distance = Integer.MAX_VALUE;

    private Edge edge;

    private boolean visited = false;

    
    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }
 
    public Node(String name) {
        this.name = name;
    }
    // getters and setters

    public Map<Node, Integer> getAdjacentNodes(){
        return this.adjacentNodes;
    }
    public void setAdjacentNodes(Map<Node, Integer> adjacentNodes){
        this.adjacentNodes = adjacentNodes;
    }
    public String getName(){
        return this.name;
    }
    public List<Node> getShortestPath(){
        return this.shortestPath;
    }
    public Integer getDistance(){
        return this.distance;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setShortestPath(List<Node> shortestPath){
        this.shortestPath = shortestPath;
    }
    public void setDistance(Integer distance){
        this.distance = distance;
    }
}