package com.devxpress.sum;

import java.util.Arrays;

public class Sum {
    
    public static int sumSequential(int[] nums) {
        return Arrays.stream(nums).sum();
    }
    
    public static int sumParallel(int[] nums, int numOfThreads) {
        
        if (numOfThreads <= 1) {
            return sumSequential(nums);
        }
        
        // How big is each sub-array going to be ?
        int subSize = nums.length / numOfThreads;
        
        if (subSize <= 1) {
            return sumSequential(nums);
        }
        
        ParallelWorker[] workers = new ParallelWorker[numOfThreads];
        
        for (int i = 0; i < numOfThreads; i++) {
            int low = i * subSize;
            int high = (i == numOfThreads - 1) ? nums.length - 1 : low + (subSize - 1);
            System.out.println("Creating worker : low = " + low + " / high = " + high);
            workers[i] = new ParallelWorker(nums, low, high);
            workers[i].start();
        }
        
        try {
            for (int i = 0; i < workers.length; i++) {
                workers[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int total = 0;
        
        for (int i = 0; i < workers.length; i++) {
            total += workers[i].getSum();
        }
        
        return total;
    }
}