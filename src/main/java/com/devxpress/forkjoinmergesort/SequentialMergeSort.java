package com.devxpress.forkjoinmergesort;

import java.util.Arrays;

public class SequentialMergeSort {

    static void mergeSort(int[] arrayToSort) {

        // Recursive base case:
        // - when only 1 element in array it must be sorted
        if (arrayToSort.length <= 1) {
            return;
        }

        int middle = arrayToSort.length / 2;

        // Create copies of left and right halves
        int[] left = Arrays.copyOfRange(arrayToSort, 0, middle);   // Note, 2nd index is exclusive
        int[] right = Arrays.copyOfRange(arrayToSort, middle, arrayToSort.length);

        mergeSort(left);
        mergeSort(right);

        mergeHalves(left, right, arrayToSort);
    }

    static void mergeHalves(int[] leftHalf, int[] rightHalf, int[] original) {

        int i = 0;     // Index of the left half
        int j = 0;     // Index of the right half
        int k = 0;     // Index of the original

        // While neither left or right arrays have been exhausted,
        // copy smallest value from either left or right arrays into output
        while (i < leftHalf.length && j < rightHalf.length) {
            if (leftHalf[i] < rightHalf[j]) {
                original[k++] = leftHalf[i++];
            } else {
                original[k++] = rightHalf[j++];
            }
        }

        // If left half not yet exhausted, copy across the remaining items
        while (i < leftHalf.length) {
            original[k++] = leftHalf[i++];
        }

        // If right half not yet exhausted, copy across the remaining items
        while (j < rightHalf.length) {
            original[k++] = rightHalf[j++];
        }
    }
}
