public class Pre {
    int time;
    int distance;
    int full_time;
    int time_to_end;
    boolean visited = false;
    Node pre;
    static int inf = 1_000_000_000;
    public Pre(){
        time = inf;
        distance = inf;
        full_time = inf;
        time_to_end = inf;
        visited = false;
    }

    public int get_time() { 
        return time;
    }
    public Node get_pre() {
        return pre;
    }
    public int get_full_time() {
        return full_time;
    }

    public int get_ditance_to_end() {
        return time_to_end;
    }

    public int get_distance() {
        return time;
    }
    
}
