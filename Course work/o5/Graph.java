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
    }

    int N, K;
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
            for (int i = N; i-- > 0;){
                nodes[i].data = new DFS_Pre();
            }
            DFS_Pre.reset_time();
        }
        public void DFS_Search(Node search_node) {
            DFS_Pre n_pre = (DFS_Pre) search_node.data;
            n_pre.found_time = DFS_Pre.read_time();
            for (Edge e = search_node.edge; e != null; e = e.next){
                DFS_Pre m_pre = (DFS_Pre) e.to.data;
                if(m_pre.found_time == 0) {
                    m_pre.pre = search_node;
                    m_pre.depth = n_pre.depth + 1;
                    DFS_Search(e.to);
                }
            }
            n_pre.finished_time = DFS_Pre.read_time();
        }

        public void DFS(Node s){
            DFS_init();
            ((DFS_Pre)s.data).depth = 0;
            DFS_Search(s);
        }
    }

    public void init_graph(String path){
        List<int[]> list = read_from_file(path);
        N = list.get(0)[0];
        K = list.get(0)[1];
        this.nodes = new Node[N];
        list.remove(0);
        for (int i = 0; i < N; i++) {
            nodes[i] = new Node();
        }

        for (int[] nums : list){
            int from = nums[0];
            int to = nums[1];
            Edge e = new Edge(nodes[to], nodes[from].edge);
            nodes[from].edge = e;
        }
    }
    
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

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.init_graph("ø5g2.txt");
        DFS_Pre dfsPre = graph.new DFS_Pre();
        dfsPre.DFS_init();

        // Start DFS from a specific node, for example, the first node
        Node startNode = graph.nodes[0];
        dfsPre.DFS_Search(startNode);

        // Optionally, you can print the DFS results
        for (Node node : graph.nodes) {
            DFS_Pre nodePre = (DFS_Pre) node.data;
            System.out.println("Found Time: " + nodePre.found_time + ", Finished Time: " + nodePre.finished_time);
        }
    }
}
