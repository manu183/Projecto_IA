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
        Population newPopulation = new Population(maxPopulationSize);
        FeedforwardNeuralNetwork[] feedforwardNeuralNetworks = getFNN();
        int[] fitnesses = getFitnesses();
        feedforwardNeuralNetworks[index] = nn;
        for (int i = 0; i < feedforwardNeuralNetworks.length; i++) {
            newPopulation.add(feedforwardNeuralNetworks[i], fitnesses[i]);
        }
        population = newPopulation.population;
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
        int[] fitnesses = new int[maxPopulationSize];
        int i = 0;
        for (int fitness : population.values()) {
            fitnesses[i] = fitness;
            i++;
        }
        return fitnesses;
    }

    public int[] sortedFitnesses() {
        int[] fitnesses = new int[maxPopulationSize];
        int i = 0;
        for (int fitness : population.values()) {
            fitnesses[i] = fitness;
            i++;
        }
        Arrays.sort(fitnesses);
        return fitnesses;

    }

    public FeedforwardNeuralNetwork[] sortedFNNByFitness() {
        FeedforwardNeuralNetwork[] feedforwardNeuralNetworks = new FeedforwardNeuralNetwork[maxPopulationSize];
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
        FeedforwardNeuralNetwork[] feedforwardNeuralNetworks = new FeedforwardNeuralNetwork[maxPopulationSize];
        int i = 0;
        for (FeedforwardNeuralNetwork nn : population.keySet()) {
            feedforwardNeuralNetworks[i] = nn;
            i++;
        }
        return feedforwardNeuralNetworks;
    }
}
