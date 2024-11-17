public class InterestPoint extends Node {
    public enum Type {
        STEDSNAVN, BENSINSTASJON, LADESTASJON, SPISESTED, DRIKKESTED, OVERNATTINGSSTED
    }
    private int type;
    private String description;
    public InterestPoint(int name, double latitude, double longitude, int type, String description) {
        super(name, latitude, longitude);
        this.type = type;
        this.description = description;
    }

    public InterestPoint(Node node, int type, String description) {
        super(node.getId(), node.getVector().getLatitude(), node.getVector().getLongitude());
        this.type = type;
        this.description = description;
    }
    public InterestPoint(int nodeNumber, int type, String description) {
        super(nodeNumber);
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "name: " + this.getId() + ", type: " + this.type + ", description: " + this.description;
    }
}
