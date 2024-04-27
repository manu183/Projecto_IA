package utils;

import java.util.Arrays;
import java.util.LinkedHashMap;

class Population {
    private final int maxPopulationSize;
    private LinkedHashMap<FeedforwardNeuralNetwork, Integer> population;
    // Foi utilizado LinkedHashMap para manter a ordem de inserção dos elementos

    public Population(int maxPopulationSize) {
        this.maxPopulationSize = maxPopulationSize;
        population = new LinkedHashMap<FeedforwardNeuralNetwork, Integer>();
    }

    public void add(FeedforwardNeuralNetwork nn, int fitness) {
        if (population.size() + 1 <= maxPopulationSize) {
            population.put(nn, fitness);
        }

    }

    public FeedforwardNeuralNetwork getFNNAtIndex(int index) { // Retorna o FNN num certo índice
        return getFNN()[index];
    }

    public void updateFNNAtIndex(FeedforwardNeuralNetwork nn, int index) { // Substitui um FNN por outro num certo
                                                                           // índice
        if (index < 0 || index >= population.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds!");
        }
        LinkedHashMap<FeedforwardNeuralNetwork, Integer> newPopulation = new LinkedHashMap<FeedforwardNeuralNetwork, Integer>();
        FeedforwardNeuralNetwork[] feedforwardNeuralNetworks = getFNN();
        int[] fitnesses = getFitnesses();
        feedforwardNeuralNetworks[index] = nn;
        for (int i = 0; i < feedforwardNeuralNetworks.length; i++) {
            newPopulation.put(feedforwardNeuralNetworks[i], fitnesses[i]);
        }
        population = newPopulation;
    }

    public void updateFitnessAtIndex(int fitness, int index) { // Substitui um FNN por outro num certo
        // índice
        if (index < 0 || index >= population.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds!");
        }
        Population newPopulation = new Population(maxPopulationSize);
        FeedforwardNeuralNetwork[] feedforwardNeuralNetworks = getFNN();
        int[] fitnesses = getFitnesses();
        fitnesses[index] = fitness;
        for (int i = 0; i < feedforwardNeuralNetworks.length; i++) {
            newPopulation.add(feedforwardNeuralNetworks[i], fitnesses[i]);
        }
        population = newPopulation.population;
    }

    public int getFitnessOfFNN(FeedforwardNeuralNetwork nn) {
        if (population.containsKey(nn)) {
            return population.get(nn);
        }
        return -1; // or any other value indicating that nn is not in the population
    }

    public int[] getFitnesses() {
        int[] fitnesses = new int[population.size()];
        int i = 0;
        for (int fitness : population.values()) {
            fitnesses[i] = fitness;
            i++;
        }
        return fitnesses;
    }

    public int[] sortedFitnesses() {
        int[] fitnesses = new int[population.size()];
        int i = 0;
        for (int fitness : population.values()) {
            fitnesses[i] = fitness;
            i++;
        }
        Arrays.sort(fitnesses);
        return fitnesses;

    }

    public FeedforwardNeuralNetwork[] sortedFNNByFitness() {
        FeedforwardNeuralNetwork[] feedforwardNeuralNetworks = new FeedforwardNeuralNetwork[population.size()];
        int[] sortedFitnesses = sortedFitnesses();
        int currentIndex = 0; // Índice atual para adicionar ao array

        for (int i = 0; i < sortedFitnesses.length; i++) {
            for (FeedforwardNeuralNetwork nn : population.keySet()) {
                if (population.get(nn) == sortedFitnesses[i]) {
                    if (!contains(feedforwardNeuralNetworks, nn)) {
                        feedforwardNeuralNetworks[currentIndex] = nn;
                        currentIndex++; // Avança para o próximo índice disponível
                    }
                }
            }
        }
        return feedforwardNeuralNetworks;
    }

    private boolean contains(FeedforwardNeuralNetwork[] pop, FeedforwardNeuralNetwork nn) {
        for (FeedforwardNeuralNetwork actual : pop) {
            if (actual != null && actual.equals(nn)) { // Verifica-se se o elemento não é nulo antes de comparar
                return true;
            }
        }
        return false;
    }

    public int actualBestFitness() {
        return sortedFitnesses()[sortedFitnesses().length - 1];
    }

    public FeedforwardNeuralNetwork[] getFNN() {
        FeedforwardNeuralNetwork[] feedforwardNeuralNetworks = new FeedforwardNeuralNetwork[population.size()];
        int i = 0;
        for (FeedforwardNeuralNetwork nn : population.keySet()) {
            feedforwardNeuralNetworks[i] = nn;
            i++;
        }
        return feedforwardNeuralNetworks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i =0;
        for (FeedforwardNeuralNetwork nn : population.keySet()) {
            sb.append("index:"+i+" | "+nn.toString() + " Fitness: " + population.get(nn) + "\n");
            i++;
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        System.out.println("Testing Population class");
        Population population = new Population(10);
        FeedforwardNeuralNetwork nn0 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn1 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn2 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn3 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn4 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn5 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn6 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn7 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn8 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn9 = new FeedforwardNeuralNetwork(2, 2, 1);
        FeedforwardNeuralNetwork nn10 = new FeedforwardNeuralNetwork(2, 2, 1);
        System.out.println("nn1:"+nn0.toString());
        System.out.println("Adicionar nn0 a population");
        population.add(nn0, 4);
        System.out.println("Verificar population");
        System.out.println(population);
        population.add(nn1, 8);
        population.add(nn2, 4);
        population.add(nn3, 8);
        population.add(nn4, 23);
        population.add(nn5, 54);
        System.out.println("nn1:"+nn1.toString());
        System.out.println("nn2:"+nn2.toString());
        System.out.println("nn3:"+nn3.toString());
        System.out.println("nn4:"+nn4.toString());
        System.out.println("nn5:"+nn5.toString());
        System.out.println("nn9:"+nn9.toString());
        System.out.println("Verificar population");
        System.out.println(population);
        System.out.println("Fitness de nn0: "+population.getFitnessOfFNN(nn1));
        System.out.println("Fitness de nn1: "+population.getFitnessOfFNN(nn6));
        System.out.println("Get FNN at index 0: "+population.getFNNAtIndex(0));
        System.out.println("Get FNN at index 5: "+population.getFNNAtIndex(5));
        System.out.println("Update FNN at index 5 with nn9");
        System.out.println("Population before update:");
        System.out.println(population.toString());
        population.updateFNNAtIndex(nn9, 5);
        System.out.println("Population after update:");
        System.out.println(population.toString());
        System.out.println("Update fitness at index 5 with 100");
        population.updateFitnessAtIndex(100, 5);
        System.out.println("Population after update:");
        System.out.println(population.toString());
        System.out.println("Fitnesses:");
        System.out.println(Arrays.toString(population.getFitnesses()));
        System.out.println("Sorted fitnesses:");
        System.out.println(Arrays.toString(population.sortedFitnesses()));
        System.out.println("Sorted FNN by fitness:");
        System.out.println(Arrays.toString(population.sortedFNNByFitness()));
        System.out.println("Actual best fitness:");
        System.out.println(population.actualBestFitness());
    }
}
