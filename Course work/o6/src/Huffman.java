import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

class Node {
    int frequency;
    char c;
    Node left;
    Node right;
    String[] bitStrings = new String[256];

    public Node(){};
    public Node (char c, int frequency){
        this.c = c;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public Node (char c, int frequency, Node left, Node right){
        this.c = c;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public void createBitStrings(Node node, String s){
        if(node.left != null && node.right != null){
            createBitStrings(node.left, s + "0");
            createBitStrings(node.right, s + "1");
        } else {
            bitStrings[node.c] = s; 
        }
    }

    public static Node createHuffmanTree(PriorityQueue<Node> priorityQueue) {
        Node root = new Node();
        while (priorityQueue.size() > 1) {
            Node x = priorityQueue.poll();
            Node y = priorityQueue.poll();
            Node f = new Node();

            f.frequency = x.frequency + y.frequency;
            f.c = '\0';
            f.left = x;
            f.right = y;
            priorityQueue.add(f);
            root = f;
        }
        return root;
    }
}

class HuffmanComparator implements Comparator<Node>{
    public int compare(Node x, Node y){
        return x.frequency - y.frequency;
    }
}

public class Huffman {

    /**
     * Compresses an arbitrary file with Huffman-code.
     * 
     * @param input_path path for input file, in other words path to the file we want to compress 
     * @param output_path path for output file, creates a new compressed file
     * @throws IOException Incase of input/output exception from file handling
     */

    public static byte[] compress(byte[] input_bytes) throws IOException{
        int length = 256;
        int[] frequencies = new int[length];
        
        for (byte b : input_bytes) {
            frequencies[b & 0xFF]++;
        }
        
        PriorityQueue<Node> priorityQueue = createHuffmanQueue(length, new HuffmanComparator(), frequencies);
        Node root = Node.createHuffmanTree(priorityQueue);
        root.createBitStrings(root, "");
    
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outStream);

        for (int frequency : frequencies) {
            out.writeInt(frequency);
        }

        int writeByte = 0;
        int bitIteration = 0;
        ArrayList<Byte> bytes = new ArrayList<>();

        for (byte b : input_bytes) {
            String bitString = root.bitStrings[b & 0xFF];
            for (char bit : bitString.toCharArray()) {
                writeByte = (writeByte << 1) | (bit == '1' ? 1 : 0);
                bitIteration++;
                if (bitIteration == 8) {
                    bytes.add((byte) writeByte);
                    bitIteration = 0;
                    writeByte = 0;
                }
            }
        }
        while (bitIteration < 8 && bitIteration != 0) {
            writeByte = (writeByte << 1);
            ++bitIteration;
        }
        bytes.add((byte) writeByte);
        for (Byte s : bytes) {
            out.write(s);
        }
        out.close();

        return outStream.toByteArray();
    }

    /**
     * Creating a queue prioritized by the frequency of each node. 
     * 
     * @param length Initial capasity of the PriorityQueue
     * @param huffmanComparator Comparator used to order each node by their frequency
     * @param frequencies frequency of each character in the original file
     * @return
     */

    private static PriorityQueue<Node> createHuffmanQueue(int length, HuffmanComparator huffmanComparator, int[] frequencies) {

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(length, huffmanComparator);
        for (int i = 0; i < length; i++){
            if (frequencies[i] > 0) {
                Node node = new Node();
                node.c = (char) i;
                node.frequency = frequencies[i];

                node.left = null;
                node.right = null;

                priorityQueue.add(node);
            }
        }
        return priorityQueue;
    }

    /**
     * Decompresses a file that is compressed to Huffman-code.
     * 
     * @param input_path path for input file, in other words path to the file we want to decompress 
     * @param output_path path for output file, creates a new file if path does not match any exisiting file.  
     * @throws IOException Incase of input/output exception from file handling
     */

    public static byte[] decompress(byte[] input_bytes) throws IOException {
        int length = 256;
        int[] frequencies= new int[length];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input_bytes);
        DataInputStream in = new DataInputStream(inputStream);
        
        for (int i = 0; i < length; i++) {
            frequencies[i] = in.readInt();
        }
        //byte lastByte = in.readByte();
        
        PriorityQueue<Node> priorityQueue = createHuffmanQueue(length, new HuffmanComparator(), frequencies);
        Node root = Node.createHuffmanTree(priorityQueue);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        
        in.close();

        Node currentNode = root;
        byte currentByte;
        
        while (in.available() > 0) {
            currentByte = in.readByte();
            for (int i = 7; i >= 0; i--) {
                int bit = (currentByte >> i) & 1;
                currentNode = (bit == 0) ? currentNode.left : currentNode.right;

                if (currentNode.left == null && currentNode.right == null) {
                    outStream.write(currentNode.c); // Skriver tegnet til output
                    currentNode = root;
                }
            }
        }

        return outStream.toByteArray();

    }

    public static void main(String[] args) {
        try {
            byte[] input_bytes = Files.readAllBytes(Paths.get("diverse.lyx"));
            byte[] compressed_bytes = compress(input_bytes);
            FileOutputStream out = new FileOutputStream("diverse.hec");
            out.write(compressed_bytes);
            out.close();
    
            input_bytes = Files.readAllBytes(Paths.get("diverse.hec"));
            byte[] decompressed_bytes = decompress(input_bytes);
            FileOutputStream outDecomp = new FileOutputStream("løsning.lyx");
            outDecomp.write(decompressed_bytes);
            outDecomp.close();

        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
