package o2;

import java.lang.Math;
import java.util.function.BiFunction;
public class Exercise2 {
    
    public static void main(String[] args) {
        double base = 1.0002;

        int[] exponents = new int[] {10, 100, 1000, 10000};
        
        long start;
        double result;
        double duration;


        for (int exponent : exponents) {
            start = System.nanoTime();
            result = recursive_exponent_v1(base, exponent);
            duration = (System.nanoTime() - start);
            System.out.println("Time elapsed in recursive_exponent_v1(), " + result + " is: " + duration + " ns\n");
        }
        for (int exponent : exponents) {
            start = System.nanoTime();
            result = recursive_exponent_v2(base, exponent);
            duration = (System.nanoTime() - start);
            System.out.println("Time elapsed in recursive_exponent_v2(), " + result + " is: " + duration + " ns\n");
        }
        for (int exponent : exponents) {
            start = System.nanoTime();
            result = Math.pow(base, exponent);
            duration = (System.nanoTime() - start);
            System.out.println("Time elapsed in Math.pow(), " + result + " is: " + duration + " ns\n");
        }
    }

    public static double recursive_exponent_v1(double base, int exponent){
        if (exponent == 1){
            return base;
        }
        else {
            return base * recursive_exponent_v1(base, exponent - 1);
        }
    }


    public static double recursive_exponent_v2(double base, int exponent) {
        if (exponent == 1){
            return base;
        }
        else if ((exponent & 1) == 1) {
            double temp = recursive_exponent_v2(base * base, (exponent - 1) / 2);
            return base * temp; 
        } else {
            double temp = recursive_exponent_v2(base * base, exponent / 2);
            return temp;
        }
    }

    private static void timeTaker(BiFunction<Double, Integer, Double> function, double x, int n) {
        long start = System.nanoTime();
        double result;
        do{
            result = function.apply(x, n);
        } while (System.nanoTime() - start < 1000000000);
        
        long duration = System.nanoTime() - start;
        System.out.println("Time elapsed in " + function + ", " + result + " is: " + duration + " ns\n");
    }
    
}

