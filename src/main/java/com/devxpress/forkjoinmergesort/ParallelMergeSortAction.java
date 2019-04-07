package com.devxpress.forkjoinmergesort;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSortAction extends RecursiveAction {

    private final int[] nums;
    private final int threshold;

    ParallelMergeSortAction(int[] nums, int threshold) {
        this.nums = nums;
        this.threshold = threshold;
    }

    @Override
    protected void compute() {

        if (nums.length <= threshold) {
            SequentialMergeSort.mergeSort(nums);
            return;
        }

        int middle = nums.length / 2;

        // Create copies of left and right halves
        int[] left = Arrays.copyOfRange(nums, 0, middle);   // Note, 2nd index is exclusive
        int[] right = Arrays.copyOfRange(nums, middle, nums.length);

        ParallelMergeSortAction taskLeft = new ParallelMergeSortAction(left, threshold);
        ParallelMergeSortAction taskRight = new ParallelMergeSortAction(right, threshold);

        invokeAll(taskLeft, taskRight);

        SequentialMergeSort.mergeHalves(left, right, nums);
    }

}
