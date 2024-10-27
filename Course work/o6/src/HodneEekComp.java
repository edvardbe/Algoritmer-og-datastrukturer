import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HodneEekComp{

    public static void main(String[] args) {
        try {

            if (args[0].equals("c")){
                byte[] input_bytes = Files.readAllBytes(Paths.get(args[1]));
                byte[] lempel_ziv_compressed = LZ.compress(input_bytes);
                byte[] huffman_compressed = Huffman.compress(lempel_ziv_compressed);
                FileOutputStream out = new FileOutputStream(args[2]);
                out.write(huffman_compressed);
                out.close();

            }
            if (args[0].equals("d")){
                byte[] input_bytes = Files.readAllBytes(Paths.get(args[1]));
                byte[] huffman_decompressed = Huffman.decompress(input_bytes);
                byte[] lempel_ziv_decompressed = LZ.decompress(huffman_decompressed);
                FileOutputStream out = new FileOutputStream(args[2]);
                out.write(lempel_ziv_decompressed);
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}