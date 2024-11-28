import java.util.Arrays;

public class CountingSort{
    private final int[] arr;
    public CountingSort(int[] arr){
        this.arr = arr;
    }

    public void sort(int min, int max){
    
    }
    public void sort(){
        int min = Arrays.stream(arr).min().orElse(0);
        int max = Arrays.stream(arr).max().orElse(Integer.MAX_VALUE);

        int[] countArray = new int[max - min + 1];

        for(int value : arr){
            countArray[value - min]++;
        }
        System.out.println("Count array: " + Arrays.toString(countArray));

        int arrayIndex = 0;
        for (int i = 0; i < max - min + 1; i++){
            while(countArray[i] > 0){
                arr[arrayIndex] = i + min;
                countArray[i]--;
                arrayIndex++;
            }
        }
    }

    public static void main(String[] args){
        int[] arr = {0, 0, 2, 5, 0, 1, 3, 5, -2, 4, 3, 3, 1};
        System.out.println("Original array: " + Arrays.toString(arr));
        new CountingSort(arr).sort();
        System.out.println("Sorted array: " + Arrays.toString(arr));
    }
}
