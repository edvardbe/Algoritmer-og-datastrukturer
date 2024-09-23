package o4.src;

import java.util.ArrayList;
import java.util.List;

public class Exercise4 {
    private static HashTable<String> readFromFile(String path){
            
        int length = 0;
        List<String> list = new ArrayList<>();
        try {
            java.io.File file = new java.io.File(path);
            java.util.Scanner scanner = new java.util.Scanner(file);
            while (scanner.hasNextLine()) {
                length++;
                list.add(scanner.nextLine());
            }
            scanner.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found");
        }
        HashTable<String> hashTable = new HashTable<String>(length);
        for (String string : list) {
            hashTable.put(string);
        }
        return hashTable;
    }
    public static void main(String[] args) {
        
        
        
        HashTable<String> hashTabel = readFromFile("o4/src/navn.txt");
        System.out.println(hashTabel.get("Victor Udnæs"));

    }
    
}
