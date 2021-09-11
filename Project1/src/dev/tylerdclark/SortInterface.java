package dev.tylerdclark;

/**
 * The interface Sort interface.
 */
public interface SortInterface {
    /**
     * Recursive sort.
     *
     * @param list the list
     * @throws UnsortedException the unsorted exception
     */
    void recursiveSort(int[] list) throws UnsortedException;

    /**
     * Iterative sort.
     *
     * @param list the list
     * @throws UnsortedException the unsorted exception
     */
    void iterativeSort(int[] list) throws UnsortedException;

    /**
     * Gets count.
     *
     * @return the count
     */
    int getCount();

    /**
     * Gets time.
     *
     * @return the time
     */
    long getTime();
}
