package o4.src.Del2;

import java.util.Hashtable;

class TestHash {
  private int[] arr;
  private int kollisjon;

  public TestHash(int length) {
    arr = new int[(int) Math.pow(2, Math.ceil(Math.log(length)/Math.log(2)))];
    System.out.println(arr.length);
    kollisjon = 0;
  }

  public void push(int x) {
    int h1 = multiHash(x);
    if(arr[h1] == 0){
      arr[h1] = x;
    } else {
      int counter = 1;
      while(counter < arr.length){
        int h2 = (modHash(x)*counter+h1)%(arr.length-1);
        if(arr[h2] == 0){
          arr[h2] = x;
          break;
        } else {
          counter ++;
          kollisjon++;
        }
      }
    }
  }

  public int checkContent(int[] array){
    int counter = 0;
    for (int i : array) {
        counter++;
        if (get(i) == 0) {
            System.out.println("Value not found: " + i);
            return i;
        }
    }
    System.out.println(counter);
    return -1;
}

  public int get(int x){
    int h1 = multiHash(x);
    if(arr[h1] == x){
      return arr[h1];
    } else {
      int counter = 1;
      while(counter < arr.length){
        int h2 = (modHash(x)*counter+h1)%(arr.length-1);
        if(arr[h2] == x){
          return arr[h2];
        } else {
          counter ++;
        }
      }
    }
    return -1;
  }

  public int multiHash(int k) {
    double A = k* ((Math.sqrt(5.0)-1.0)/2.0);
    A -= (int) A;
    return (int) (arr.length * Math.abs(A));
  }

  public int modHash(int k) {
    return ((2*Math.abs(k) + 1) % (arr.length-1));
  }
  public void hashList(int[] array){
    for (int i : array) {
        push(i);
    }
}

public int getHash(int value){
    int h1 = multiHash(value);
    if(arr[h1] == value){
        return h1;
    }
    
    int i = 1;
    while (i < arr.length ) {
        int h2 = (modHash(value) * i + h1) % (arr.length - 1);
        if (arr[h2] == value) {
            return h2 ;
        }
        i++;
    }
    return -1;
}
public void print(){

    for (int i = 0; i < arr.length ; i++) {
        System.out.println(arr[i] + " at index: " + i);
        
        System.out.println("\n");
        
    }

}

  private static int[] randomArray(int length, int testNumber){
    int range = 10 * length;
    int[] list = new int[length];
    list[0] = testNumber;
    for (int i = 1; i < length; i++) {
        list[i] = (int) (Math.random() * range);
    }
    return list;
}
  public static void main(String[] args) {

    int range = 10_000_000;
    int find = 15;
    TestHash ht = new TestHash(range);

    long start;
    long end;
    long tot = 0;
    int random = 0;
    ht.push(find);
    int[] list = randomArray(range, find);
    start = System.nanoTime();
    ht.hashList(list);
    end = System.nanoTime();
    tot += end-start;
    
    System.out.println("Time to fill my tabel: " + tot/1000000+"ms");
    System.out.println("Kollisjoner: " +ht.kollisjon);
    System.out.println("Test number: " + find + " = " + ht.get(find));
    System.out.println("Hashtable contains test number: " + ht.get(find));
    tot = 0;
    Hashtable<Integer, Integer> h = new Hashtable<Integer, Integer>();
    for(int i = 0; i < list.length; i++){
      random = list[i];
      start = System.nanoTime();
      h.put(i, random);
      end = System.nanoTime();
      tot += end-start;
    }
    System.out.println("Time to fill java tabel: " + tot/1000000+"ms");
    int contains = ht.checkContent(list);
        if (contains == -1) {
            System.out.println("Yes");
        } else {
            System.out.println("index: " + ht.get(contains));
            System.out.println("No");
        }
  }
}