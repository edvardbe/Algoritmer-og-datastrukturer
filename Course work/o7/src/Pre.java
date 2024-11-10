import java.util.HashMap;

public class Pre {
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
    public void init_pre(Node s, int numberOfNodes, HashMap<Integer, Node> nodes){
        for (Node n : nodes.values()){
            Pre p = new Pre();
            n.setData(p);
        }
        ((Pre) s.getData()).depth = 0;
    }

}
