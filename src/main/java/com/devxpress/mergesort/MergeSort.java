package com.devxpress.mergesort;

import java.util.Arrays;

public class MergeSort {
    
    private int[] nums;
    private int[] temp;
    
    // Time order complexity : O(n log(n))
    public MergeSort(int[] nums) {
        this.nums = nums;
        // Allocate the temporary space once
        this.temp = new int[nums.length];
    }
    
    public void mergeSortParallel(int low, int high, int numOfThreads) {
        
        if (numOfThreads <= 1) {
            // Just use the sequential merge sort
            mergeSort(low, high);
            return;
        }
        
        int middle = low + ((high - low) / 2);
        
        // Half threads used for left array sort and the other half for right
        // If odd number of threads, give the extra one to the left which could have an extra item
        int rightThreadCount = numOfThreads / 2;
        Thread leftSorter = createParallelSortThread(low, middle, numOfThreads - rightThreadCount);
        Thread rightSorter = createParallelSortThread(middle + 1, high, rightThreadCount);
        
        leftSorter.start();
        rightSorter.start();
        
        try {
            // Wait for both to finish...
            leftSorter.join();
            rightSorter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        merge(low, middle, high);
    }
    
    private Thread createParallelSortThread(int low, int high, int numOfThreads) {
        return new Thread(() -> {
            mergeSortParallel(low, high, numOfThreads);
        });
    }
    
    public void mergeSort(int low, int high) {
        
        // Recursive base case
        if (low >= high) {
            return;
        }
        
        // Avoid potential integer overflow
        // int middle = (low + high) / 2;
        int middle = low + ((high - low) / 2);
        
        mergeSort(low, middle);
        mergeSort(middle + 1, high);
        merge(low, middle, high);
    }
    
    private void merge(int low, int middle, int high) {
        
        // We copy the values from the specific portion of the original array to 
        // the associated portion of the temp array and use the temp array for reference.
        // We do this so that we can write the changes back to the original input array.
        for (int i = low; i <= high; i++) {
            temp[i] = nums[i];
        }

        int i = low;            // Index of the left-hand (low half) input array
        int j = middle + 1;     // Index of the right-hand (high half) input array
        int k = low;            // Index of the output array (the original array)
        
        // While neither left or right arrays have been exhausted,
        // copy smallest value from either left or right arrays into output
        while (i <= middle && j <= high) {
            if (temp[i] <= temp[j]) {
                nums[k] = temp[i];
                i++;
            } else {
                nums[k] = temp[j];
                j++;
            }
            
            k++;
        }

        // If left array not yet exhausted, copy across the remaining items
        while (i <= middle) {
            nums[k] = temp[i];
            i++;
            k++;            
        }

        // If right array not yet exhausted, copy across the remaining items
        while (j <= high) {
            nums[k] = temp[j];
            j++;
            k++;            
        }
    }
    
}