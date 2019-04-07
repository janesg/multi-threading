package com.devxpress.forkjoinmax;

class SequentialMaxFinder {

    static int findMax(int[] nums, int low, int high) {

        // The Java 8 streams approach
        // return Arrays.stream(nums, low, high).max().getAsInt();

        int max = nums[low];

        for (int i = low + 1; i < high; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
        }

        return max;
    }
}
