package com.devxpress.sum;

import java.util.Random;

public class App {
    
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        
        demoSequentialSum();
        
        demoParallelVersusSequentialSum();
    }
    
    private static void demoSequentialSum() {
        int[] nums = random.ints(10000001, -100, 100).toArray();

        long start = System.currentTimeMillis();
        int total = Sum.sumSequential(nums);
        long finish = System.currentTimeMillis();
        
        System.out.println("Sum of array elements : " + total + " / time taken (ms) : " + (finish - start));
    }

    private static void demoParallelVersusSequentialSum() {
        int[] nums = random.ints(20000011, -100, 100).toArray();

        long start = System.currentTimeMillis();
        int total = Sum.sumSequential(nums);
        long finish = System.currentTimeMillis();
        
        System.out.println("\nSequential Sum of array elements : " + total + " / time taken (ms) : " + (finish - start));

        start = System.currentTimeMillis();
        total = Sum.sumParallel(nums, Runtime.getRuntime().availableProcessors());
        finish = System.currentTimeMillis();
        
        System.out.println("Parallel Sum of array elements : " + total + " / time taken (ms) : " + (finish - start));
    }
    
}