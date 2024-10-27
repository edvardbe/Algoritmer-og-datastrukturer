public class HodneEekComp{

    public static void main(String[] args) {
        try {

            if (args[0].equals("c")){
                Huffman.compress(args[1], args[2]);

            }
            if (args[0].equals("d")){
                Huffman.decompress(args[1], args[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}