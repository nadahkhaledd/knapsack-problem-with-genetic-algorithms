package knapsack;

import java.util.Random;

public class knapsackGA {

    static double Pc = 0.7;
    static double Pm = 0.01;
    static Random rand = new Random();

    static public void DoCrossover(String chromosome1, String chromosome2)
    {
        int chromosomeLength = chromosome1.length();
        String offspring1, offspring2;
        boolean crossover = true;
        double r2 = rand.nextDouble();
        while (crossover)
        {
            if(r2 > Pc) break;
        else
            {
                int r1 = (int)Math.floor(Math.random()*((chromosomeLength-1)-1+1)+1);  // da lw point crosssover




            }
        }



    }
}
