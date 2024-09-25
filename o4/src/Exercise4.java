package o4.src;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exercise4 {
    private static List<String> readFromFile(String path){
        List<String> list = new ArrayList<>();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return list;
    }

    public static void main(String[] args) {
        
        
        List<String> list = readFromFile("o4/src/navn.txt");
        HashTable<String> hashTabel = new HashTable<>(list.size());
        hashTabel.hashList(list);
        
        hashTabel.print();
        System.out.println("Hashtable contains entire dataset: " + hashTabel.checkContent(list));
        System.out.println(hashTabel.get("Edvard Berdal Eek"));
    }
    
}
