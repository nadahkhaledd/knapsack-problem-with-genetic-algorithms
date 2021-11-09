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
    static Vector<String> population = new Vector<>();
    static Vector<pair<String, Integer>> fitnessValues = new Vector<>();
    static Vector<String> selectedChromosomes = new Vector<>();
    static Vector<pair<String, Integer>> offSprings = new Vector<>();

    public knapsackGA(){}

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

    static public String generateChromosome()
    {
        String chromosome = "";
        for(int i=0; i<numberOfPairs; i++)
        {
              chromosome += Math.round(rand.nextDouble());
        }
        return chromosome;
    }

    static public int randomizePopulation()
    {
        int N =  2;
        int result = (int)(factorialOf(numberOfPairs)  * 0.01);
        if(result > N) N = (result%2 == 0 ? result : result+1);

        return N;
    }

    static public int getWeight(String chromosome)
    {
        int totalWeight = 0;
        for(int i=0; i<chromosome.length(); i++)
        {
            if(chromosome.charAt(i) == '1')
            {
                totalWeight += pairs.get(i).key;
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
                     selectedChromosomes.add(fitnessValues.get(j-1).key);
                     break;
                 }
             }
         }
    }

    static public void DoCrossover(String chromosome1, String chromosome2) {
        int chromosomeLength = chromosome1.length();
        String offspring1,offspring2;
        while(true){
            offspring1 = chromosome1;
            offspring2 = chromosome2;
            double r2 = rand.nextDouble();
            if (r2 <= Pc)
            {
//            System.out.println("\ncrossover happened");
                int r1 = (int) Math.floor(Math.random() * ((chromosomeLength - 1)) + 1);  // da lw point crosssover
//            System.out.println("crossover point:" + r1);
                offspring1 = chromosome1.substring(0, r1);
                offspring1 += chromosome2.substring(r1, chromosomeLength);

                offspring2 = chromosome2.substring(0, r1);
                offspring2 += chromosome1.substring(r1, chromosomeLength);
            }
            if(getWeight(offspring1) <= capacity && getWeight(offspring2) <= capacity) {
                break;
            }
        }
        offSprings.add(new pair<>(offspring1,getFitness(offspring1)));
        offSprings.add(new pair<>(offspring2,getFitness(offspring2)));

        // System.out.println("\nafter crossover:");
        // for(String i : offSprings)
        //     System.out.println(i);
    }

    static public void DoMutation()
    {
        for (int i=0; i<offSprings.size(); i++) {
            String temp = "";
            for (int j = 0; j < offSprings.get(i).key.length(); j++) {
                double r = rand.nextDouble();
                if (r <= Pm)
                {
                    temp = offSprings.get(i).key.substring(0, j)
                            + (offSprings.get(i).key.charAt(j) == '0' ? '1' : '0')
                            + offSprings.get(i).key.substring(j+1);
                    if(getWeight(temp) <= capacity){
                        offSprings.get(i).key = temp;
                    }
                }
            }
        }

        // System.out.println("\nafter mutation:");
        // for(String i : offSprings)
        //     System.out.println(i);
    }

    static public void DoReplacement()
    {
        population.clear();
        fitnessValues.clear();
       // System.out.println("dddddd" + offSprings.size());
        for(int i = 0; i < offSprings.size(); i++){
            population.add(offSprings.get(i).key);
            fitnessValues.add(new pair<>(offSprings.get(i).key,getFitness(offSprings.get(i).key)));
        }
        offSprings.clear();
        selectedChromosomes.clear();
    }

    public static void performGA(int caseNo)
    {
        int popSize = randomizePopulation();
        while(population.size() < popSize)
        {
            String chromosome = generateChromosome();
            while (getWeight(chromosome) > capacity)
            {
                chromosome = generateChromosome();
            }
            population.add(chromosome);
            int fitness = getFitness(chromosome);
            fitnessValues.add(new pair<>(chromosome,fitness));
        }
        int turn = 0;
        String goodone = "";
        while (turn < 500){
            DoSelection();
            for(int i=0; i<selectedChromosomes.size(); i+=2){
                DoCrossover(selectedChromosomes.get(i), selectedChromosomes.get(i+1));
            }
            DoMutation();
            DoReplacement();
            goodone = (getFitness(goodone) > getFitness(getMax()) ? goodone : getMax());
            turn++;
        }
        print(goodone,caseNo);
    }

    public static void print(String goodone, int caseNo){
        System.out.println("Weight = " + getWeight(goodone));
        String output = "";
        int numberOfItems = 0;
        for(int i = 0; i < goodone.length(); i++){
            if(goodone.charAt(i) == '1'){
                numberOfItems++;
                output += pairs.get(i).key + " " + pairs.get(i).value + '\n';
            }
        }
        System.out.println("Case " + caseNo + ": " + getFitness(goodone));
        System.out.println(numberOfItems);
        System.out.println(output);
    }

    public static String getMax(){
        String goodChr = "";
        int max = -1;
        for(int i = 0; i < population.size();i++){
            if(max < getFitness(population.get(i))){
                max = getFitness(population.get(i));
                goodChr = population.get(i);
            }
        }
        return goodChr;
    }
    ///testing
//    public static void main(String[] args) throws IOException {
////        DoCrossover("10110", "01001");
////        DoMutation(population);
////        System.out.println("size: " + population.size());
//        String x = "0111010101";
//        System.out.print(x.replace(x.charAt(0),'1'));
//
//    }
}
