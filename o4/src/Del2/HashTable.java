package o4.src.Del2;

public class HashTable {
    private int[] table;
    private int collision = 0;
    private int size;

    public HashTable(int size){
        this.size = calculateSize(size);
        this.table = new int[this.size];
        
    }

    private int calculateSize(int size){
        return (int) Math.pow(2, Math.ceil(Math.log(size)/Math.log(2)));
    }    

    public void put(int value){
        int h1 = multiHash(value);
        if(table[h1] == 0){
            table[h1] = value;
        } else {
            collision++;
            int i = 1;
            while (i < size) {
                int h2 = ((modHash(value) * i + h1) % (size - 1) + (size - 1)) % (size - 1);
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
        int h1 = multiHash(value);
        if(table[h1] == value){
            table[h1] = 0;
            return;
        }
        int i = 1;
        while (i < size ) {
            int h2 = ((modHash(value) * i + h1) % (size - 1) + (size - 1)) % (size - 1);
            if(table[h2] == value){
                table[h2]= 0;
                return;
            }
            i++;
        }
    }

    public int get(int value){
        int h1 = multiHash(value);
        if(table[h1] == value){
            return value;
        }
       
        int i = 1;
        while (i < size ) {
            int h2 = ((modHash(value) * i + h1) % (size - 1) + (size - 1)) % (size - 1);
            if (table[h2] == value) {
                return table[h2];
            }
            i++;
        }
        return -1;
    }

    private int getHash(int value){
        int h1 = multiHash(value);
        if(table[h1] == value){
            return h1;
        }
        
        int i = 1;
        while (i < size ) {
            int h2 = (modHash(value) * i + h1) % (size - 1);
            if (table[h2] == value) {
                return h2 ;
            }
            i++;
        }
        return -1;
    }

    private int multiHash(int value){
        double A = value * (Math.sqrt(5.0) - 1.0) / 2.0;
        A = A - (int) A;
        return (int) (size * Math.abs(A)); 
    }
    private int modHash(int value){
        int result = (2*Math.abs(value) + 1) % (size - 1);
        return result < 0 ? result + (size - 1) : result;
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

    public void print(){
        int nullCounter = 0;
        for (int i = 0; i < size ; i++) {
            if (table[i] != 0) {
                int value = table[i];
                System.out.print(value + " index: " + getHash(value));
            }
            else {
                nullCounter++;
            }
            System.out.println("\n");
        }
        System.out.println("Collisions: " + collision);
        System.out.println("Load factor: " + (double) (size - nullCounter) / size );
        System.out.println("Average collision per data: " + (double) collision / size );

    }
}