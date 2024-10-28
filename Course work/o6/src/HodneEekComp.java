import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HodneEekComp{

    public static void main(String[] args) {
        try {
            byte[] input_bytes = Files.readAllBytes(Paths.get("diverse.lyx"));
                byte[] lzw_compressed = LZW.compress(input_bytes);
                byte[] huffman_compressed = Huffman.compress(lzw_compressed);
                byte[] lz_compressed = LZ.compress(huffman_compressed);

                FileOutputStream out = new FileOutputStream("diverse.hec");
                out.write(lz_compressed);
                out.close();

                byte[] de_input_bytes = Files.readAllBytes(Paths.get("diverse.hec"));
                byte[] de_lzw_decompressed = LZ.decompress(de_input_bytes);

                byte[] de_huffman_decompressed = Huffman.decompress(de_lzw_decompressed);
                byte[] de_lempel_ziv_decompressed = LZ.decompress(de_huffman_decompressed);
                FileOutputStream de_out = new FileOutputStream("løsning.lyx");
                de_out.write(de_lempel_ziv_decompressed);
                de_out.close();
            
            /* if (args[0].equals("c")){
                byte[] input_bytes = Files.readAllBytes(Paths.get(args[1]));
                byte[] lempel_ziv_compressed = LZW.compress(input_bytes);
                byte[] huffman_compressed = Huffman.compress(lempel_ziv_compressed);
                FileOutputStream out = new FileOutputStream(args[2]);
                out.write(huffman_compressed);
                out.close();

            }
            if (args[0].equals("d")){
                byte[] input_bytes = Files.readAllBytes(Paths.get(args[1]));
                byte[] huffman_decompressed = Huffman.decompress(input_bytes);
                byte[] lempel_ziv_decompressed = LZW.decompress(huffman_decompressed);
                FileOutputStream out = new FileOutputStream(args[2]);
                out.write(lempel_ziv_decompressed);
                out.close();
            } */
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}