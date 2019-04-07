package com.devxpress.forkjoinmergesort;

import java.util.Arrays;

public class MergeSort {

    static void sort(int[] arrayToSort) {

        // Recursive base case:
        // - when only 1 element in array it must be sorted
        if (arrayToSort.length <= 1) {
            return;
        }

        int middle = arrayToSort.length / 2;

        // Create copies of left and right halves
        int[] left = Arrays.copyOfRange(arrayToSort, 0, middle);   // Note, 2nd index is exclusive
        int[] right = Arrays.copyOfRange(arrayToSort, middle, arrayToSort.length);

        sort(left);
        sort(right);

        mergeSortedHalves(left, right, arrayToSort);
    }

    static void mergeSortedHalves(int[] leftHalf, int[] rightHalf, int[] output) {

        if (leftHalf.length + rightHalf.length != output.length) {
            throw new IllegalArgumentException("Combined length of both halves does not equal output length");
        }

        int i = 0;     // Index of the left half
        int j = 0;     // Index of the right half
        int k = 0;     // Index of the original

        // While neither left or right arrays have been exhausted,
        // copy smallest value from either left or right arrays into output
        while (i < leftHalf.length && j < rightHalf.length) {
            if (leftHalf[i] < rightHalf[j]) {
                output[k++] = leftHalf[i++];
            } else {
                output[k++] = rightHalf[j++];
            }
        }

        // If left half not yet exhausted, copy across the remaining items
        while (i < leftHalf.length) {
            output[k++] = leftHalf[i++];
        }

        // If right half not yet exhausted, copy across the remaining items
        while (j < rightHalf.length) {
            output[k++] = rightHalf[j++];
        }
    }
}
