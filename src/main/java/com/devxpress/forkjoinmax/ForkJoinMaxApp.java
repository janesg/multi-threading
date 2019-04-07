package com.devxpress.forkjoinmax;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinMaxApp {

    private static final Random random = new Random();

    public static void main(String[] args) {

        System.out.println("******************");
        System.out.println("** Using 10,001 **");
        System.out.println("******************");
        doIt(10001);

        System.out.println("\n***********************");
        System.out.println("** Using 100,000,001 **");
        System.out.println("***********************");
        doIt(100000001);
    }

    private static void doIt(int arraySize) {
        int[] nums = random.ints(arraySize, (-1 * arraySize), arraySize).toArray();

        long start = Instant.now().toEpochMilli();
        System.out.println("Max (sequential) : " + SequentialMaxFinder.findMax(nums, 0, nums.length));
        System.out.println("Time taken (ms) : " + (Instant.now().toEpochMilli() - start));

        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        int threshold = nums.length / Runtime.getRuntime().availableProcessors();
        System.out.println("\nSplit threshold : " + threshold);

        ParallelMaxFinder para = new ParallelMaxFinder(nums, 0, nums.length, threshold);
        start = Instant.now().toEpochMilli();
        System.out.println("Max (parallel) : " + pool.invoke(para));
        System.out.println("Time taken (ms) : " + (Instant.now().toEpochMilli() - start));
    }
}
