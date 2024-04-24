package utils;

import java.util.ArrayList;

public class GeneticAlgorithm {
    
    private final int POPULATION_SIZE = 100;
    private final int NUM_GENERATIONS = 1000;
    private final double MUTATION_RATE = 0.05;
    private final double SELECTION_PERCENTAGE = 0.2;
    private final int k_tournament = 2;

    private final int INPUT_DIM = 7; // Número de entradas da rede neural (estado do jogo)
    private final int HIDDEN_DIM = 10; // Número de neurônios na camada oculta
    private final int OUTPUT_DIM = 2; // Número de saídas da rede neural (ações do jogador)

    private FeedforwardNeuralNetwork[] population = new FeedforwardNeuralNetwork[POPULATION_SIZE];

    public GeneticAlgorithm() {
        generatePopulation();
    }

    private void generatePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new FeedforwardNeuralNetwork(INPUT_DIM, HIDDEN_DIM, OUTPUT_DIM);
        }
    }

    private void createNewPopulation(FeedforwardNeuralNetwork[] newgeneration) {
        ArrayList<FeedforwardNeuralNetwork> list = new ArrayList<FeedforwardNeuralNetwork>();
        for (int i = 0; i < POPULATION_SIZE * SELECTION_PERCENTAGE; i++) {
            list.add(population[i]);
        }
        for (int i = 0; i < POPULATION_SIZE * (1 - SELECTION_PERCENTAGE); i++) {
            list.add(newgeneration[i]);
        }
        population = list.toArray(new FeedforwardNeuralNetwork[0]);
    }

    private void mutate(FeedforwardNeuralNetwork child) {
        //
    }

    private FeedforwardNeuralNetwork[] crossover(FeedforwardNeuralNetwork parent1, FeedforwardNeuralNetwork parent2) {
        return population;
        //
    }

    private FeedforwardNeuralNetwork selectParent() {
        FeedforwardNeuralNetwork best = population[(int) (Math.random()*POPULATION_SIZE)];
        for (int i = 0; i < k_tournament; i++) {
            FeedforwardNeuralNetwork candidate = population[(int) (Math.random()*POPULATION_SIZE)];
            /*if (candidate.getFitness() > best.getFitness()) {
                best = candidate;
            }*/
        }
        return best;
    }
}

    