package knapsack;

import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class knapsackGA {
    static int numberOfPairs;
    static int capacity;
    static Vector<pair<Integer, Integer>> pairs = new Vector<>();
    static double Pc = 0.7;
    static double Pm = 0.01;
    static Random rand = new Random();
    static Set<String> population = new TreeSet<>();
    static Vector<pair<String, Integer>> fitnessValues = new Vector<>();
    static Set<String> selectedChromosomes = new TreeSet<>();

    public knapsackGA(int numberOfPairs, int capacity, Vector<pair<Integer, Integer>> pairs)
    {
        this.numberOfPairs = numberOfPairs;
        this.capacity = capacity;
        this.pairs = pairs;
    }

     static public int factorialOf(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorialOf(n - 1);
    }

    static public String generateChromosome(int numberOfPairs)
    {
        String chromosome = "";
        for(int i=0; i<numberOfPairs; i++)
        {
              chromosome += Math.round(rand.nextDouble());
        }
        return chromosome;
    }

    static public int randomizePopulation(int numberOfPairs)
    {
        int N =  2;
        int result = (int)(factorialOf(numberOfPairs)  * 0.01);
        if(result > N) N = result;

        return N;
    }

    static public int getWeight(String chromosome)
    {
        int totalWeight = 0;
        for(int i=0; i<chromosome.length(); i++)
        {
            if(chromosome.charAt(i) == '1')
            {
                totalWeight += pairs.get(i).weight;
            }
        }
        return totalWeight;
    }

    static public int getFitness(String chromosome)
    {
        int totalValue = 0;
        for(int i=0; i<chromosome.length(); i++)
        {
            if(chromosome.charAt(i) == '1')
            {
                totalValue += pairs.get(i).value;
            }
        }
        return totalValue;
    }

    static public void DoSelection()
    {
         Vector<Integer> comulative = new Vector<>();
         int sum =0;
         for(int i=0; i<fitnessValues.size(); i++)
         {
             comulative.add(sum);
             sum+= fitnessValues.get(i).value;
         }
         comulative.add(sum);
         for(int i=0; i<fitnessValues.size(); i++)
         {
             int r = (int) Math.floor(Math.random() * sum);
             for(int j=0; j<comulative.size(); j++)
             {
                 if(r < comulative.get(j))
                 {
                     selectedChromosomes.add(fitnessValues.get(j-1).weight);
                     break;
                 }
             }
         }
    }

    static public void DoCrossover(String chromosome1, String chromosome2) {
        int chromosomeLength = chromosome1.length();
        String offspring1 = chromosome1, offspring2 = chromosome2;
        double r2 = rand.nextDouble();
        if (r2 <= Pc)
        {
            System.out.println("\ncrossover happened");
            int r1 = (int) Math.floor(Math.random() * ((chromosomeLength - 1) - 1 + 1) + 1);  // da lw point crosssover
            System.out.println("crossover point:" + r1);
            offspring1 = chromosome1.substring(0, r1);
            offspring1 += chromosome2.substring(r1, chromosomeLength);

            offspring2 = chromosome2.substring(0, r1);
            offspring2 += chromosome1.substring(r1, chromosomeLength);
        }
        population.add(offspring1);
        population.add(offspring2);

        System.out.println("\nafter crossover:");
        for(String i : population)
            System.out.println(i);
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

        System.out.println("\nafter mutation:");
        for(String i : population)
            System.out.println(i);
    }

    static public void DoReplacement()
    {

    }

    static public void performGA()
    {
        int popSize = randomizePopulation(numberOfPairs);
        while(population.size() < popSize)
        {
            String chromosome = generateChromosome(numberOfPairs);
            while (getWeight(chromosome) > capacity)
            {
                chromosome = generateChromosome(numberOfPairs);
            }
            population.add(chromosome);
            int fitness = getFitness(chromosome);
            fitnessValues.add(new pair<>(chromosome,fitness));

        }
        DoSelection();


    }

    ///testing
    public static void main(String[] args) throws IOException {
//        DoCrossover("10110", "01001");
//        DoMutation(population);
//        System.out.println("size: " + population.size());


    }
}
