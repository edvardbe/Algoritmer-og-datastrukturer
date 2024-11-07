import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class Node {
    
    private Vector vector;

    private int name;
    
    private List<Node> shortestPath = new LinkedList<>();
    
    private Edge edge;

    private int distance = Integer.MAX_VALUE;

    private boolean visited = false;
 
    public Node(int name) {
        this.name = name;
    }

    public Node(int name, double latitude, double longitude) {
        this.name = name;
        this.vector = new Vector(latitude, longitude);
    }
    // getters and setters

    public Vector getVector(){
        return this.vector;
    }

    public void setVector(Vector vector){
        this.vector = vector;
    }

    public int getDistance(){
        return this.distance;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public int getName(){
        return this.name;
    }
    public List<Node> getShortestPath(){
        return this.shortestPath;
    }
    public void setName(int name){
        this.name = name;
    }
    public void setShortestPath(List<Node> shortestPath){
        this.shortestPath = shortestPath;
    }
    public void setEdge(Edge edge){
        this.edge = edge;
    }
    public Edge getEdge(){
        return this.edge;
    }
    public void setVisited(boolean visited){
        this.visited = visited;
    }
    public boolean getVisited(){
        return this.visited;
    }
    
}