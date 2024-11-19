import java.util.List;
import java.util.LinkedList;

public class Node {
    private final static int inf = Integer.MAX_VALUE;

    private Vector vector;

    private Node from;

    private int id;

    private int time = inf;

    private int distance = inf;

    private int full_time = inf;

    private int time_to_end = inf;
    
    private List<Node> shortestPath = new LinkedList<>();
    
    private Edge edge;


    private boolean visited = false;

    private boolean isInterestPoint = false;
    
     
    public Node(int id) {
        this.id = id;
    }

    public Node(int id, double latitude, double longitude) {
        this.id = id;
        this.vector = new Vector(latitude, longitude);
        this.from = null;
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

    public int getId(){
        return this.id;
    }
    public List<Node> getShortestPath(){
        return this.shortestPath;
    }

    public Node getFrom(){
        return this.from;
    }

    public void setFrom(Node from){
        this.from = from;
    }

    public void setId(int id){
        this.id = id;
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

    public int getFullTime(){
        return this.full_time;
    }

    public void setFullTime(int full_time){
        this.full_time = full_time;
    }

    public int getTimeToEnd(){
        return this.time_to_end;
    }

    public void setTimeToEnd(int time_to_end){
        this.time_to_end = time_to_end;
    }

    public boolean isInterestPoint(){
        return this.isInterestPoint;
    }

    public void setIsInterestPoint(boolean isInterestPoint){
        this.isInterestPoint = isInterestPoint;
    }

    @Override
    public String toString() {
        return id + "(" + vector.getLatitude() + ", " + vector.getLongitude() + ")";
    }
}