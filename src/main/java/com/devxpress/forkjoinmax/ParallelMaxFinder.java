package com.devxpress.forkjoinmax;

import java.util.concurrent.RecursiveTask;

public class ParallelMaxFinder extends RecursiveTask<Integer> {

    private final int[] nums;
    private final int low;
    private final int high;
    private final int threshold;

    ParallelMaxFinder(int[] nums, int low, int high, int threshold) {
        this.nums = nums;
        this.low = low;
        this.high = high;
        this.threshold = threshold;
    }

    @Override
    protected Integer compute() {

        // Use sequential finder when size of range less than the split threshold
        if ((high - low) < threshold) {
            System.out.println("Using sequential max finder - low : " + low + " / high : " + high);
            return SequentialMaxFinder.findMax(nums, low, high);
        } else {
            // We split into 2 new RecursiveTasks
            int middle = low + ((high - low) / 2);

            System.out.println("New parallel finder (1) - low : " + low + " / high : " + middle);
            ParallelMaxFinder task1 = new ParallelMaxFinder(nums, low, middle, threshold);
            System.out.println("New parallel finder (2) - low : " + (middle + 1) + " / high : " + high);
            ParallelMaxFinder task2 = new ParallelMaxFinder(nums, middle + 1, high, threshold);

            invokeAll(task1, task2);

            return Math.max(task1.join(), task2.join());
        }
     }
}
