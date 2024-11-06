import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {
    private int numberOfNodes;
    private int numberOfEdges;
    private Set<Node> nodes = new HashSet<>();
    public Graph(){};
    
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

    public Graph read_from_file(String path) {
        String[] temp;
        List<int[]> list = new ArrayList<>();
        try {
            BufferedReader b = new BufferedReader(new FileReader(path));
            String text;
            while ((text = b.readLine()) != null) {
                temp = text.split(" ");
                int[] res = new int[2];
                boolean first = true;
                for (String s : temp){
                    if (!s.equals("")){
                        if (first){
                            res[0] = Integer.parseInt(s);
                            first = false;
                        } else {
                            res[1] = Integer.parseInt(s);
                        }
                    }
                }
                list.add(res);
            }
            b.close();
        } catch (FileNotFoundException f){
            System.out.println("File not found: " + f.getMessage());
        } catch (IOException e) {
            System.out.println("An error occured: " + e.getMessage());
        }
        return this;
    }
}