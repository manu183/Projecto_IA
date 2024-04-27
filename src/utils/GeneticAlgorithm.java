package utils;

import java.util.Random;

import breakout.Breakout;
import breakout.BreakoutBoard;

public class GeneticAlgorithm {

    private final int POPULATION_SIZE = 500;
    // private final int NUM_GENERATIONS = 1000;
    private final int NUM_GENERATIONS = 100;
    private final double MUTATION_RATE = 0.1;
    private final double SELECTION_PERCENTAGE = 0.5;
    private final int k_tournament = 10;
    private final int FITNESS_GOAL = 999999999; // O número de fitness que se pretende alcançar

    private final int INPUT_DIM = 7; // Número de entradas da rede neural (estado do jogo)
    private final int HIDDEN_DIM = 7; // Número de neurônios na camada oculta
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


    // Criar a nova população com base na população gerada e na população mantida
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

        Random random = new Random();
        int randomSeed = random.nextInt(1000);
        int  seed=40;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            BreakoutBoard game = new BreakoutBoard(population.getFNNAtIndex(i), false, seed);
            game.runSimulation();
            population.updateFitnessAtIndex(game.getFitness(), i);
        }
        System.out.println(
            "Current best fitness: " + population.actualBestFitness() + " Generation: "+ generationNum + " Seed: " + seed);

    }

    public void run() { // Correr o algoritmo genético
        int actualGeneration = 0; //indice de geração atual

        while (actualGeneration < NUM_GENERATIONS && population.actualBestFitness() < FITNESS_GOAL) {// Para cada geração

            runGeneration(actualGeneration); // Executar uma geração, calculando os fitnesses de cada indivíduo
            int newPopulationSize = (int) (POPULATION_SIZE * (1 - SELECTION_PERCENTAGE));
            Population newPopulation = new Population(newPopulationSize);
            Population mantainedPopulation = selection();
            for (int j = 0; j < newPopulationSize; j++) {// Para cada indíviduo que será gerado
                //FeedforwardNeuralNetwork parent1 = selectParent(); // Selecionar os pai 1
                FeedforwardNeuralNetwork parent1 = population.sortedFNNByFitness()[0];
                //FeedforwardNeuralNetwork parent2 = selectParent(); // Selecionar os pai 2
                FeedforwardNeuralNetwork parent2 = population.sortedFNNByFitness()[1];
                FeedforwardNeuralNetwork child = crossover(parent1, parent2); // Cruzar os pais para gerar um filho
                newPopulation.add(child, -1); // Adicionar o indivíduo gerado à nova população que tem por defeito o fitness -1 porque ainda não foi calculado
            }
            this.population=createNewPopulation(newPopulation, mantainedPopulation); // Criar a nova população com base na nova população e na população mantida

            this.population = mutatedPopulation(); // Aplicar a mutação à população
            
            actualGeneration++;
        }
        // Escrever o melhor indivíduo num ficheiro
        FeedforwardNeuralNetwork best = population.sortedFNNByFitness()[0];
        Utils.printToFile("best.txt", best);
        System.out.println(actualGeneration + " generations runned");
        Breakout game = new Breakout(best, 40);
    }

    public static void main(String[] args) {
        System.out.println("Testing Genetic Algorithm");
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.run();
    }
}
