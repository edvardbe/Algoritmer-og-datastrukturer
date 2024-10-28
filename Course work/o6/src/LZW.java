import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;



public class LZW {
    public static byte[] compress(byte[] input) {
        int dictSize = 256;
        Map<ArrayList<Byte>, Integer> dictionary = new HashMap<>();
        
        // Initialize dictionary with single bytes
        for (int i = 0; i < dictSize; i++) {
            ArrayList<Byte> byteList = new ArrayList<>();
            byteList.add((byte) i);
            dictionary.put(byteList, i);
        }

        ArrayList<Byte> current = new ArrayList<>();
        List<Integer> result = new ArrayList<>();

        for (byte b : input) {
            ArrayList<Byte> newSeq = new ArrayList<>(current);
            newSeq.add(b);

            if (dictionary.containsKey(newSeq)) {
                current = newSeq;
            } else {
                result.add(dictionary.get(current));
                dictionary.put(newSeq, dictSize++);
                current = new ArrayList<>();
                current.add(b);
            }
        }

        if (!current.isEmpty()) {
            result.add(dictionary.get(current));
        }

        // Convert the integer list to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (Integer code : result) {
            byte[] bytes = ByteBuffer.allocate(4).putInt(code).array();
            outputStream.write(bytes, 0, 4);
        }

        return outputStream.toByteArray();
    }

    public static byte[] decompress(byte[] input) {
        // Convert byte array back to integer list
        List<Integer> encodedText = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.wrap(input);
        while (buffer.remaining() >= 4) {
            encodedText.add(buffer.getInt());
        }

        int dictSize = 256;
        Map<Integer, ArrayList<Byte>> dictionary = new HashMap<>();
        
        // Initialize dictionary with single bytes
        for (int i = 0; i < dictSize; i++) {
            ArrayList<Byte> byteList = new ArrayList<>();
            byteList.add((byte) i);
            dictionary.put(i, byteList);
        }

        ArrayList<Byte> current = new ArrayList<>(dictionary.get(encodedText.get(0)));
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        for (byte b : current) {
            result.write(b);
        }

        for (int i = 1; i < encodedText.size(); i++) {
            int code = encodedText.get(i);
            ArrayList<Byte> entry;
            
            if (dictionary.containsKey(code)) {
                entry = new ArrayList<>(dictionary.get(code));
            } else {
                entry = new ArrayList<>(current);
                entry.add(current.get(0));
            }

            for (byte b : entry) {
                result.write(b);
            }

            ArrayList<Byte> newSeq = new ArrayList<>(current);
            newSeq.add(entry.get(0));
            dictionary.put(dictSize++, newSeq);
            
            current = entry;
        }

        return result.toByteArray();
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