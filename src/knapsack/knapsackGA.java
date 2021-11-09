package knapsack;

import java.util.Random;
import java.util.Vector;

public class knapsackGA {
    int numberOfPairs;
    int capacity;
    final double Pc = 0.7;
    final double Pm = 0.01;
    Random rand;
    Vector<pair<Integer, Integer>> pairs;
    Vector<String> population;
    Vector<pair<String, Integer>> fitnessValues;
    Vector<String> selectedChromosomes;
    Vector<pair<String, Integer>> offSprings;

    public knapsackGA(int numberOfPairs, int capacity, Vector<pair<Integer, Integer>> pairs) {
        this.numberOfPairs = numberOfPairs;
        this.capacity = capacity;
        this.pairs = pairs;
        rand = new Random();
        population = new Vector<>();
        fitnessValues = new Vector<>();
        selectedChromosomes = new Vector<>();
        offSprings = new Vector<>();
    }

    public int binarySearch(Vector<Integer> cumulative, int l, int r, int x) {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            if (cumulative.get(mid + 1) > x && cumulative.get(mid) <= x)
                return mid;

            if (cumulative.get(mid) > x)
                return binarySearch(cumulative, l, mid - 1, x);

            return binarySearch(cumulative, mid + 1, r, x);
        }

        return -1;
    }

    public int factorialOf(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorialOf(n - 1);
    }

    public String generateChromosome() {
        StringBuilder chromosome = new StringBuilder();
        for (int i = 0; i < numberOfPairs; i++) {
            chromosome.append(Math.round(rand.nextDouble()));
        }
        return chromosome.toString();
    }

    public int getPopulation() {
        int N = 50;
        int result = (int) (factorialOf(numberOfPairs) * 0.000002);
        if (result > N) N = (result % 2 == 0 ? result : result + 1);

        return N;
    }

    public int getWeight(String chromosome) {
        int totalWeight = 0;
        for (int i = 0; i < chromosome.length(); i++) {
            if (chromosome.charAt(i) == '1') {
                totalWeight += pairs.get(i).key;
            }
        }
        return totalWeight;
    }

    public int getFitness(String chromosome) {
        int totalValue = 0;
        for (int i = 0; i < chromosome.length(); i++) {
            if (chromosome.charAt(i) == '1') {
                totalValue += pairs.get(i).value;
            }
        }
        return totalValue;
    }

    public void DoSelection() {
        Vector<Integer> cumulative = new Vector<>();
        int sum = 0;
        for (pair<String, Integer> fitnessValue : fitnessValues) {
            cumulative.add(sum);
            sum += fitnessValue.value;
        }
        cumulative.add(sum);
        for (int i = 0; i < fitnessValues.size(); i++) {
            int r = (int) Math.floor(Math.random() * sum);
            selectedChromosomes.add(fitnessValues.get(binarySearch(cumulative, 0, cumulative.size() - 1, r)).key);
        }
    }

    public void DoCrossover(String chromosome1, String chromosome2) {
        int chromosomeLength = chromosome1.length();
        String offspring1, offspring2;
        int turn = 0;
        while (true) {
            offspring1 = chromosome1;
            offspring2 = chromosome2;
            if (turn == 3) {
                break;
            }
            double r2 = rand.nextDouble();
            if (r2 <= Pc) {
                int r1 = (int) Math.floor(Math.random() * ((chromosomeLength - 1)) + 1);
                offspring1 = chromosome1.substring(0, r1);
                try {
                    offspring1 += chromosome2.substring(r1, chromosomeLength);
                } catch (Exception e) {
                    System.out.println(chromosomeLength + " " + r1 + " " + chromosome2);
                }
                offspring2 = chromosome2.substring(0, r1);
                offspring2 += chromosome1.substring(r1, chromosomeLength);
            }
            if (getWeight(offspring1) <= capacity && getWeight(offspring2) <= capacity) {
                break;
            }
            turn++;
        }
        offSprings.add(new pair<>(offspring1, getFitness(offspring1)));
        offSprings.add(new pair<>(offspring2, getFitness(offspring2)));
    }

    public void DoMutation() {
        for (pair<String, Integer> offSpring : offSprings) {
            String temp;
            for (int j = 0; j < offSpring.key.length(); j++) {
                double r = rand.nextDouble();
                if (r <= Pm) {
                    temp = offSpring.key.substring(0, j)
                            + (offSpring.key.charAt(j) == '0' ? '1' : '0')
                            + offSpring.key.substring(j + 1);
                    if (getWeight(temp) <= capacity) {
                        offSpring.key = temp;
                    }
                }
            }
        }
    }

    public void DoReplacement() {
        population.clear();
        fitnessValues.clear();
        for (pair<String, Integer> offSpring : offSprings) {
            population.add(offSpring.key);
            fitnessValues.add(new pair<>(offSpring.key, getFitness(offSpring.key)));
        }
        offSprings.clear();
        selectedChromosomes.clear();
    }

    public void performGA(int caseNumber) {
        int popSize = getPopulation();
        while (population.size() < popSize) {
            String chromosome = generateChromosome();
            while (getWeight(chromosome) > capacity) {
                chromosome = generateChromosome();
            }
            population.add(chromosome);
            int fitness = getFitness(chromosome);
            fitnessValues.add(new pair<>(chromosome, fitness));
        }
        int turn = 0;
        String bestChromosome = "";
        while (turn < 200) {
            DoSelection();
            for (int i = 0; i < selectedChromosomes.size(); i += 2) {
                DoCrossover(selectedChromosomes.get(i), selectedChromosomes.get(i + 1));
            }
            DoMutation();
            DoReplacement();
            bestChromosome = (getFitness(bestChromosome) > getFitness(getMax()) ? bestChromosome : getMax());
            turn++;
        }
        print(bestChromosome, caseNumber);
    }

    public void print(String bestChromosome, int caseNumber) {
        StringBuilder output = new StringBuilder();
        int numberOfItems = 0;
        for (int i = 0; i < bestChromosome.length(); i++) {
            if (bestChromosome.charAt(i) == '1') {
                numberOfItems++;
                output.append(pairs.get(i).key).append(" ").append(pairs.get(i).value).append('\n');
            }
        }
        //System.out.println("Weight = " + getWeight(bestChromosome));
        System.out.println("Case " + caseNumber + ": " + getFitness(bestChromosome));
        System.out.println(numberOfItems);
        System.out.println(output);
    }

    public String getMax() {
        String bestChromosome = "";
        int max = -1;
        for (String s : population) {
            if (max < getFitness(s)) {
                max = getFitness(s);
                bestChromosome = s;
            }
        }
        return bestChromosome;
    }

}
