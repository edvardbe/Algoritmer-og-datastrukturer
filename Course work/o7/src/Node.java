import java.util.List;
import java.util.LinkedList;

public class Node {

    private Vector vector;

    private Pre data;

    private int name;

    private int time = 0;
    
    private List<Node> shortestPath = new LinkedList<>();
    
    private Edge edge;

    private int distance = Integer.MAX_VALUE;

    private boolean visited = false;

    private boolean isInterestPoint = false;
    
     
    public Node(int name) {
        this.name = name;
    }

    public Node(int name, double latitude, double longitude) {
        this.name = name;
        this.vector = new Vector(latitude, longitude);
        this.data = new Pre();
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

    public Pre getData(){
        return this.data;
    }

    public void setData(Pre data){
        this.data = data;
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

    public int getTime(){
        return this.time;
    }

    public void setTime(int time){
        this.time = time;
    }

    public boolean isInterestPoint(){
        return this.isInterestPoint;
    }

    public void setIsInterestPoint(boolean isInterestPoint){
        this.isInterestPoint = isInterestPoint;
    }

    @Override
    public String toString() {
        return name + "(" + vector.getLatitude() + ", " + vector.getLongitude() + ")";
    }
}