package o4.src.Del2;

public class HashTable {
    private int[] table;
    private int collision = 0;
    private int length;

    public HashTable(int length){
        this.length = calculateSize(length);
        this.table = new int[this.length];
        
    }

    private int calculateSize(int length){
        return Integer.highestOneBit(length) << 1;
    }    

    public void put(int value){
        int h1 = multiplicativeHash(value);
        if(table[h1] == 0){
            table[h1] = value;
        } else {
            collision++;
            int i = 1;
            while (i < length) {
                int h2 = ((moduloHash(value) * i + h1) % (length - 1) + (length - 1)) % (length - 1);
                if(table[h2] == 0) {
                    table[h2] = value;
                    break;
                } else {
                    i++;
                    collision++;
                }
            }
            
        }
    }

    public void remove(int value){
        int h1 = multiplicativeHash(value);
        if(table[h1] == value){
            table[h1] = 0;
            return;
        }
        int i = 1;
        while (i < length ) {
            int h2 = ((moduloHash(value) * i + h1) % (length - 1) + (length - 1)) % (length - 1);
            if(table[h2] == value){
                table[h2]= 0;
                return;
            }
            i++;
        }
    }

    public int get(int value){
        int h1 = multiplicativeHash(value);
        if(table[h1] == value){
            return value;
        }
       
        int i = 1;
        while (i < length ) {
            int h2 = ((moduloHash(value) * i + h1) % (length - 1) + (length - 1)) % (length - 1);
            if (table[h2] == value) {
                return table[h2];
            }
            i++;
        }
        return -1;
    }

    private int getHash(int value){
        int h1 = multiplicativeHash(value);
        if(table[h1] == value){
            return h1;
        }
        
        int i = 1;
        while (i < length ) {
            int h2 = (moduloHash(value) * i + h1) % (length - 1);
            if (table[h2] == value) {
                return h2 ;
            }
            i++;
        }
        return -1;
    }

    private int multiplicativeHash(int value){
        double A = value * (Math.sqrt(5.0) - 1.0) / 2.0;
        A = A - (int) A;
        return (int) (length * Math.abs(A)); 
    }
    private int moduloHash(int value){
        int result = (2 * Math.abs(value) + 1) % (length - 1);
        return result < 0 ? result + (length - 1) : result;
    }

    public void hashList(int[] array){
        for (int i : array) {
            put(i);
        }
    }


    public int checkContent(int[] array){
        for (int i : array) {
            if (get(i) == 0) {
                return i;
            }
        }
        return -1;
    }

    public int getCollisions() {
        return collision;
    }

    public int getLength() {
        return length;
    }
}