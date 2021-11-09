package knapsack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;


public class files {
    static int numberOfTestcases = 0;
    static int numberOfPairs = 0;
    static Vector<pair<Integer,Integer>> pairs = new Vector<>();
    static knapsackGA testcase = new knapsackGA();

    public static void read() throws FileNotFoundException {
        File inputFile = new File("input.txt");
        Scanner reader = new Scanner(inputFile);
        numberOfTestcases = reader.nextInt();
        for (int i = 0; i < numberOfTestcases; i++) {
            numberOfPairs = reader.nextInt();
            int capacity = reader.nextInt();
            for (int j = 0; j < numberOfPairs; j++) {
                pairs.add( new pair(reader.nextInt(), reader.nextInt()));
            }
            System.out.println(numberOfPairs + "\n" + capacity);
            for (pair p : pairs) {
                System.out.println(p.key + " " + p.value);
            }
            testcase = new knapsackGA(numberOfPairs,capacity,pairs);
            //call performGA()
            pairs.clear();
        }
        reader.close();

    }

    public static void main(String[] args) throws IOException {

        read();
    }


}
