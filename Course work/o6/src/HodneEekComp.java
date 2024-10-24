import java.io.File;
public class HodneEekComp{

    public static void main(String[] args) {
        try {

            if (args[0].equals("c")){
                File HuffcompressOutputFile = File.createTempFile(args[2],null);
                HuffmanTest.compress(HuffcompressOutputFile, args[2]);

            }
            if (args[0].equals("d")){
                File HuffdecompressOutputFile = File.createTempFile(args[2],null);
                HuffmanTest.decompress(args[1], HuffdecompressOutputFile);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}