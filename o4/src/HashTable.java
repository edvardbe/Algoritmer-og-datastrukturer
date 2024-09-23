package o4.src;


public class HashTable<V> {
    private HashNode<V>[] tabel;
    private Class<?> type;
    private int size;
    private int collision = 0;

    public HashTable(int size){
        this.tabel = new HashNode[size];
        this.size = size;
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
        tabel[index] = null;
    }

    public V get(V value){
        int index = hash(value);
        if(index == -1){
            throw new IllegalArgumentException("Key not found");
        }
        HashNode<V> node = tabel[index];
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
        return sum % tabel.length;
          
    }

    private void putVal(int hash, V value){
        if (tabel[hash] == null) {
            tabel[hash] = new HashNode<V>(value);
        } else {
            HashNode<V> node = tabel[hash];
            while (node.getNext() != null) {
                node = node.getNext();
                collision++;
            }
            node.setNext(new HashNode<V>(value));
            System.out.println(collision);
        }
    }
}