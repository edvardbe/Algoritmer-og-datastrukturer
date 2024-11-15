/* Navi

	Skjelettprogram for veinavigasjon.
	Ferdig GUI for å legge inn nodenumre og vise kartfliser (tiles) fra Openstreetmap.

	Det som mangler: 
	* innlesing av graf fra fil
	* Dijkstras algoritme og ALT
	* plotte punkter på det grafiske kartet
	  - nodenes bredde- og lengdegrader kan brukes for plotting på kartet

  Kompilere:
	javac Navi.java -cp JMapViewer.jar

	Kjøre programmet:
	java -cp .:JMapViewer.jar Navi

	Eller sett opp CLASSPATH så java finner JMapViewer.jar uten hjelp.

 */

 import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;
 
 //GUI
 import javax.swing.*; //Vinduer
 import javax.swing.text.*;
 import javax.swing.event.*;
 import java.awt.*;
 import java.awt.event.*;
 
 
 //JMapViewer
 
 import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
 import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
 import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;
 import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;
 import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
 import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
 import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;
 
 import org.openstreetmap.gui.jmapviewer.*;
 
 
 
 class Graf {
   int N, K;
 
   /* Noder og kanter og sånt, Dijkstras algoritme, osv. */
 
 
 } //Graf slutt
 
 
 
 //GUI
 class vindu extends JPanel implements ActionListener, DocumentListener, JMapViewerEventListener {
     JButton btn_dijkstra = new JButton("Dijkstra");
     JButton btn_alt = new JButton("ALT");
     JButton btn_find_closest = new JButton("Find 4 Closest");
     JButton btn_slutt = new JButton("Avslutt");
     JLabel lbl_fra = new JLabel();
     JLabel lbl_til = new JLabel();
     JTextField txt_fra = new JTextField(30);
     JTextField txt_til = new JTextField(30);
     JLabel lbl_tur = new JLabel("—");
     JLabel lbl_alg = new JLabel("—");
     String gml_fra = "";
     String gml_til = "";
     JPanel kart = new JPanel(new BorderLayout());


     String[] interestPointTypes = {
        "1 - Stedsnavn",
        "2 - Bensinstasjon",
        "4 - Ladestasjon",
        "8 - Spisested",
        "16 - Drikkested",
        "32 - Overnattingsted"
    };

    JComboBox<String> interestPointDropdown = new JComboBox<>(interestPointTypes);
     //JMapViewer stuff
     private final JMapViewerTree treeMap;
     private final JLabel zoomLabel;
     private final JLabel zoomValue;
 
     private final JLabel mperpLabelName;
     private final JLabel mperpLabelValue;
 
     Graph graph;
     Node source, destination;
     Layer rutelag, areallag;
     int noder = 0;
 
 /*
     For å plotte punkter:
     Layer rutelag = treeMap.addLayer("Kjørerute"); 
     MapMarkerDot navnx = new MapMarkerDot(rutelag, "navn", breddegrad, lengdegrad);
     //navn er optional: new MapMarkerDot(layer, breddegrad, lengdegrad);∏∏
   map().addMapMarker(navnx);
 
     map().removeMapMarker(navnx); //Fjerne et punkt, hvis man gidder...
     map().removeAllMapMarkers(); //Fjerne alle punkter på en gang.
 */
 
     public vindu(Graph parameterG) {
         super(new GridBagLayout());
         graph = parameterG;
         
         GridBagConstraints c = new GridBagConstraints();
         GridBagConstraints hc =  new GridBagConstraints(); //høyrejustert
         GridBagConstraints vc =  new GridBagConstraints(); //venstrejustert
 
         btn_dijkstra.setActionCommand("dijkstra");
         btn_dijkstra.setMnemonic(KeyEvent.VK_D);
         btn_alt.setActionCommand("alt");
         btn_find_closest.setActionCommand("find_closest");
 
         btn_dijkstra.addActionListener(this);
         btn_alt.addActionListener(this);
         btn_find_closest.addActionListener(this);
         btn_slutt.addActionListener(this);
 
         txt_fra.getDocument().addDocumentListener(this);
         txt_til.getDocument().addDocumentListener(this);
 
         hc.gridx = 0; hc.gridy = 1;
 
         hc.anchor = GridBagConstraints.NORTHEAST;
         vc.anchor = GridBagConstraints.NORTHWEST;
         hc.fill = vc.fill = GridBagConstraints.NONE;
 
         add(new JLabel("Fra:"), hc);
 
         c.gridx = 1; c.gridy = 1;
         add(txt_fra, c);
 
         hc.gridx = 3;
         add(new JLabel("Til:"), hc);
 
         c.gridx = 4;
         add(txt_til, c);
 
 
         hc.gridx = 0; hc.gridy = 2;
         add(new JLabel("Node:"), hc);
 
         vc.gridx = 1; vc.gridy = 2;
         add(lbl_fra, vc);
 
         hc.gridx = 3;
         add(new JLabel("Node:"), hc);
 
         vc.gridx = 4; 
         add(lbl_til, vc);
     
 
         c.gridx = 0; c.gridy = 3;
         add(btn_dijkstra, c);
 
         c.gridx = 1;
         add(btn_alt, c);

         c.gridx = 2;
         add(interestPointDropdown, c);

        c.gridx = 3;
        add(btn_find_closest, c);
         
         vc.gridx = 1; vc.gridy = 4;
         vc.gridwidth = 3;
         add(lbl_tur, vc);
 
         vc.gridy = 5;
         add(lbl_alg, vc);
 
         c.gridx = 5; c.gridy = 6;
         add(btn_slutt, c);
 
         c.gridx = 0; c.gridy = 7;
         c.gridwidth = 6;
         c.gridheight = 5;
         c.fill = GridBagConstraints.BOTH;
         c.weightx = 1.0;
         c.weighty = 1.0;
         add(kart, c);
 
         treeMap = new JMapViewerTree("Lag");
         rutelag = treeMap.addLayer("kjørerute");
         areallag = treeMap.addLayer("undersøkt areal");
         // Listen to the map viewer for user operations so components will
         // receive events and update
         map().addJMVListener(this);		
 
         JPanel panel = new JPanel(new BorderLayout());
         JPanel panelTop = new JPanel();
         JPanel panelBottom = new JPanel();
         JPanel helpPanel = new JPanel();
 
         mperpLabelName = new JLabel("meter/Pixel: ");
         mperpLabelValue = new JLabel(String.format("%s", map().getMeterPerPixel()));
 
         zoomLabel = new JLabel("Zoomnivå: ");
         zoomValue = new JLabel(String.format("%s", map().getZoom()));
 
         kart.add(panel, BorderLayout.NORTH);
         kart.add(helpPanel, BorderLayout.SOUTH);
         panel.add(panelTop, BorderLayout.NORTH);
         panel.add(panelBottom, BorderLayout.SOUTH);
         JLabel helpLabel = new JLabel("Flytt med høyre musknapp,\n "
                 + "zoom med venstre eller dobbeltklikk.");
         helpPanel.add(helpLabel);
         JButton button = new JButton("setDisplayToFitMapMarkers");
         button.addActionListener(e -> map().setDisplayToFitMapMarkers());
         JComboBox<TileSource> tileSourceSelector = new JComboBox<>(new TileSource[] {
                 new OsmTileSource.Mapnik(),
                 new OsmTileSource.TransportMap(),
                 new BingAerialTileSource(),
                 });
         tileSourceSelector.addItemListener(new ItemListener() {
                 @Override
                 public void itemStateChanged(ItemEvent e) {
                 map().setTileSource((TileSource) e.getItem());
                 }
                 });
         JComboBox<TileLoader> tileLoaderSelector;
         tileLoaderSelector = new JComboBox<>(new TileLoader[] {new OsmTileLoader(map())});
         tileLoaderSelector.addItemListener(new ItemListener() {
                 @Override
                 public void itemStateChanged(ItemEvent e) {
                 map().setTileLoader((TileLoader) e.getItem());
                 }
                 });
         map().setTileLoader((TileLoader) tileLoaderSelector.getSelectedItem());
         panelTop.add(tileSourceSelector);
         panelTop.add(tileLoaderSelector);
         final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
         showMapMarker.setSelected(map().getMapMarkersVisible());
         showMapMarker.addActionListener(e -> map().setMapMarkerVisible(showMapMarker.isSelected()));
         panelBottom.add(showMapMarker);
         ///
         final JCheckBox showTreeLayers = new JCheckBox("Tree Layers visible");
         showTreeLayers.addActionListener(e -> treeMap.setTreeVisible(showTreeLayers.isSelected()));
         panelBottom.add(showTreeLayers);
         ///
         final JCheckBox showToolTip = new JCheckBox("ToolTip visible");
         showToolTip.addActionListener(e -> map().setToolTipText(null));
         panelBottom.add(showToolTip);
         ///
         final JCheckBox showTileGrid = new JCheckBox("Tile grid visible");
         showTileGrid.setSelected(map().isTileGridVisible());
         showTileGrid.addActionListener(e -> map().setTileGridVisible(showTileGrid.isSelected()));
         panelBottom.add(showTileGrid);
         final JCheckBox showZoomControls = new JCheckBox("Show zoom controls");
         showZoomControls.setSelected(map().getZoomControlsVisible());
         showZoomControls.addActionListener(e -> map().setZoomControlsVisible(showZoomControls.isSelected()));
         panelBottom.add(showZoomControls);
         final JCheckBox scrollWrapEnabled = new JCheckBox("Scrollwrap enabled");
         scrollWrapEnabled.addActionListener(e -> map().setScrollWrapEnabled(scrollWrapEnabled.isSelected()));
         panelBottom.add(scrollWrapEnabled);
         panelBottom.add(button);
 
         panelTop.add(zoomLabel);
         panelTop.add(zoomValue);
         panelTop.add(mperpLabelName);
         panelTop.add(mperpLabelValue);
 
         kart.add(treeMap, BorderLayout.CENTER);
 
         map().addMouseListener(new MouseAdapter() {
                 @Override
                 public void mouseClicked(MouseEvent e) {
                 if (e.getButton() == MouseEvent.BUTTON1) {
                 map().getAttribution().handleAttribution(e.getPoint(), true);
                 }
                 }
                 });
 
         map().addMouseMotionListener(new MouseAdapter() {
                 @Override
                 public void mouseMoved(MouseEvent e) {
                 Point p = e.getPoint();
                 boolean cursorHand = map().getAttribution().handleAttributionCursor(p);
                 if (cursorHand) {
                 map().setCursor(new Cursor(Cursor.HAND_CURSOR));
                 } else {
                 map().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                 }
                 if (showToolTip.isSelected()) map().setToolTipText(map().getPosition(p).toString());
                 }
                 });
 
     } //konstruktør for vindu
 
     public double grad(double rad) {
         return rad / Math.PI * 180;
     }
 
     //Tegn ruta på kartet
     public void tegn_ruta(Color color) {
 /*
     F.eks. ved å starte med målnoden, og følge forgjengere hele veien
         tilbake til startnoden. Nodene har bredde- og lengdegrader, som 
         fungerer med MapMarkerDot:
 */     

        Node node = destination;//graph.getDestinationNode();
        noder = 0;
        
        while (node != null) {
            MapMarkerDot prikk;
            prikk = new MapMarkerDot(rutelag, grad(node.getVector().getLatitude()), grad(node.getVector().getLongitude()));
            map().addMapMarker(prikk);
            prikk.getStyle().setBackColor(color);
            node = (Node) ((Pre) node.getData()).get_pre();
            noder++;
        }
        
        /* for (Node n : vei) {
            MapMarkerDot prikk;
            prikk = new MapMarkerDot(rutelag, grad(n.getVector().getLatitude()), grad(n.getVector().getLongitude()));
            map().addMapMarker(prikk);
        }
        System.out.println("Destination node (x, y): (" + graph.getDestinationNode().getVector().getLatitude() + ", " + graph.getDestinationNode().getVector().getLongitude() + ")"); */
     }
 
     //Tegn det gjennomsøkte arealet, altså alle noder med forgjengere.
     public void tegn_areal() {
         /* ... */
     }
 
     //Knapper
     public void actionPerformed(ActionEvent e) {
        map().removeAllMapMarkers();

         Date tid1 = new Date();
         String tur = "Kjøretur " + txt_fra.getText() + " — " + txt_til.getText();
         String alg = "";
         switch (e.getActionCommand()) {
             case "dijkstra":
                 /* sett inn et kall for å kjøre Dijkstras algoritme her */
                 /* graph.setSourceNode(graph.getNodes().get(Integer.parseInt(txt_fra.getText())));
                 graph.setDestinationNode(graph.getNodes().get(Integer.parseInt(txt_til.getText())));
                 graph.dijkstra(graph.getSourceNode(), graph.getDestinationNode()); */

                 source = graph.getNodes().get(Integer.parseInt(txt_fra.getText()));
                destination = graph.getNodes().get(Integer.parseInt(txt_til.getText()));
                graph.dijkstra(source, destination);
                alg = "Dijkstras algoritme ";
                draw_algorithm(tur, alg, tid1, destination);
                 //Dijkstra.calculateShortestPathFromSource(graph.getSourceNode(), graph.getDestinationNode(), graph.getNumberOfNodes(), graph.getNodes());
                 //noder = graph.getDestinationNode().getShortestPath().size();
                 break;
             case "alt":
                 /* sett inn kall for å kjøre ALT her */
                    //graph.setSourceNode(graph.getNodes().get(Integer.parseInt(txt_fra.getText())));
                    //graph.setDestinationNode(graph.getNodes().get(Integer.parseInt(txt_til.getText())));
                    source = graph.getNodes().get(Integer.parseInt(txt_fra.getText()));
                    destination = graph.getNodes().get(Integer.parseInt(txt_til.getText()));
                    graph.alt(source, destination);
                    alg = "ALT-algoritmen ";
                    draw_algorithm(tur, alg, tid1, destination);;
                    //ALT.calculateShortestPathFromSource(graph.getSourceNode(), graph.getDestinationNode(), graph.getNumberOfNodes(), graph.getNodes());
                    //noder = graph.getDestinationNode().getShortestPath().size();
                 break;
            case "find_closest":
                graph.setSourceNode(graph.getNodes().get(Integer.parseInt(txt_fra.getText())));
                String selectedType = (String) interestPointDropdown.getSelectedItem();
                int interestPointCode = Integer.parseInt(selectedType.split(" - ")[0]);
                Node[] closestInterestPoints = graph.find_closest_interestpoints(graph.getSourceNode(), interestPointCode, graph.getNodes());
                System.out.println("Number of interestpoints: " + closestInterestPoints.length);
                tur = "Tider - " + txt_fra.getText() + ":";
                alg = "Noder: ";
                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
                for (int i = 0; i < closestInterestPoints.length; i++) {
                    destination = closestInterestPoints[i];
                    InterestPoint interestPoint = (InterestPoint) destination;
                    Map<Integer, String> interestPointTypes = new HashMap<>();
                    interestPointTypes.put(1, "Stedsnavn");
                    interestPointTypes.put(2, "Bensinstasjon");
                    interestPointTypes.put(4, "Ladestasjon");
                    interestPointTypes.put(8, "Spisested");
                    interestPointTypes.put(16, "Drikkested");
                    interestPointTypes.put(32, "Overnattingsted");
                    int type = interestPoint.getType();
                    alg += interestPoint.getDescription() + ": ";
                    for (Map.Entry<Integer, String> entry : interestPointTypes.entrySet()) {
                        if ((type & entry.getKey()) != 0) {
                            alg += " " + entry.getValue();
                        }
                    }
                    alg += " | ";
                    int tid = interestPoint.getTime();
                    int tt = tid / 360000; tid -= 360000 * tt;
                    int mm = tid / 6000; tid -= 6000 * mm;
                    int ss = tid / 100;
                    int hs = tid % 100;
                    tur += String.format("  %d:%02d:%02d,%02d  %d", tt, mm, ss, hs, destination.getDistance()) + " | ";
                    
                    // Set the color for each route
                    tegn_ruta(colors[i]);
                }
                System.out.println(alg);
                System.out.println(tur);
                lbl_tur.setText(tur);
                lbl_alg.setText(alg);
                break;
             default:
                 System.exit(0);
                 break;
                
         }
         graph.reset();
        }
            
         
 
 /*
     Vise frem kjøretid for bilen, hvis målet ble funnet:
 */

         
     

     private void draw_algorithm(String tur, String alg, Date tid1, Node node) {
            Date tid2 = new Date();
         
            if (node.getData() == null) {
                tur += "  Fant ikke veien!";
            } else {
                int tid = node.getTime();
                int tt = tid / 360000; tid -= 360000 * tt;
                int mm = tid / 6000; tid -= 6000 * mm;
                int ss = tid / 100;
                int hs = tid % 100;
                tegn_ruta(Color.YELLOW);
                tur = String.format("%s Kjøretid %d:%02d:%02d,%02d, antall noder: %d", tur, tt, mm, ss, hs, this.noder);

            }
            float sek = (float)(tid2.getTime() - tid1.getTime()) / 1000;
            alg += String.format(" prosesserte %,d noder på %2.3fs. %2.0f noder/ms", graph.getNumberOfProcessed(), sek, graph.getNumberOfProcessed()/sek/1000);
            lbl_tur.setText(tur);
            lbl_alg.setText(alg);
            System.out.println(tur);
            System.out.println(alg);
            System.out.println();
            /* graph = new Graph();
            graph.init_graph(nodeList, edgeList, interestPoints); */
        
     }


 
     //Skriving i tekstfelt
     public void changedUpdate(DocumentEvent ev) {
     }
     public void removeUpdate(DocumentEvent ev) {
         stedsoppslag();
     }
     public void insertUpdate(DocumentEvent ev) {
         stedsoppslag();
     }
 
     //Finn hvilket felt som ble endret.
     //Slå opp nodenumre om mulig/ønskelig
     void stedsoppslag() {
 /*
         String txt = txt_fra.getText();
         if (!txt.equals(gml_fra)) {
             //Endret fra-felt
             gml_fra = txt;
             if (txt.matches("[0-9]+")) {
                 G.startnode = Integer.parseInt(txt);
             } else {
                 Integer I = G.punkter.get(txt);
                 if (I != null) {
                     G.startnode = I.intValue();
                 } else {
                     G.startnode = -1;
                 }
             }
             lbl_fra.setText(Integer.toString(G.startnode));
         }
 
         txt = txt_til.getText();
         if (!txt.equals(gml_til)) {
             //Endret til-felt
             gml_til = txt;
             if (txt.matches("[0-9]+")) {
                 G.målnode = Integer.parseInt(txt);
             } else {
                 Integer I = G.punkter.get(txt);
                 if (I != null) {
                     G.målnode = I.intValue();
                 } else {
                     G.målnode = -1;
                 }
             }
             lbl_til.setText(Integer.toString(G.målnode));
         }
 
         boolean klart = (G.målnode > 0 && G.startnode > 0);
         btn_dijkstra.setEnabled(klart);
         btn_alt.setEnabled(klart);
 */
     }
 
     //Noe skjer med kartet. 
     public void processCommand(JMVCommandEvent command) {
         if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
                 command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
             updateZoomParameters();
         }
     }
 
     private void updateZoomParameters() {
         if (mperpLabelValue != null)
             mperpLabelValue.setText(String.format("%s", map().getMeterPerPixel()));
         if (zoomValue != null)
             zoomValue.setText(String.format("%s", map().getZoom()));			
     }
 
     private JMapViewer map() {
         return treeMap.getViewer();
     }
     
 }
 
 
 //Java Navi
 public class Navi {
 
     public static void gui(Graph graph) {
        
         JFrame frame = new JFrame("Kartnavigasjon");
 
         //Innhold
         frame.add(new vindu(graph));
 
         //Vis vinduet
         frame.pack();
         frame.setVisible(true);
     }
 
     public static void main(String[] args) {
         //Les inn kart, og
         //opprett grafen
     Graph graph = new Graph();
     List<double[]> nodeList = graph.read_from_file("noder.txt");
     List<double[]> edgeList = graph.read_from_file("kanter.txt");
     Map<Integer, InterestPoint> interestPoints = graph.read_interest_points("interessepkt.txt");
     graph.init_graph(nodeList, edgeList, interestPoints);

     
        //Les inn noder og kanter fra fil
        
 
         //...
 
 	
         gui(graph); //Få opp GUI med tiles fra openstreetmap
   }
 }
