package knapsack;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class knapsackGA {

    static double Pc = 0.7;
    static double Pm = 0.01;
    static Random rand = new Random();
    static Vector<String> population = new Vector<>();

    static public void DoCrossover(String chromosome1, String chromosome2) {
        int chromosomeLength = chromosome1.length();
        String offspring1 = chromosome1, offspring2 = chromosome2;
        double r2 = rand.nextDouble();
        if (r2 <= Pc)
        {
            int r1 = (int) Math.floor(Math.random() * ((chromosomeLength - 1) - 1 + 1) + 1);  // da lw point crosssover
            System.out.println(r1);
            offspring1 = chromosome1.substring(0, r1);
            offspring1 += chromosome2.substring(r1, chromosomeLength);

            offspring2 = chromosome2.substring(0, r1);
            offspring2 += chromosome1.substring(r1, chromosomeLength);
        }
        population.add(offspring1);
        population.add(offspring2);

        /*for(String i : population)
            System.out.println(i);*/
    }

    static public void DoMutation(Vector<String> pop)
    {
        for (String p : pop) {
            for (int i = 0; i < p.length(); i++) {
                double r = rand.nextDouble();
                if (r <= Pm)
                {
                    p.replace(p.charAt(i), (char) (Character.getNumericValue(p.charAt(i)) == 0 ? 1 : 0));

                }
            }
        }
    }

    ///testing
    public static void main(String[] args) throws IOException {
        DoCrossover("10111", "01001");
        DoMutation(population);
        System.out.println("size: " + population.size());
        for(String i : population)
            System.out.println(i);


    }
}
