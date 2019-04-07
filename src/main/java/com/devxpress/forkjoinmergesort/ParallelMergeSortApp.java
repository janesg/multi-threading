package com.devxpress.forkjoinmergesort;

import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ParallelMergeSortApp {

    private static final Random random = new Random();

    public static void main(String[] args) {

        System.out.println("*******************");
        System.out.println("** Sorting 1,000 **");
        System.out.println("*******************");
        doIt(1000);

        System.out.println("\n***********************");
        System.out.println("** Sorting 1,000,000 **");
        System.out.println("***********************");
        doIt(1000000);
    }

    private static void doIt(int arraySize) {

        int[] nums = random.ints(arraySize, (-1 * arraySize), arraySize).toArray();
        int[] copyNums = Arrays.copyOf(nums, nums.length);

        System.out.println("=== Sequential Merge Sort ===");
        long start = Instant.now().toEpochMilli();
        MergeSort.sort(nums);
        System.out.println("Time taken (ms) : " + (Instant.now().toEpochMilli() - start));
        // showResults(nums);

        System.out.println("\n=== Parallel Merge Sort ===");
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        int threshold = copyNums.length / Runtime.getRuntime().availableProcessors();
        System.out.println("Split threshold : " + threshold);

        ParallelMergeSortAction action = new ParallelMergeSortAction(copyNums, threshold);

        start = Instant.now().toEpochMilli();
        pool.invoke(action);

        System.out.println("Time taken (ms) : " + (Instant.now().toEpochMilli() - start));
        // showResults(copyNums);
    }

    private static void showResults(int[] nums) {
        Arrays.stream(nums).forEach(i -> System.out.print(i + " "));
    }

}
