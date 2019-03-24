package com.devxpress.sum;

import java.util.Arrays;

public class ParallelWorker extends Thread {
    
    private int[] nums;
    private int low;
    private int high;
    private int sum = 0;
    
    public ParallelWorker(int[] nums, int low, int high) {
        this.nums = nums;
        this.low = low;
        this.high = high;
    }
    
    public int getSum() {
        return this.sum;
    }
    
    @Override
    public void run() {
        sum = Arrays.stream(nums, low, high + 1).sum();
    }
}