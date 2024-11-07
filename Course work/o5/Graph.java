import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Graph {

    class Edge {
    Edge next;
    Node to;
        public Edge(Node to, Edge next){
            this.to = to;
            this.next = next;
        }
    }

    class Node {
        Edge edge;
        Object data;
        int id;
        boolean visited = false;

        public Node(int id){
            this.id = id;
        }

    }

    int N, E;
    Node[] nodes;
    public Graph(){};
    class Pre {
        int depth;
        Node pre;
        static int inf = 1_000_000_000;
        public Pre(){
            depth = inf;
        }

        public int get_depth() { 
            return depth;
        }
        public Node get_pre() {
            return pre;
        }
        public void init_pre(Node s){
            for (int i = N; i-- > 0;){
                nodes[i].data = new Pre();
            }
            ((Pre) s.data).depth = 0;
        }

    }
    class DFS_Pre extends Pre {
        int found_time, finished_time;
        static int time;
        static void reset_time(){
            time = 0;
        }
        static int read_time() {
            return ++time;
        }
        public void DFS_init() {
            for (int i = 0; i < N; i++){
                nodes[i].data = new DFS_Pre();
            }
            DFS_Pre.reset_time();
        }
        
        public List<Node> DFS_Search(Node search_node) {
            List<Node> sccNodes = new ArrayList<>();
            DFS_Pre n_pre = (DFS_Pre) search_node.data;
            n_pre.found_time = DFS_Pre.read_time();
            search_node.visited = true;
            sccNodes.add(search_node);  // Add current node to the SCC list
    
            for (Edge e = search_node.edge; e != null; e = e.next) {
                if (!e.to.visited) {
                    DFS_Pre m_pre = (DFS_Pre) e.to.data;
                    if (m_pre.found_time == 0) {
                        m_pre.pre = search_node;
                        m_pre.depth = n_pre.depth + 1;
                        sccNodes.addAll(DFS_Search(e.to));  // Recursively collect nodes in SCC
                    }
                }
            }
            n_pre.finished_time = DFS_Pre.read_time();
            return sccNodes;  // Return all nodes in this SCC
        }
    

        public void DFS(Node s){
            DFS_init();
            ((DFS_Pre)s.data).depth = 0;
            DFS_Search(s);
        }

        public List<Node> get_nodes_sorted_by_finished_time() {
            List<Node> nodeList = new ArrayList<>();
            
            // Collect all nodes into the list
            for (Node node : nodes) {
                nodeList.add(node);
            }

            // Sort the nodes by their finished time
            nodeList.sort((n1, n2) -> {
                int t1 = ((DFS_Pre) n1.data).finished_time;
                int t2 = ((DFS_Pre) n2.data).finished_time;
                return Integer.compare(t2, t1);
            });

            return nodeList;  // Return the sorted list of nodes
        }
    }

    /**
     * Initiates a graph using the {@link #read_from_file(String path)}
     * method. Initiates N, amount of nodes, and E, amount of edges,
     * and the array of nodes, nodes.
     * @param path
     */
    public void init_graph(List<int[]> list){
        N = list.get(0)[0];
        E = list.get(0)[1];
        this.nodes = new Node[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 1; i < list.size(); i++){
            int from = list.get(i)[0];
            int to = list.get(i)[1];
            Edge e = new Edge(nodes[to], nodes[from].edge);
            nodes[from].edge = e;
        }
    }

    /**
     * Reads graph from file where each line 
     * contains a two adjacent node. 
     * The first and second integer in the first line is 
     * the number of nodes, and number of edges respectively,
     * but this is handeled in the {@link #init_graph(String path)}
     * 
     * @param path
     * @return A list of integer arrays with length 2
     */
    
    private static List<int[]> read_from_file(String path){
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
        return list;
    }
        
    public List<int[]> transpose_graph(List<int[]> list){
        List<int[]> result = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            int[] temp = new int[2];
            temp[0] = list.get(i)[1];
            temp[1] = list.get(i)[0];
            if(i == 0){
                temp = list.get(i);
            }
            result.add(temp);
        }
        return result;
    }

    public void reset_visited() {
        for (Node node : nodes) {
            node.visited = false;
        }
    }
    

    /**
     * Prints a graph with each node and their adjacent nodes
     * 
     */ 
    public void print_graph(){
        for (int i = 0; i < N; i++){
            System.out.print(i + ": ");
            for (Edge e = nodes[i].edge; e != null; e = e.next){
                System.out.print("-> " + e.to.id + " ");
            }
            System.out.println();
        }
    }
    public void print_scc(){
        for (int i = 0; i < N; i++){
            System.out.print(i + ": ");
            for (Edge e = nodes[i].edge; e != null; e = e.next){
                if(!e.to.visited){
                    System.out.print("-> " + e.to.id + " ");
                }
            }
            System.out.println();
        }
    }

    

    public static void main(String[] args) {
        Graph graph = new Graph();
        List<int[]> list = read_from_file("ø5g2.txt");
        graph.init_graph(list);

        DFS_Pre dfsPre = graph.new DFS_Pre();
        dfsPre.DFS_init();

        for (Node node : graph.nodes) {
            if(!node.visited){
                dfsPre.DFS_Search(node);
            }
        }
        
        // Get the nodes sorted by finished time (in descending order)
        List<Node> time_nodes = dfsPre.get_nodes_sorted_by_finished_time();

        // Transpose the graph
        
        List<int[]> transposedList = graph.transpose_graph(list);
        graph.init_graph(transposedList);

        // Reset visited for all nodes in the transposed graph
        graph.reset_visited();

        // Initialize DFS_Pre for the transposed graph
        dfsPre.DFS_init();

        System.out.println("Strongly Connected Components:");

        int sccIndex = 1;
        // Perform DFS on transposed graph in the order of decreasing finished time
        for (Node node : time_nodes) {
            if (!graph.nodes[node.id].visited) {
                List<Node> scc = dfsPre.DFS_Search(graph.nodes[node.id]);  // Get all nodes in this SCC
                // Print SCC index and nodes
                System.out.print("SCC nr " + sccIndex + ": ");
                for (Node sccNode : scc) {
                    System.out.print(sccNode.id + " ");
                }
                System.out.println();  // Newline after each SCC
                sccIndex++;
            }
        }
    }

}
