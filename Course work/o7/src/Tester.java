import java.util.Date;
import java.util.List;
import java.util.Map;

public class Tester {
    public static void main(String[] args) {
        Graph graph = new Graph();

        List<double[]> nodeList = graph.read_from_file("island/noder.txt");
        List<double[]> edgeList = graph.read_from_file("island/kanter.txt");
        Map<Integer, InterestPoint> interestPoints = graph.read_interest_points("island/interessepkt.txt");

        System.out.println("Graph initialized");
        System.out.println("\n ------------- \n");
        Node source;
        Node destination;
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (true) {
            graph = new Graph();
            graph.init_graph(nodeList, edgeList, interestPoints);
            System.out.println("\n ------------- \n");
            System.out.println("Press 'x' to exit, 'm' to make landmark files");
            System.out.println("Select source node: ");
            try {
                String input = scanner.nextLine();
                if (input.equals("x")) {
                    break;
                } else if(input.equals("m")) {
                    graph.makeLandmarks(new int[]{1432, 50010, 109221}, "fra-landemerker.txt");
                    Graph inverse = graph;
                    inverse.inverse_graph();
                    inverse.makeLandmarks(new int[]{1432, 50010, 109221}, "til-landemerker.txt");
                    continue;
                } 
                source = graph.getNodes()[Integer.parseInt(input)];
                graph.setSourceNode(source);
                System.out.println("Source Node: " + source);
            } catch (Exception e) {
                System.out.println("Invalid input");
                e.printStackTrace();
                continue;
            }

            try {
                System.out.println("\n ------------- \n");
                System.out.println("Press 's' for shortest path, 'i' for interest points, 'x' to exit");
                String input = scanner.nextLine();
                if(input.equals("x")){
                    break;
                } else if(input.equals("i")){
                    System.out.println("Select interest point type: ");
                    System.out.println("1: Stedsnavn");
                    System.out.println("2: Bensinstasjon");
                    System.out.println("4: Ladestasjon");
                    System.out.println("8: spisested");
                    System.out.println("16: drikkested");
                    System.out.println("32: overnattingssted");
                    input = scanner.nextLine();
                    Node[] closestInterestPoints = graph.find_closest_interestpoints(source, Integer.parseInt(input), graph.getNodes());
                    for (Node node : closestInterestPoints) {
                        if (node != null) {
                            System.out.println("Interest point: " + node.getId());
                        }
                    }
                    continue;
                } 
                else if (!input.equals("s")) {
                    System.out.println("Invalid input");
                    continue;
                }

                System.out.println("Select destination node: ");
                input = scanner.nextLine();
                destination = graph.getNodes()[Integer.parseInt(input)];
                System.out.println("Press 'd' or 'a', x to exit");
                input = scanner.nextLine();
                if (input.equals("x")) {
                    break;
                } else if (!input.equals("d") && !input.equals("a")) {
                    System.out.println("Invalid input");
                    continue;
                }
                Date tid1 = new Date();
                graph.setDestinationNode(destination);
                String tur = "Kjøretur " + source.getId() + " — " + destination.getId() + ": ";
                String alg = "Algoritme: ";
                if (input.equals("d")) {
                    graph.dijkstra(source, destination);
                    alg = alg + "Dijkstra. ";                  
                } else if (input.equals("a")) {
                    graph.alt(source, destination);
                    alg = alg + "ALT. ";                  
                }
                Date tid2 = new Date();
                System.out.println("Destination Node: " + destination.getId());
                int count = 0;
                System.out.println("Shortest path: ");
                Node node = destination;
                while (node != null) {
/*                     System.out.print(" ->  " + node.getId());
 */                    node = (Node) ((Pre) node.getData()).get_pre();
                    count++;
                }
                System.out.println();
                if (graph.getDestinationNode().getData() == null || count == 1) {
                    System.out.println("No path found");
                } else {
                    int tid = graph.getDestinationNode().getData().get_time();
                    System.out.println("Time: " + tid);
                    int tt = tid / 360000; tid -= 360000 * tt;
                    int mm = tid / 6000; tid -= 6000 * mm;
                    int ss = tid / 100;
                    int hs = tid % 100;
                    tur = String.format("%s Kjøretid %d:%02d:%02d,%02d   (tid: %d)", tur, tt, mm, ss, hs, tid);
                }
                float sek = (float)(tid2.getTime() - tid1.getTime()) / 1000;
                alg = String.format("%s prosesserte %,d noder på %2.3fs. %2.0f noder/ms", alg, count, sek, count/sek/1000);
                System.out.println(tur);
                System.out.println(alg);
                System.out.println();
                
            } catch (Exception e) {
                System.out.println("Invalid input");
                e.printStackTrace();
                continue;
            }
        }
        scanner.close();
    }
}
