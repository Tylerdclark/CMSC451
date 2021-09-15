package dev.tylerdclark;


import java.util.Arrays;

public class Utilities {

    /**
     * Calculates the average using the formula: sum of data / data count
     *
     * @param data to be averaged
     * @return average value
     */
    protected static double getAverage(double[] data) {
        double sum = Arrays.stream(data).sum();
        return sum / data.length;
    }

    /**
     * Creates an array of random numbers between 0 and 999
     * @param n length of array
     * @return array of random numbers
     */
    protected static int[] getData(int n) {
        int[] data = new int[n];
        for (int i = 0; i < n; i++)
            data[i] = (int) (Math.random() * 1000);
        return data;
    }

    /**
     * Calculates the standard deviation which is how close the data is to average.
     * Formula from https://en.wikipedia.org/wiki/Standard_deviation
     *
     * @param data to be analyzed
     * @return standard deviation of a collection
     */
    protected static double getStandardDeviation(double[] data) {
        double sum = 0;
        for (double datum : data) {
            sum += (datum - getAverage(data)) * (datum - getAverage(data));
        }
        return Math.sqrt(sum / (data.length - 1));
    }

    /**
     * Shows the extent of variability in relation to the average.
     * Formula from https://en.wikipedia.org/wiki/Coefficient_of_variation
     *
     * @param data to be analyzed
     * @return coefficient of variance
     */
    protected static double getCoefficientOfVariance(double[] data) {
        return ((getStandardDeviation(data)) / getAverage(data)) * 100;
    }

    /**
     * Using the manual JVM warmup method mentioned in the article:
     * https://www.baeldung.com/java-jvm-warmup
     */
    protected static void JVMWarmUp() {
        for (int i = 0; i < 100000; i++) {
            ManualClass manualClass = new ManualClass();
            manualClass.method();
        }
    }

    /**
     * Manual class to be instantiated for the purpose of JVM warmup
     */
    public static class ManualClass {
        public void method() {
        }
    }


}
