import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HodneEekComp{

    public static void main(String[] args) {
        try {
            if(Integer.parseInt(args[1]) > 4 || Integer.parseInt(args[1]) < 1) {
                throw new IllegalArgumentException("Write a number between 1 and 4");
            }
            if (args[0].equals("c")){
                int byteSize = Integer.parseInt(args[1]);
                byte[] input_bytes = Files.readAllBytes(Paths.get(args[2]));
                byte[] lempel_ziv_compressed = LZW.compress(input_bytes, byteSize);
                byte[] huffman_compressed = Huffman.compress(lempel_ziv_compressed);
                FileOutputStream out = new FileOutputStream(args[3]);
                out.write(huffman_compressed);
                out.close();

                System.out.println();
                System.out.println("Original file size: " + input_bytes.length);
                System.out.println("Compressed file size: " + huffman_compressed.length);
                
            }
            else if (args[0].equals("d")){
                int byteSize = Integer.parseInt(args[1]);
                byte[] input_bytes = Files.readAllBytes(Paths.get(args[2]));
                byte[] huffman_decompressed = Huffman.decompress(input_bytes);
                byte[] lempel_ziv_decompressed = LZW.decompress(huffman_decompressed, byteSize);
                FileOutputStream out = new FileOutputStream(args[3]);
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