package knapsack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;


public class files {
    int numberOfTestcases = 0;
    int numberOfPairs = 0;
    Vector<pair<Integer, Integer>> pairs = new Vector<>();
    knapsackGA testcase;

    public void read() throws FileNotFoundException {
        File inputFile = new File("input.txt");
        Scanner reader = new Scanner(inputFile);
        numberOfTestcases = reader.nextInt();
        for (int i = 0; i < numberOfTestcases; i++) {
            numberOfPairs = reader.nextInt();
            int capacity = reader.nextInt();
            for (int j = 0; j < numberOfPairs; j++) {
                pairs.add(new pair(reader.nextInt(), reader.nextInt()));
            }

            testcase = new knapsackGA(numberOfPairs, capacity, pairs);
            testcase.performGA(i + 1);
            pairs.clear();
        }
        reader.close();
    }
}
