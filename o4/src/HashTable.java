package o4.src;

import java.util.List;

public class HashTable<V> {
    private HashNode<V>[] table;
    private Class<?> type;
    private int collision = 0;

    public HashTable(int size){
        this.table = new HashNode[size];
    }

        
    public void put(V value){
        if (type == null) {
            type = value.getClass();
        } else if (!type.isInstance(value)) {
            throw new IllegalArgumentException("Value is not of the correct type");
        }
        // Type check removed because it is not possible to check the type of a generic parameter at runtime
        putVal(hash(value), value);
    }

    public void remove(V value){
        int index = hash(value);
        if(index == -1){
            throw new IllegalArgumentException("Value not found");
        }
        table[index] = null;
    }

    public V get(V value){
        int index = hash(value);
        if(index == -1){
            throw new IllegalArgumentException("Key not found");
        }
        HashNode<V> node = table[index];
        while (!node.getData().equals(value) && node.getNext() != null) {
            node = node.getNext();
        }
        return node.getData();
    }

    public int hash(V value){
        String s = value.toString();
        int sum = 0;
        int counter = 1;
        for (char c : s.toCharArray()) {
            sum += c * counter;
            counter++;
        }
        return sum % table.length;
          
    }

    public void hashList(List<V> list){
        for (V v : list) {
            put(v);
        }
    }

    private void putVal(int hash, V value){
        if (table[hash] == null) {
            table[hash] = new HashNode<V>(value);
        } 
        else if (table[hash].getData().equals(value)) {
            return;
        } else {
            collision++;
            HashNode<V> node = table[hash];
            while (node.getNext() != null) {
                node = node.getNext();
            }
            node.setNext(new HashNode<V>(value));
        }
    }

    public boolean checkContent(List<V> list){
        int counter = 0;
        for (V v : list) {
            counter++;
            V value = get(v);
            if (value == null) {
                return false;
            }
        }
        System.out.println(counter);
        return true;
    }

    public void print(){

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                HashNode<V> node = table[i];
                while (node != null) {
                    V data = node.getData();
                    System.out.print(data + " : " + hash(data));
                    if (node.getNext() != null) {
                        System.out.print(" -> ");
                    }
                    node = node.getNext();
                }
                System.out.println("\n");
            }
        }
        System.out.println("Collisions: " + collision + "\n");
        System.out.println("Load factor: " + (double) (table.length - collision) / table.length);
        System.out.println("Average collision per persion: " + (double) collision / table.length);

    }
}