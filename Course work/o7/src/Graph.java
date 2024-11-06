import java.util.HashSet;
import java.util.Set;

public class Graph {

    private Set<Node> nodes = new HashSet<>();
    
    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    // getters and setters 

    public Set<Node> getNodes(){
        return this.nodes;
    }
    
    public void setNodes(Set<Node> nodes){
        this.nodes = nodes;
    }

    public void printGraph(){
        for(Node node : nodes){
            System.out.print(" Node: " + node.getName());
            for(Node destination : node.getAdjacentNodes().keySet()){
                System.out.print(" -> " + destination.getName());
            }
        }
    }
}