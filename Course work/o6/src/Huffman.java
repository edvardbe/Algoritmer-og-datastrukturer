import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static void compress(String input_path, String output_path) throws IOException{
        int length = 256;
        int[] frequencies= new int[length];
        
        FileInputStream inputFile = new FileInputStream(input_path);
        int numberOfBytes = inputFile.available();
        for (int i = 0; i < numberOfBytes; i++) {
            frequencies[inputFile.read()]++;
        }
        inputFile.close();
        
        PriorityQueue<Node> priorityQueue = createHuffmanQueue(length, new HuffmanComparator(), frequencies);
        Node root = Node.createHuffmanTree(priorityQueue);
        root.createBitStrings(root, "");
    
        FileInputStream inFile = new FileInputStream(input_path);
        DataOutputStream outFile = new DataOutputStream(new FileOutputStream(output_path));

        for (int frequency : frequencies) {
            outFile.writeInt(frequency);
        }

        int input;
        int writeByte = 0;
        int bitIteration = 0;
        int j = 0;
        ArrayList<Byte> bytes = new ArrayList<>();
        for (int k = 0; k < numberOfBytes; ++k) {
            input = inFile.read();
            j = 0;
            String bitString = root.bitStrings[input];
            while (j < bitString.length()) {
                if (bitString.charAt(j) == '0'){
                    // If the character at position j is '0' the byte is shifted by one to the left position
                    // For example, if writeByte = 00000010 and we do a left bitshift, writeByte << 1,
                    // it is now 00000100.
                    writeByte = (writeByte << 1);
                } else {
                    // If the character at position j is not '0', in other words it is '1',
                    // the byte is shifted by one to the left and the first position is a 1. For example, 
                    // if writeByte = 00000010 and we do a left bitshift, (writeByte << 1) | 1,
                    // it is now 00000101.
                    writeByte = ((writeByte << 1) | 1);
                }
                ++j;
                ++bitIteration;
                 if (bitIteration == 8) {
                    // If the bit iteration is 8 we have completed the byte and we 
                    // can add the writeByte to the list of bytes. We also reset thje
                    bytes.add((byte) writeByte);
                    bitIteration = 0;
                    writeByte = 0;
                }
            }
        }
        int lastByte = bitIteration;
        while (bitIteration < 8 && bitIteration != 0) {
            writeByte = (writeByte << 1);
            ++bitIteration;
        }
        bytes.add((byte) writeByte);
        outFile.write(lastByte);
        for (Byte s : bytes) {
            outFile.write(s);
        }
        inFile.close();
        outFile.close();
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

    public static void decompress(String input_path, String output_path) throws IOException {
        int length = 256;
        int[] frequencies= new int[length];
        DataInputStream inputFile = new DataInputStream(new FileInputStream(input_path));
        
        for (int i = 0; i < length; i++) {
            frequencies[i] = inputFile.readInt();
        }
        byte lastByte = inputFile.readByte();
        
        PriorityQueue<Node> priorityQueue = createHuffmanQueue(length, new HuffmanComparator(), frequencies);
        Node root = Node.createHuffmanTree(priorityQueue);

        FileOutputStream outFile = new FileOutputStream(output_path);
        byte[] bytes = inputFile.readAllBytes();
        inputFile.close();

        int bytesLength = bytes.length;
        Node currentNode = root;
        int bitIndex;
        
        for (int byteIndex = 0; byteIndex < bytesLength; byteIndex++) {
            byte currentByte = bytes[byteIndex];
            bitIndex = (byteIndex == bytesLength - 1) ? lastByte : 8;

            for (int i = 7; i >= 8 - bitIndex; i--) {
                int bit = (currentByte >> i) & 1;
                currentNode = (bit == 0) ? currentNode.left : currentNode.right;

                if (currentNode.left == null && currentNode.right == null) {
                    outFile.write(currentNode.c); // Skriver tegnet til output
                    currentNode = root;
                }
            }
        }

        inputFile.close();
        outFile.flush();
        outFile.close();

    }

    public static void main(String[] args) {
        try {
            compress("c-test.txt", "c-test.hec");
            decompress("c-test.hec", "d-test.txt");
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
