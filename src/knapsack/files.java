package knapsack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class files {
    static int numberOfTestcases = 0;
    static int numberOfPairs = 0;
    //static HashMap<Integer, Integer> pairs = new HashMap<>();

    public static void read() throws FileNotFoundException {
        File inputFile = new File("input.txt");
        Scanner reader = new Scanner(inputFile);
        numberOfTestcases = reader.nextInt();


        reader.close();

    }


}
