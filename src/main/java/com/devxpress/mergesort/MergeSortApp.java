package com.devxpress.mergesort;

import java.util.Arrays;
import java.util.Random;

public class MergeSortApp {
    
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        
        demoSequential();
        
        // Large number of elements... parallel should be faster
        System.out.print("\n\nParallel merge sort with large number of items...");
        demoParallelVersusSequential(5000000);

        // Small number of elements... sequential should be faster
        System.out.print("\nParallel merge sort with small number of items...");
        demoParallelVersusSequential(10000);
        
    }
    
    private static void demoSequential() {

        int[] nums = random.ints(25, -1000, 1000).toArray();

        System.out.println("Original array : ");
        showResults(nums);
        System.out.print("\n");

        MergeSort sms = new MergeSort(nums);
        sms.mergeSort(0, nums.length - 1);

        System.out.println("Sorted array : ");
        showResults(nums);

    }
    
    private static void demoParallelVersusSequential(int numOfElements) {

        int numOfThreads = Runtime.getRuntime().availableProcessors();

        System.out.println("Number of threads == Number of processors : " + numOfThreads + "\n");

        int[] nums = random.ints(numOfElements, numOfElements * -1, numOfElements).toArray();
        int[] copyNums = Arrays.copyOf(nums, nums.length);

        MergeSort sms = new MergeSort(nums);
        
        long start = System.currentTimeMillis();
        sms.mergeSort(0, nums.length - 1);
        long finish = System.currentTimeMillis();
        
        System.out.println("Sequential sort on " + numOfElements + " items (ms) : " + (finish - start));

        sms = new MergeSort(copyNums);
        
        start = System.currentTimeMillis();
        sms.mergeSortParallel(0, copyNums.length - 1, numOfThreads);
        finish = System.currentTimeMillis();
        
        System.out.println("Parallel sort on " + numOfElements + " items (ms) : " + (finish - start));
    }
    
    private static void showResults(int[] nums) {
        Arrays.stream(nums).forEach(i -> System.out.print(i + " "));
    }
    
}