import java.util.HashMap;

public class Pre {
    int depth;
    int fullDepth;
    int distanceToEnd;
    Node pre;
    static int inf = 1_000_000_000;
    public Pre(){
        depth = inf;
        fullDepth = inf;
        distanceToEnd = -1;
    }

    public int get_depth() { 
        return depth;
    }
    public Node get_pre() {
        return pre;
    }
    public int get_fullDepth() {
        return fullDepth;
    }

    public int getDistanceToEnd() {
        return this.distanceToEnd;
    }
    
}
