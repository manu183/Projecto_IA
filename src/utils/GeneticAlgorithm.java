package utils;

import java.util.Arrays;
import java.util.Random;
import breakout.BreakoutBoard;
import java.util.LinkedHashMap;

public class GeneticAlgorithm {

    private final int POPULATION_SIZE = 1000;
    // private final int NUM_GENERATIONS = 1000;
    private final int NUM_GENERATIONS = 1000;
    private final double MUTATION_RATE = 0.05;
    private final double SELECTION_PERCENTAGE = 0.2;
    private final int k_tournament = 4;
    private final int FITNESS_GOAL = 999999999; // O número de fitness que se pretende alcançar

    private final int INPUT_DIM = 7; // Número de entradas da rede neural (estado do jogo)
    private final int HIDDEN_DIM = 10; // Número de neurônios na camada oculta
    private final int OUTPUT_DIM = 2; // Número de saídas da rede neural (ações do jogador)

    private Population population = new Population(POPULATION_SIZE); // População de indivíduos
    

    public GeneticAlgorithm() {
        generatePopulation();
    }

    private void generatePopulation() { // Criar a população inicial que é inicializada com pesos e bias aleatórios
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new FeedforwardNeuralNetwork(INPUT_DIM, HIDDEN_DIM, OUTPUT_DIM), 0);
        }
    }

    private Population createNewPopulation(Population generatedPopulation, Population mantainedPopulation) {
        Population newPopulation = new Population(POPULATION_SIZE);
        FeedforwardNeuralNetwork[] generated = generatedPopulation.getFNN();
        FeedforwardNeuralNetwork[] mantained = mantainedPopulation.getFNN();
        for (FeedforwardNeuralNetwork actual: mantained) {// Adicionar os indivíduos que foram mantidos à população
            newPopulation.add(actual, -1); // O fitness é -1 porque ainda não foi calculado
        }
        for (FeedforwardNeuralNetwork actual: generated) {// Adicionar os indivíduos gerados à população
            newPopulation.add(actual, -1); // O fitness é -1 porque ainda não foi calculado
        }
        return newPopulation;
    }

    private FeedforwardNeuralNetwork mutate(FeedforwardNeuralNetwork child) {
        double[] charArray = child.getNeuralNetwork();
        FeedforwardNeuralNetwork newChild= new FeedforwardNeuralNetwork(INPUT_DIM, HIDDEN_DIM, OUTPUT_DIM, charArray);
        Random random = new Random();
        int index1 = random.nextInt(charArray.length);
        int index2 = random.nextInt(charArray.length);
        double temp = charArray[index1];
        charArray[index1] = charArray[index2];
        charArray[index2] = temp;
        return newChild;
    }

    private FeedforwardNeuralNetwork crossover(FeedforwardNeuralNetwork parent1, FeedforwardNeuralNetwork parent2) {
        double[] childArray = new double[parent1.getNeuralNetwork().length];
        Random random = new Random();
        int middle = random.nextInt(parent1.getNeuralNetwork().length);

        for (int i = 0; i < middle; i++) {
            childArray[i] = parent1.getNeuralNetwork()[i];

        }
        for (int i = middle; i < parent1.getNeuralNetwork().length; i++) {
            childArray[i] = parent2.getNeuralNetwork()[i];
        }
        FeedforwardNeuralNetwork child = new FeedforwardNeuralNetwork(INPUT_DIM, HIDDEN_DIM, OUTPUT_DIM, childArray);
        return child;
    }

    private FeedforwardNeuralNetwork selectParent() {

        FeedforwardNeuralNetwork bigger = randomParent();
        int biggerFitness = population.getFitnessOfFNN(bigger);

        for (int i = 0; i < k_tournament - 1; i++) { // É k_tournament-1 porque 1 dos k_tournaments foi atribuído ao
                                                     // bigger
            FeedforwardNeuralNetwork actual = randomParent();
            int actualFitness = population.getFitnessOfFNN(actual);
            if (actualFitness > biggerFitness)
                bigger = actual;
            biggerFitness = actualFitness;
        }
        return bigger;
    }


    private FeedforwardNeuralNetwork randomParent() {
        Random random = new Random();
        int index = random.nextInt(POPULATION_SIZE);
        return population.getFNNAtIndex(index);
    }

    private Population selection() {
        FeedforwardNeuralNetwork[] sortedNN = population.sortedFNNByFitness();
        Population sortedPopulation = new Population((int)(POPULATION_SIZE*SELECTION_PERCENTAGE));
        int selectedPopulation = (int) (POPULATION_SIZE * SELECTION_PERCENTAGE);
        for(int i=0; i<selectedPopulation; i++){
            sortedPopulation.add(sortedNN[i], population.getFitnessOfFNN(sortedNN[i]));
        }
        return sortedPopulation;
    }

    private Population mutatedPopulation() { // Aplicar a mutação à população de acordo com a taxa de mutação
        Population mutatedPopulation = this.population;
        int numberOfMutations = (int) (POPULATION_SIZE * MUTATION_RATE);
        int[] indexes = new int[numberOfMutations];
        indexes = Utils.generateNDifferentsNumbers(numberOfMutations, POPULATION_SIZE);
        for(int actual : indexes){
            mutatedPopulation.updateFNNAtIndex(mutate(mutatedPopulation.getFNNAtIndex(actual)), actual);
        }
        return mutatedPopulation;
    }

    private void runGeneration(int generationNum) { // Executar uma geração e guardar os fitnesses de cada indivíduo

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Random random = new Random();
            int randomSeed = random.nextInt(50);
            //BreakoutBoard game = new BreakoutBoard(population[i], false, randomSeed);
            //System.out.println("População no indice"+ i+ population.getFNNAtIndex(i));
            BreakoutBoard game = new BreakoutBoard(population.getFNNAtIndex(i), false, randomSeed);
            game.runSimulation();
            //fitnesses[i] = game.getFitness();
            population.updateFitnessAtIndex(game.getFitness(), i);
        }
        System.out.println(
            "Current best fitness: " + population.actualBestFitness() + " Generation: "+ generationNum);

    }

    public void run() { // Correr o algoritmo genético
        int actualGeneration = 0;

        while (actualGeneration < NUM_GENERATIONS && population.actualBestFitness() < FITNESS_GOAL) {// Para cada geração

            runGeneration(actualGeneration);
            int newPopulationSize = (int) (POPULATION_SIZE * (1 - SELECTION_PERCENTAGE));
            Population newPopulation = new Population(newPopulationSize);
            Population mantainedPopulation = selection();
            for (int j = 0; j < newPopulationSize; j++) {// Para cada indíviduo que será gerado
                FeedforwardNeuralNetwork parent1 = selectParent();
                FeedforwardNeuralNetwork parent2 = selectParent();
                FeedforwardNeuralNetwork child = crossover(parent1, parent2);
                newPopulation.add(child, -1); // Adicionar o indivíduo gerado à nova população que tem por defeito o fitness -1 porque ainda não foi calculado
            }
            this.population=createNewPopulation(newPopulation, mantainedPopulation); // Criar a nova população com base na nova população
                                                                   // e na população mantida

            this.population = mutatedPopulation(); // Aplicar a mutação à população
            
            actualGeneration++;
        }
        // Escrever o melhor indivíduo num ficheiro
        //FeedforwardNeuralNetwork best = findFNNOfFitness(currentBestFitness);
        FeedforwardNeuralNetwork best = population.sortedFNNByFitness()[0];
        Utils.printToFile("best.txt", best);
        System.out.println(actualGeneration + " generations runned");
    }

    public static void main(String[] args) {
        System.out.println("Testing Genetic Algorithm");
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.run();
        // BreakoutBoard randomGame = new BreakoutBoard(new RandomController(), false,
        // 2);
        // randomGame.runSimulation();
        // System.out.println("Random game fitness: " + randomGame.getFitness());
    }
}
