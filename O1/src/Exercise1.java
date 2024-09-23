package o1.src;
import java.util.Random;

public class Exercise1{
    public static void main(String[] args) {
        int[] num =  new int[]{-1, 3, -9, 2, 2, -1, 2, -1, -5};
        int[] exercise1_1 = optimalStock(num);

        System.out.println("--- Exercise 1.1 ---");
        System.out.println("Best profit rate: " + exercise1_1[0]);
        System.out.println("Best buy day index: " + exercise1_1[1]);
        System.out.println("Best sell day index: " + exercise1_1[2]);
        System.out.println("--------------------");
        
        System.out.println();
        int[] ranges = new int[]{1000000, 10000000, 100000000};
        int[] exercise1_3;
        System.out.println("--- Exercise 1.3 ---");
        // Test the algorithm with different array sizes
        for (int i = 0; i < ranges.length; i++) {
            num = randomArray(ranges[i]);
            long startTime = System.nanoTime();
            exercise1_3 = optimalStock(num);
            System.out.println("Time taken: " + (System.nanoTime() - startTime)/1000000.0 + " ms");
            System.out.println("Best profit rate: " + exercise1_3[0]);
            System.out.println("Best buy day index: " + exercise1_3[1]);
            System.out.println("Best sell day index: " + exercise1_3[2] + "\n");
        }
        System.out.println("--------------------");
    }

    /**
     * Find the best buy and sell days to maximize profit
     * @param numbers
     */
    static int[] optimalStock(int[] numbers) {
                                                   //  Time complexity analaysis
        int largestSum = 0;                        //  1 assignment
        int sumOfRates = 0;                        //  1 assignment
        int bestBuyDay = 0;                        //  1 assignment
        int bestSellDay = 0;                       //  1 assignment
        int tempBuyDay = 1;                        //  1 assignment

        for (int i = 1; i < numbers.length; i++) { // 1 assignment, n comparisons, n increments

            // Add the current element to the sum

            sumOfRates += numbers[i];              // n additions, n array accesses

            // If the sum is less than the current element, then the sum is the current element
            // Set bestBuyDay at s and bestSellDay at i + 1

            if (largestSum < sumOfRates){          // n comparisons
                largestSum = sumOfRates;           // n assignments
                bestBuyDay = tempBuyDay;           // n assignments
                bestSellDay = i + 1;               // n additions
            }

            // If the sum of rates is less than 0, set the sum to 0 and the temporary buy day at i + 1

            if (sumOfRates < 0) {                  // n comparisons
                sumOfRates = 0;                    // n assignments
                tempBuyDay = i + 1;                // n additions
            }
        }
        
        // Time complexity: 11n + 6 or O(n)
        return new int[]{largestSum, bestBuyDay, bestSellDay};    
    }

    /**
     * Generate an integer array of random integers between -10 and 10
     * @param range
     * @return random integer array
     */
    public static int[] randomArray(int range) {
        Random rng = new Random();
        int[] numbers = new int[range];
        for (int i = 0; i < range; i++) {
            numbers[i] = rng.nextInt(-10, 10);
        }
        return numbers;

    }
}