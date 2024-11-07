class Edge {
    private Edge next;
    private Node to;
    private int driveTime;
    private int distance;
    private int speedLimit;

    public Edge(Node to, Edge next){
        this.to = to;
        this.next = next;
    }

    public Edge(Node to, Edge next, int driveTime, int distance, int speedLimit){
        this.to = to;
        this.next = next;
        this.driveTime = driveTime;
        this.distance = distance;
        this.speedLimit = speedLimit;
    }

    public Edge getNext(){
        return this.next;
    }
    
    public void setNext(Edge next){
        this.next = next;
    }

    public Node getTo(){
        return this.to;
    }

    public void setTo(Node to){
        this.to = to;
    }

    public int getDriveTime(){
        return this.driveTime;
    }

    public void setDriveTime(int driveTime){
        this.driveTime = driveTime;
    }

    public int getDistance(){
        return this.distance;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public int getSpeedLimit(){
        return this.speedLimit;
    }

    public void setSpeedLimit(int speedLimit){
        this.speedLimit = speedLimit;
    }
}
