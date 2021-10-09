package dev.tylerdclark;


import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import static dev.tylerdclark.Utilities.*;

/**
 * The type Benchmark sorts.
 */
public class BenchmarkSorts {

    private static final int DATASET_COUNT = 50;
    private static final int[] DATASET_SIZES = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
    private static final int RESULT_ROW_COUNT = 5;
    private final long[][] recursiveTime;
    private final long[][] iterativeTime;
    private final Object[][] recursiveResultForm;
    private final Object[][] iterativeResultForm;
    private final int[][] recursiveCount;
    private final int[][] iterativeCount;
    private final QuickSort recursiveSort;
    private final QuickSort iterativeSort;
    private final Scanner scanner;
    private final JFileChooser fileChooser;
    private File file;

    /**
     * Instantiates a new Benchmark sorts.
     */
    BenchmarkSorts() {
        scanner = new Scanner(System.in);
        recursiveTime = new long[DATASET_SIZES.length][DATASET_COUNT];
        iterativeTime = new long[DATASET_SIZES.length][DATASET_COUNT];
        recursiveCount = new int[DATASET_SIZES.length][DATASET_COUNT];
        iterativeCount = new int[DATASET_SIZES.length][DATASET_COUNT];
        recursiveResultForm = new Object[DATASET_SIZES.length][RESULT_ROW_COUNT];
        iterativeResultForm = new Object[DATASET_SIZES.length][RESULT_ROW_COUNT];
        recursiveSort = new QuickSort();
        iterativeSort = new QuickSort();
        fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));

    }

    /**
     * Menu. receives feedback from user and calls other functions that split up functionality.
     */
    public void menu() {
        System.out.println("***Project 1 - QuickSort Analysis***");
        System.out.println("Proceed to tests? (Y/N)");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("y")) {
            runTests();
            outputFile();
            System.out.println("Proceed to select file for report viewing? (Y/N)");
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("y")) {
                selectFile();
                generateReport();
                showReport();
            }
        }
        System.out.println("Thank you for using the program!");
    }

    /**
     * Warmups up JVM, creates data for both sorting versions (recursive and iterative) then tries to sort them.
     * Handles {@link UnsortedException} if array is not sorted. Places operation count and time into arrays.
     */
    private void runTests() {
        //Warm up JVM
        Utilities.JVMWarmUp();
        //for all 10 data sizes
        for (int i = 0; i < DATASET_SIZES.length; i++) {
            int dataSize = DATASET_SIZES[i];
            //run the sort 50 times (recursively and iteratively)
            for (int j = 0; j < DATASET_COUNT; j++) {
                int[] data = getData(dataSize);
                int[] data2 = data.clone(); //same data but not a reference
                try {
                    recursiveSort.recursiveSort(data);
                } catch (UnsortedException e) {
                    System.err.println("Recursive data failed to sort!");
                    System.err.println(Arrays.toString(data));
                }
                try {
                    iterativeSort.iterativeSort(data2);
                } catch (UnsortedException e) {
                    System.err.println("Iterative data failed to sort!");
                    System.err.println(Arrays.toString(data2));
                }
                //populate the arrays
                recursiveCount[i][j] = recursiveSort.getCount();
                iterativeCount[i][j] = iterativeSort.getCount();
                recursiveTime[i][j] = recursiveSort.getTime();
                iterativeTime[i][j] = iterativeSort.getTime();

                recursiveSort.reset(); //setting count and size back to 0
                iterativeSort.reset();

            }
        }
    }

    /**
     * writes the time and operation count data to a file. Handles {@link IOException} if issues with {@link BufferedWriter}
     */
    private void outputFile() {
        System.out.println("Outputting file..");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("QS_benchmark_output.txt"), StandardCharsets.UTF_8))) {

            writeData(writer, recursiveCount, recursiveTime);
            writer.write("\n");//new line to separate iterative and recursive
            writeData(writer, iterativeCount, iterativeTime);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function for {@link #outputFile}
     * @param writer buffered writer
     * @param counts iterative or recursive count nested array
     * @param times iterative or recursive time nested array
     * @throws IOException if issues with {@link BufferedWriter}
     */
    private void writeData(Writer writer, int[][] counts, long[][] times) throws IOException {
        for (int i = 0; i < DATASET_SIZES.length; i++) {
            writer.write(DATASET_SIZES[i] + " ");
            for (int j = 0; j < DATASET_COUNT; j++) {
                writer.write(counts[i][j] + "," + times[i][j] + " ");
            }
            writer.write("\n");
        }
    }

    /**
     * Use JFileChooser to select file and save to data member
     */
    private void selectFile() {
        System.out.println("Select file (dialog may be below other windows)");
        int result = fileChooser.showOpenDialog(null); //will show under all windows on macOS due to this being CL
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }

    }

    /**
     * Reads in the selected file from {@link #selectFile()} and calls {@link #parseStringData(String, int)} to parse.
     */
    private void generateReport() {
        System.out.println("Generating report..");
        if (file != null) {
            try {
                BufferedReader in;
                in = new BufferedReader(new FileReader(file));
                String line = in.readLine();
                int resultsIndex = 0;
                while (line != null) {
                    parseStringData(line, resultsIndex);
                    resultsIndex++;
                    line = in.readLine();
                }

            } catch (IOException e) {
                System.err.println("IO Exception parsing: " + file.getName());
            }

        } else {
            System.err.println("File object is null");
        }
    }

    /**
     * Generates two {@link JFrame} objects with {@link JTable} to view report.
     */
    private void showReport() {
        //create a report table for each sort type
        String[] columnNames = {"Size", "Avg Count", "Coef Count", "Avg Time", "Coef Time"};
        JFrame recursiveFrame = new JFrame("Recursive Quicksort Report");
        JFrame iterativeFrame = new JFrame("Iterative Quicksort Report");
        recursiveFrame.setBounds(30, 40, 400, 215);
        iterativeFrame.setBounds(60, 80, 400, 215);
        JScrollPane recursiveSP = new JScrollPane(new JTable(recursiveResultForm, columnNames));
        JScrollPane iterativeSP = new JScrollPane(new JTable(iterativeResultForm, columnNames));
        recursiveFrame.add(recursiveSP);
        iterativeFrame.add(iterativeSP);
        recursiveFrame.setVisible(true);
        iterativeFrame.setVisible(true);
        recursiveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        iterativeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Helper function for {@link #parseLineData(int, String[], double[], double[], Object[][], Object[])}
     * @param line string read from file
     * @param index which line
     */
    private void parseStringData(String line, int index) {
        String[] stringData = line.split(" ");
        double[] countData = new double[DATASET_COUNT];
        double[] timeData = new double[DATASET_COUNT];

        if (index < 10) { //The recursive data
            parseLineData(index, stringData, countData, timeData, recursiveResultForm, recursiveResultForm[index]);
            //10th index is the space separating the two
        } else if (index > 10) { //the iterative data
            int iterativeIndex = index - 11; //so the 11th index becomes 0
            parseLineData(iterativeIndex, stringData, countData, timeData, iterativeResultForm, iterativeResultForm[iterativeIndex]);
        }
    }

    /**
     * helper function for {@link #parseStringData(String, int)} actually
     * calculates average, and coefficient of variance for the data before saving it to be displayed
     * @param index which line
     * @param stringData arr of strings
     * @param countData time arr
     * @param timeData count arr
     * @param resultForm holds the results
     * @param resultsRow holds the rows of the results
     */
    private void parseLineData(int index, String[] stringData, double[] countData, double[] timeData, Object[][] resultForm, Object[] resultsRow) {
        resultForm[index][0] = stringData[0];
        for (int i = 1; i <= DATASET_COUNT; i++) {
            String val = stringData[i];
            String[] countAndTime = val.split(",");
            countData[i - 1] = Double.parseDouble(countAndTime[0]);
            timeData[i - 1] = Double.parseDouble(countAndTime[1]);
        }
        resultsRow[1] = String.format("%.2f", getAverage(countData));
        resultsRow[2] = String.format("%.2f", getCoefficientOfVariance(countData));
        resultsRow[3] = String.format("%.2f", getAverage(timeData));
        resultsRow[4] = String.format("%.2f", getCoefficientOfVariance(timeData));
    }

}
