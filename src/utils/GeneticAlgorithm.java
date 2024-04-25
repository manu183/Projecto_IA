package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import breakout.Breakout;

import breakout.BreakoutBoard;
import pacman.RandomController;

import java.util.Collections;


public class GeneticAlgorithm {
    
    private final int POPULATION_SIZE = 100;
    //private final int NUM_GENERATIONS = 1000;
    private final int NUM_GENERATIONS = 1;
    private final double MUTATION_RATE = 0.05;
    private final double SELECTION_PERCENTAGE = 0.2;
    private final int k_tournament = 2;
    private final int FITNESS_GOAL=20000; //O número de fitness que se pretende alcançar

    private final int INPUT_DIM = 7; // Número de entradas da rede neural (estado do jogo)
    private final int HIDDEN_DIM = 10; // Número de neurônios na camada oculta
    private final int OUTPUT_DIM = 2; // Número de saídas da rede neural (ações do jogador)

    private FeedforwardNeuralNetwork[] population = new FeedforwardNeuralNetwork[POPULATION_SIZE];
    private int[] fitnesses = new int[POPULATION_SIZE];
    private int currentBestFitness = 0;

    public GeneticAlgorithm() {
        generatePopulation();
    }

    private void generatePopulation() { //Criar a população inicial que é inicializada com pesos e bias aleatórios
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new FeedforwardNeuralNetwork(INPUT_DIM, HIDDEN_DIM, OUTPUT_DIM);
        }
    }

    private void createNewPopulation(FeedforwardNeuralNetwork[] newgeneration, FeedforwardNeuralNetwork[] mantainGeneration) { 
        FeedforwardNeuralNetwork[] newPopulation = new FeedforwardNeuralNetwork[POPULATION_SIZE];
        for(int i=0;i<mantainGeneration.length;i++) {//Adicionar os indivíduos que foram mantidos à população
            newPopulation[i] = mantainGeneration[i];
        }
        for(int i=0;i<newgeneration.length;i++) {//Adicionar os indivíduos gerados à população
            newPopulation[i+mantainGeneration.length] = newgeneration[i];
        }
        population = newPopulation;//Definir a nova população
    }

    private void mutate(FeedforwardNeuralNetwork child) {
        // A mutação consistirá em trocar dois pesos da matriz de pesos entre a camada de entrada e a camada oculta
        double[][] inputHiddenWeights = child.getInputHiddenWeights(); //Obter a matriz de pesos entre a camada de entrada e a camada oculta
        double weigh1 = inputHiddenWeights[1][1];
        double weigh2 = inputHiddenWeights[2][1];
        double aux = weigh1;
        inputHiddenWeights[1][1] = weigh2;
        inputHiddenWeights[2][1] = aux;
        child.setInputHiddenWeights(inputHiddenWeights);//Definir a matriz de pesos entre a camada de entrada e a camada oculta
    }

    private FeedforwardNeuralNetwork crossover(FeedforwardNeuralNetwork parent1, FeedforwardNeuralNetwork parent2) {
        FeedforwardNeuralNetwork child;
        // O child terá os hiddenBiases e outputBiases do parent1 
        //e os inputHiddenWeights e hiddenOutputWeights do parent2
        double[] hiddenBiases = parent1.getHiddenBiases();
        double[] outputBiases = parent1.getOutputBiases();
        double[][] inputHiddenWeights = parent2.getInputHiddenWeights();
        double[][] hiddenOutputWeights = parent2.getHiddenOutputWeights();
        child = new FeedforwardNeuralNetwork(hiddenBiases, outputBiases, inputHiddenWeights, hiddenOutputWeights);
        return child;
    }

    private FeedforwardNeuralNetwork selectParent() {
        
        FeedforwardNeuralNetwork bigger = randomParent();
        int biggerFitness = findFitnessOf(bigger);
        
        for (int i = 0; i < k_tournament - 1; i++) { // É k_tournament-1 porque 1 dos k_tournaments foi atribuído ao bigger
            FeedforwardNeuralNetwork actual = randomParent();
            int actualFitness = findFitnessOf(actual);
            if (actualFitness > biggerFitness)
                bigger = actual;
                biggerFitness = actualFitness;
        }
        return bigger;
    }

    private int findFitnessOf(FeedforwardNeuralNetwork parent) {
        for(int i=0;i<population.length;i++) {
            if(population[i] == parent) {
                return fitnesses[i];
            }
        }
        return -1;
    }
    private FeedforwardNeuralNetwork findFNNOfFitness(int fitness) {
        for(int i=0;i<population.length;i++) {
            if(fitnesses[i] == fitness) {
                return population[i];
            }
        }
        return null;
    }

    private FeedforwardNeuralNetwork randomParent() {
        Random random = new Random();
        int index = random.nextInt(population.length);
        return population[index];
    }

    
    private FeedforwardNeuralNetwork[] selection(){
        //Ordenar as fitnesses
        int[] sortedFitnesses = Arrays.copyOf(fitnesses, fitnesses.length);
        Arrays.sort(sortedFitnesses);
        //Selecionar os melhores indivíduos
        FeedforwardNeuralNetwork[] selected = new FeedforwardNeuralNetwork[(int)(POPULATION_SIZE * SELECTION_PERCENTAGE)];
        for(int i=0;i<selected.length;i++) {
            for(int j=0;j<fitnesses.length;j++) {
                if(fitnesses[j] == sortedFitnesses[i]) {
                    selected[i] = population[j];
                    break;
                }
            }
        }
        return selected;
    }
    private void runGeneration() { //Executar uma geração e guardar os fitnesses de cada indivíduo

        for (int i = 0; i < POPULATION_SIZE; i++) {
            BreakoutBoard game = new BreakoutBoard(population[i], false, 2);
            game.runSimulation();
            fitnesses[i] = game.getFitness();
            if(fitnesses[i]>currentBestFitness) {
                currentBestFitness = fitnesses[i];
            }
        }

    }

    public void run() { //Correr o algoritmo genético
        int actualGeneration = 0;
        if(population.length == 0) {
            generatePopulation();
        }
        while (actualGeneration<NUM_GENERATIONS && currentBestFitness < FITNESS_GOAL) {//Para cada geração
            runGeneration();
            
            FeedforwardNeuralNetwork[] newgeneration = new FeedforwardNeuralNetwork[POPULATION_SIZE-(int)(POPULATION_SIZE*SELECTION_PERCENTAGE)];
            FeedforwardNeuralNetwork[] mantainGeneration = selection();
            for (int j = 0; j < newgeneration.length; j++) {//Para cada indíviduo que será gerado
                FeedforwardNeuralNetwork parent1 = selectParent();
                FeedforwardNeuralNetwork parent2 = selectParent();
                FeedforwardNeuralNetwork child = crossover(parent1, parent2);
                newgeneration[j] = child;
            }
            createNewPopulation(newgeneration, mantainGeneration); //Criar a nova população com base na nova população e na população mantida

            //De modo a garantir que a mutação é feita de modo aleatório a população é baralhada
            List<FeedforwardNeuralNetwork> list = new ArrayList<FeedforwardNeuralNetwork>(Arrays.asList(population)); //Converte o array em uma lista para poder ser baralhado
            Collections.shuffle(list);
            population = list.toArray(population);
            //Mutação
            for(int m=0;m<(int)(POPULATION_SIZE*MUTATION_RATE);m++) {//Para cada indivíduo que será mutado
                FeedforwardNeuralNetwork actual = population[m];
                System.out.println(m);
                mutate(actual);
                population[m] = actual;
            }

            actualGeneration++;
        }
        //Escrever o melhor indivíduo num ficheiro
        FeedforwardNeuralNetwork best = findFNNOfFitness(currentBestFitness);
        Utils.printToFile("best.txt", best);
    }

    public static void main(String[] args) {
        System.out.println("Testing Genetic Algorithm");
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.run();
        //BreakoutBoard randomGame = new BreakoutBoard(new RandomController(), false, 2);
        //randomGame.runSimulation();
        //System.out.println("Random game fitness: " + randomGame.getFitness());
    }
}

    