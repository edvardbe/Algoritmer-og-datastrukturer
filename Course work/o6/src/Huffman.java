import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;


public class Huffman {
    private int length;

    public static void compress(File file, String path){
        // Read the file
        // Compress the file
        // Write the compressed file
        int[] freq = new int[256];
        try {
            FileInputStream fis = new FileInputStream(path);
            while (fis.available() > 0) {
                freq[fis.read()]++;
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Node> nodes = new ArrayList<>();
        



    }

    public static void main(String[] args) {
        compress(new File("diverse.txt"), "diverse.txt");
    }
}
