import java.util.Date;
import java.util.List;
import java.util.Map;

public class Tester {
    public static void main(String[] args) {
        Graph graph = new Graph();

        System.out.println("\n - Reading graph - ");
        System.out.println("\nThis may take a while...\n");

        List<double[]> nodeList = graph.read_from_file("noder.txt");
        List<double[]> edgeList = graph.read_from_file("kanter.txt");
        Map<Integer, InterestPoint> interestPoints = graph.read_interest_points("interessepkt.txt");
        System.out.println("----------------");
        System.out.println(" - Graph read - ");
        System.out.println("----------------");
        Node source;
        Node destination;
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        graph = new Graph();
        int[] landmarkIds = new int[]{478452, 2531818, 1361309, 5542364}; //{1432, 50010, 109221};
        graph.init_graph(nodeList, edgeList, interestPoints, landmarkIds);

        System.out.println("\n - Initializing graph - \n");


        System.out.println("-----------------------");
        System.out.println(" - Graph initialized - ");
        System.out.println("-----------------------");
        while (true) {
            graph.reset();
            System.out.println("Press 'x' to exit, 'm' to make landmark files");
            System.out.println("Select source node: ");
            try {
                String input = scanner.nextLine();
                if (input.equals("x")) {
                    break;
                } else if(input.equals("m")) {
                    System.out.println("This requires large amounts of memory and time.");
                    System.out.println("Are you sure you want to proceed? (y/n)");
                    input = scanner.nextLine();
                    if (!input.equals("y")) {
                        continue;
                    }
                    graph.make_landmarks(graph.getLandmarkIds(), "fra-landemerker.txt");
                    graph.readInverseEdges("kanter.txt");
                    graph.make_landmarks(graph.getLandmarkIds(), "til-landemerker.txt");
                    /* graph.make_landmarks(new int[]{478452, 2531818, 1361309, 5542364}, "fra-landemerker.txt");
                    Graph inverse = graph;
                    inverse.inverse_graph();
                    inverse.make_landmarks(new int[]{478452, 2531818, 1361309, 5542364}, "til-landemerker.txt"); */
                    graph.init_graph(nodeList, edgeList, interestPoints, landmarkIds);
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
                    Node[] closestInterestPoints = graph.find_closest_interestpoints(source, Integer.parseInt(input));
                    for (Node node : closestInterestPoints) {
                        if (node != null) {
                            int tid = node.getTime();
                            System.out.println("Time: " + tid);
                            int tt = tid / 360000; tid -= 360000 * tt;
                            int mm = tid / 6000; tid -= 6000 * mm;
                            int ss = tid / 100;
                            int hs = tid % 100;
                            String tur = String.format("Kjøretid %d:%02d:%02d,%02d  ", tt, mm, ss, hs);
                            System.out.println("Interest point: " + node + " | " + tur);
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
                    graph.dijkstra(source.getId(), destination.getId());
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
                    node = node.getFrom();
                    count++;
                }
                System.out.println();
                if (graph.getDestinationNode().getFrom() == null || count == 1) {
                    System.out.println("No path found");
                } else {
                    
                    int tid = graph.getDestinationNode().getTime();
                    System.out.println("Time: " + tid);
                    int tt = tid / 360000; tid -= 360000 * tt;
                    int mm = tid / 6000; tid -= 6000 * mm;
                    int ss = tid / 100;
                    int hs = tid % 100;
                    tur = String.format("%s Kjøretid %d:%02d:%02d,%02d   (tid: %d) (noder: %d)", tur, tt, mm, ss, hs, tid, count);
                }
                float sek = (float)(tid2.getTime() - tid1.getTime()) / 1000;
                alg = String.format("%s prosesserte %,d noder på %2.3fs. %2.0f noder/ms", alg, graph.getNumberOfProcessed(), sek, graph.getNumberOfProcessed()/sek/1000);
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
