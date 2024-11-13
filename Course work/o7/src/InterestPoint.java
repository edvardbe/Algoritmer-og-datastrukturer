public class InterestPoint extends Node {
    public enum Type {
        RESTAURANT, MUSEUM, PARK, SHOPPING, HISTORIC, OTHER
    }
    private byte type;
    private String description;
    public InterestPoint(int name, double latitude, double longitude, byte type, String description) {
        super(name, latitude, longitude);
        this.type = type;
        this.description = description;
    }

    public InterestPoint(Node node, byte type, String description) {
        super(node.getName(), node.getVector().getLatitude(), node.getVector().getLongitude());
        this.type = type;
        this.description = description;
    }
    public InterestPoint(int nodeNumber, byte type, String description) {
        super(nodeNumber);
        this.type = type;
        this.description = description;
    }

    public byte getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
