package dev.tylerdclark;

import java.util.stream.IntStream;

/**
 * QuickSort class. To be instantiated for iterative and recursive versions.
 */
public class QuickSort implements SortInterface {

    private int count;
    private long startTime, endTime;


    /**
     * Instantiates a new Quick sort.
     */
    public QuickSort() {
        count = 0;
        startTime = 0;
        endTime = 0;
    }

    /**
     * Starts the time, calls the recursive sort on the list before ending time and checking for correctness of sort.
     *
     * @param list the list
     * @throws UnsortedException if list fails to sort.
     */
    @Override
    public void recursiveSort(int[] list) throws UnsortedException {
        startTime = System.nanoTime();
        recursiveQuicksort(list, 0, list.length - 1);
        endTime = System.nanoTime();

        if (isNotSorted(list)) {
            throw new UnsortedException();
        }

    }


    /**
     * Starts the time, calls the iterative sort on the list before ending time and checking for correctness of sort.
     *
     * @param list the list
     * @throws UnsortedException if list fails to sort.
     */
    @Override
    public void iterativeSort(int[] list) throws UnsortedException {

        startTime = System.nanoTime();
        iterativeQuicksort(list, 0, list.length - 1);
        endTime = System.nanoTime();

        if (isNotSorted(list)) {
            throw new UnsortedException();
        }

    }


    /**
     * Recursive quicksort. Increments the operation counter once per function call.
     * <p>
     * From https://www.geeksforgeeks.org/iterative-quick-sort/
     *
     * @param arr  the arr
     * @param low  the low
     * @param high the high
     */
    public void recursiveQuicksort(int[] arr, Integer low, Integer high) {
        count++; //increment the operation counter
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
            now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            recursiveQuicksort(arr, low, pi - 1);
            recursiveQuicksort(arr, pi + 1, high);
        }

    }

    /**
     * Iterative quicksort. increments the operation counter once per while loop.
     * <p>
     * From https://www.geeksforgeeks.org/iterative-quick-sort/
     *
     * @param arr  the arr
     * @param low  the low
     * @param high the high
     */
    public void iterativeQuicksort(int[] arr, int low, int high) {

        // Create an auxiliary stack
        int[] stack = new int[high - low + 1];

        // initialize top of stack
        int top = -1;

        // push initial values of low and high to stack
        stack[++top] = low;
        stack[++top] = high;

        // Keep popping from stack while is not empty
        while (top >= 0) {

            count++;
            // Pop high and low
            high = stack[top--];
            low = stack[top--];

            // Set pivot element at its correct position
            // in sorted array
            int p = partition(arr, low, high);

            // If there are elements on left side of pivot,
            // then push left side to stack
            if (p - 1 > low) {
                stack[++top] = low;
                stack[++top] = p - 1;
            }

            // If there are elements on right side of pivot,
            // then push right side to stack
            if (p + 1 < high) {
                stack[++top] = p + 1;
                stack[++top] = high;
            }
        }
    }

    /**
     * Partition helper function for both iterative and recursive algorithms. increments the operation count once per
     * call of this function.
     *
     * @param arr
     * @param low
     * @param high
     * @return
     */
    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1); // index of smaller element
        for (int j = low; j <= high - 1; j++) {
            count++;
            // If current element is smaller than or
            // equal to pivot
            if (arr[j] <= pivot) {
                i++;

                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }


    /**
     * returns whether the array is sorted by using IntStream.
     *
     * @param arr to check sorting
     * @return true if not sorted, false if sorted.
     */
    private boolean isNotSorted(int[] arr) {
        return IntStream.range(0, arr.length - 1).anyMatch(i -> arr[i] > arr[i + 1]);
    }

    /**
     * Resets operation count and time.
     */
    public void reset() {
        startTime = 0;
        endTime = 0;
        count = 0;
    }

    /**
     * return count.
     *
     * @return count
     */
    @Override
    public int getCount() {
        return count;
    }

    /**
     * Ending time - start time
     *
     * @return time elapsed
     */
    @Override
    public long getTime() {
        return endTime - startTime;
    }


}

