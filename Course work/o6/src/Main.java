import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main{

    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                System.out.println("Usage: java Main [c/d] [input file] [output file]");
                return;
            }
            if (args[0].equals("c")){
                byte[] input_bytes = Files.readAllBytes(Paths.get(args[1]));
                byte[] lempel_ziv_compressed = LZW.compress(input_bytes);
                byte[] huffman_compressed = Huffman.compress(lempel_ziv_compressed);
                FileOutputStream out = new FileOutputStream(args[2]);
                out.write(huffman_compressed);
                out.close();

                System.out.println();
                System.out.println("Original file size: " + input_bytes.length);
                System.out.println("Compressed file size: " + huffman_compressed.length);
                
            }
            else if (args[0].equals("d")){
                byte[] input_bytes = Files.readAllBytes(Paths.get(args[1]));
                byte[] huffman_decompressed = Huffman.decompress(input_bytes);
                byte[] lempel_ziv_decompressed = LZW.decompress(huffman_decompressed);
                FileOutputStream out = new FileOutputStream(args[2]);
                out.write(lempel_ziv_decompressed);
                out.close();

                System.out.println();
                System.out.println("Compressed file size: " + input_bytes.length);
                System.out.println("Decompressed file size: " + lempel_ziv_decompressed.length);
            } 
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}