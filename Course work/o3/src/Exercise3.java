package o3.src;


public class Exercise3 {

    private static int[] randomArray(int n){
        int[] arr = new int[n];
        for(int i = 0; i < n; i++){
            arr[i] = (int)(Math.random() * 100);
        }
        return arr;
    }

    private static int[] createAlternatingArray(int length) {
        // Initialize an array of the given length
        int[] result = new int[length];
        
        // Alternate values for the array
        int value1 = 1; // You can change this to any value you prefer
        int value2 = 2; // You can change this to any value you prefer

        // Fill the array with alternating values
        for (int i = 0; i < length; i++) {
            if ((i & 1) == 1) {
                result[i] = value1;
            } else {
                result[i] = value2;
            }
        }
        
        return result;
    }

    private static int[] sortedArray(int n){
        int[] arr = new int[n];
        for(int i = 0; i < n; i++){
            arr[i] = i;
        }
        return arr;
    }

    private static int[] reversedArray(int n){
        int[] arr = new int[n];
        for(int i = 0; i < n; i++){
            arr[i] = n - i;
        }
        return arr;
    }


    public static boolean checkSum(long original, long sorted){
        return original == sorted;
    }
    private static long calculateSum(int[] arr){
        long sum = 0;
        for(int i = 0; i < arr.length; i++){
            sum += arr[i];
        }
        return sum;
    }

    public static boolean checkOrder(int[] sorted){
        for(int i = 0; i < sorted.length - 1; i++){
            if(sorted[i] > sorted[i + 1]){
                return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }


    public static long timeTaker(TriConsumer<int[], Integer, Integer> function, int[] arr, int begin, int end) {
        long start = System.nanoTime();
        function.accept(arr, begin, end);
        long duration = (System.nanoTime() - start);
        return duration;
    }
    private static String formatWithSpace(long number) {
        return String.format("%,d", number).replace(',', ' ');
    }
    private static void runTest(int size, int low, int high){
        int[] random = randomArray(size);  
        int[] randomC = random.clone();

        int[] alternating = createAlternatingArray(size);
        int[] alternatingC = alternating.clone();

        int[] sorted = sortedArray(size);
        int[] sortedC = sorted.clone();

        int[] reversed = reversedArray(size);
        int[] reversedC = reversed.clone();


        
        System.out.println("----- Quick Sort -----");

        System.out.println(String.format("%-18s | %-10s | %-17s | %-10s | %-8s", 
        "Array type", "Array size", "Time elapsed", "Equal sum", "Sorted"));

        System.out.println(String.format("%-18s | %-10s | %-14s ns | %-10b | %-8b", 
        "Random array", formatWithSpace(size), formatWithSpace(timeTaker(Exercise3::quickSort, random, low, high)), 
        checkSum(calculateSum(random), calculateSum(randomC)), checkOrder(random)));

        System.out.println(String.format("%-18s | %-10s | %-14s ns | %-10b | %-8b", 
        "Alternating array", formatWithSpace(size), formatWithSpace(timeTaker(Exercise3::quickSort, alternating, low, high)), 
        checkSum(calculateSum(alternating), calculateSum(alternatingC)), checkOrder(alternating)));

        System.out.println(String.format("%-18s | %-10s | %-14s ns | %-10b | %-8b", 
        "Sorted array", formatWithSpace(size), formatWithSpace(timeTaker(Exercise3::quickSort, sorted, low, high)), 
        checkSum(calculateSum(sorted), calculateSum(sortedC)), checkOrder(sorted)));

        System.out.println(String.format("%-18s | %-10s | %-14s ns | %-10b | %-8b", 
        "Sorted array", formatWithSpace(size), formatWithSpace(timeTaker(Exercise3::quickSort, reversed, low, high)), 
        checkSum(calculateSum(reversed), calculateSum(reversedC)), checkOrder(alternating)));

        

        System.out.println("----- Dual Pivot Quick Sort -----");

        System.out.println(String.format("%-18s | %-10s | %-17s | %-10s | %-8s", 
        "Array type", "Array size", "Time elapsed", "Equal sum", "Sorted"));

        System.out.println(String.format("%-18s | %-10s | %-14s ns | %-10b | %-8b", 
        "Random array", formatWithSpace(size), formatWithSpace(timeTaker(Exercise3::dualPivotQuickSort, random, low, high)), 
        checkSum(calculateSum(random), calculateSum(randomC)), checkOrder(random)));

        System.out.println(String.format("%-18s | %-10s | %-14s ns | %-10b | %-8b", 
        "Alternating array", formatWithSpace(size), formatWithSpace(timeTaker(Exercise3::dualPivotQuickSort, alternating, low, high)), 
        checkSum(calculateSum(alternating), calculateSum(alternatingC)), checkOrder(alternating)));

        System.out.println(String.format("%-18s | %-10s | %-14s ns | %-10b | %-8b", 
        "Sorted array", formatWithSpace(size), formatWithSpace(timeTaker(Exercise3::dualPivotQuickSort, sorted, low, high)), 
        checkSum(calculateSum(sorted), calculateSum(sortedC)), checkOrder(sorted)));

        System.out.println(String.format("%-18s | %-10s | %-14s ns | %-10b | %-8b", 
        "Sorted array", formatWithSpace(size), formatWithSpace(timeTaker(Exercise3::dualPivotQuickSort, reversed, low, high)), 
        checkSum(calculateSum(reversed), calculateSum(reversedC)), checkOrder(alternating)));

        System.out.println("\n\n");
    

    }




    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static int partition(int[] arr, int low, int high){
        int internalLow, internalHigh;
        int median = median3Sort(arr, low, high);
        int medianValue = arr[median];
        swap(arr, median, high - 1);

        for (internalLow = low, internalHigh = high - 1;;){
            while(arr[++internalLow] < medianValue);
            while(arr[--internalHigh] > medianValue);

        
            swap(arr, internalLow, internalHigh);;    
        }
        swap(arr, internalLow, high - 1);
        return internalLow;
    }


    public static int median3Sort(int[] arr, int low, int high){
        int mid = (low + high) / 2;
        if(arr[low] > arr[mid]){
            swap(arr, low, mid);
        }
        if(arr[mid] > arr[high]){
            swap(arr, mid, high);
            
            if(arr[low] > arr[mid]){
                swap(arr, low, mid);
            }
        }
        return mid;
    }

    public static void quickSort(int[] arr, int low, int high){
        if(high - low > 2){
            int pivot = partition(arr, low, high);
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
        else {
            median3Sort(arr, low, high);
        }
    }



    static void dualPivotQuickSort(int[] arr, 
                               int low, int high)
{
    if (low < high)
    {
        
        // piv[] stores left pivot and right pivot.
        // piv[0] means left pivot and
        // piv[1] means right pivot
        int[] piv;
        piv = dualPartition(arr, low, high);
        
        dualPivotQuickSort(arr, low, piv[0] - 1);
        if (arr[piv[0]] != arr[piv[1]]) {
            dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
        }
        dualPivotQuickSort(arr, piv[1] + 1, high);
    }
}

static int[] dualPartition(int[] arr, int low, int high)
{
    swap(arr, low, low + (high-low) / 3);
    swap(arr, low, high - (high-low) / 3);
    if (arr[low] > arr[high]) swap(arr, low, high);
        
    // p is the left pivot, and q 
    // is the right pivot.
    int j = low + 1;
    int g = high - 1, k = low + 1,
        p = arr[low], q = arr[high];
        
    while (k <= g) 
    {
        
        // If elements are less than the left pivot
        if (arr[k] < p)
        {
            swap(arr, k, j);
            j++;
        }
        
        // If elements are greater than or equal
        // to the right pivot
        else if (arr[k] >= q) 
        {
            while (arr[g] > q && k < g)
                g--;
                
            swap(arr, k, g);
            g--;
            
            if (arr[k] < p)
            {
                swap(arr, k, j);
                j++;
            }
        }
        k++;
    }
    j--;
    g++;
    
    // Bring pivots to their appropriate positions.
    swap(arr, low, j);
    swap(arr, high, g);

    // Returning the indices of the pivots
    // because we cannot return two elements
    // from a function, we do that using an array.
    return new int[] { j, g };
}

    public static void main(String[] args) {
        int[] arraySizes = new int[] {5_000, 50_000, 500_000, 5_000_000, 50_000_000};

        for (int i = 0; i < arraySizes.length; i++) {
            runTest(arraySizes[i], 0, arraySizes[i] - 1);
        }
    }
}
