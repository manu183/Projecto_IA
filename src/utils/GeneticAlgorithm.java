package utils;

import java.util.Arrays;
import java.util.Random;

import breakout.Breakout;
import breakout.BreakoutBoard;

public class GeneticAlgorithm {

    private final int POPULATION_SIZE = 100;
    private final int NUM_GENERATIONS = 6000;
    private double MUTATION_RATE = 0.3;
    private final double SELECTION_PERCENTAGE = 0.4;
    private final int K_TOURNAMENT = 5;
    private final int FITNESS_GOAL = 100000000; // O número de fitness que se pretende alcançar

    public static final int INPUT_DIM = 7; // Número de entradas da rede neural (estado do jogo)
    public static final int HIDDEN_DIM = 5; // Número de neurônios na camada oculta
    public static final int OUTPUT_DIM = 2; // Número de saídas da rede neural (ações do jogador)

    private Individuo[] population = new Individuo[POPULATION_SIZE]; // População de indivíduos

    public static final int SEED = 3;

    public GeneticAlgorithm() {
        generatePopulation();
    }

    private void generatePopulation() { // Criar a população inicial que é inicializada com pesos e bias aleatórios
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new Individuo(new FeedforwardNeuralNetwork(INPUT_DIM, HIDDEN_DIM, OUTPUT_DIM), 0);
        }
    }

    // Criar a nova população com base na população gerada e na população mantida
    private Individuo[] createNewPopulation(Individuo[] generatedPopulation, Individuo[] mantainedPopulation) {
        int generatedSize = generatedPopulation.length;
        int mantainedSize = mantainedPopulation.length;
        if (generatedSize + mantainedSize != POPULATION_SIZE) {
            throw new IllegalArgumentException(
                    "The sum of the generated and mantained populations must be equal to the population size");
        }
        Individuo[] newPopulation = new Individuo[POPULATION_SIZE];
        for (int i = 0; i < mantainedSize; i++) {// Adicionar os indivíduos que foram mantidos à população
            newPopulation[i] = mantainedPopulation[i];
        }
        for (int i = 0; i < generatedSize; i++) {// Adicionar os indivíduos gerados à população
            newPopulation[mantainedSize + i] = generatedPopulation[i];
        }
        return newPopulation;
    }

    private FeedforwardNeuralNetwork swapMutation(FeedforwardNeuralNetwork child) { // Aplicar a swap mutation a um
                                                                                    // indivíduo
        double[] charArray = child.getNeuralNetwork();
        Random random = new Random();
        int index1 = random.nextInt(charArray.length);
        int index2 = random.nextInt(charArray.length);
        double temp = charArray[index1];
        charArray[index1] = charArray[index2];
        charArray[index2] = temp;
        FeedforwardNeuralNetwork newChild = new FeedforwardNeuralNetwork(INPUT_DIM, HIDDEN_DIM, OUTPUT_DIM, charArray);
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
        Individuo bigger = randomParent();
        for (int i = 0; i < K_TOURNAMENT - 1; i++) { // É k_tournament-1 porque 1 dos k_tournaments foi atribuído ao
                                                     // bigger
            Individuo actual = randomParent();
            if (actual.getFitness() > bigger.getFitness()) {
                bigger = actual;
            }
        }
        return bigger.getFNN();
    }

    private Individuo randomParent() {
        Random random = new Random();
        int index = random.nextInt(POPULATION_SIZE);
        return population[index];
    }

    private Individuo[] selection() {
        Arrays.sort(population); // Ordenar a população por fitness
        int selectedPopulationSize = (int) (POPULATION_SIZE * SELECTION_PERCENTAGE);
        Individuo[] selectedPopulation = new Individuo[selectedPopulationSize];

        for (int i = 0; i < selectedPopulationSize; i++) {
            selectedPopulation[i] = population[population.length - 1 - i];
        }
        return selectedPopulation;
    }

    private void mutatePopulation() { // Aplicar a mutação à população de acordo com a taxa de mutação
        int numberOfMutations = (int) (POPULATION_SIZE * MUTATION_RATE);
        int[] indexes = new int[numberOfMutations];
        indexes = Utils.generateNNumbers(POPULATION_SIZE, numberOfMutations);
        for (int actual : indexes) {
            Individuo toMutate = population[actual];
            FeedforwardNeuralNetwork mutatedFNN = swapMutation(toMutate.getFNN()); // Aplicar a mutação ao indivíduo
            Individuo mutatedIndividuo = new Individuo(mutatedFNN, 0); // O fitness é 0 porque ainda não foi calculado
            population[actual] = mutatedIndividuo; // Substituir o indivíduo original pelo indivíduo mutado
        }
    }



    private Individuo runGeneration() { // Executar uma geração e guardar os fitnesses de cada indivíduo retornando o
                                       // melhor indivíduo
        Individuo genBest=new Individuo(null, 0);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            BreakoutBoard game = new BreakoutBoard(population[i].getFNN(), false, SEED);
            game.runSimulation();
            int fitness = game.getFitness();
            population[i].setFitness(fitness);
            if (fitness > genBest.getFitness()) {
                genBest=new Individuo(population[i].getFNN(), fitness);
            }
        }
        return genBest;
    }

    public void run() { // Correr o algoritmo genético
        int actualGeneration = 0; // indice de geração atual
        Individuo bestOverall = new Individuo(null, 0);
        while (actualGeneration < NUM_GENERATIONS && bestOverall.getFitness() < FITNESS_GOAL) {// Para cada geração
            Individuo genBest = runGeneration(); // Executar uma geração, calculando os fitnesses de cada indivíduo
            int newPopulationSize = (int) (POPULATION_SIZE * (1 - SELECTION_PERCENTAGE));
            Individuo[] newPopulation = new Individuo[newPopulationSize];
            Individuo[] mantainedPopulation = selection();

            for (int j = 0; j < newPopulationSize; j++) {// Para cada indíviduo que será gerado
                FeedforwardNeuralNetwork parent1 = selectParent(); // Selecionar os pai 1
                FeedforwardNeuralNetwork parent2 = selectParent(); // Selecionar os pai 2
                FeedforwardNeuralNetwork child = crossover(parent1, parent2); // Cruzar os pais para gerar um filho
                newPopulation[j] = new Individuo(child, 0);// Adicionar o indivíduo gerado à nova população que tem por
                                                           // defeito o fitness 0 porque ainda não foi calculado
            }
            population = createNewPopulation(newPopulation, mantainedPopulation); // Criar a nova população com base na
                                                                                  // nova população e na população
                                                                                  // mantida
            mutatePopulation(); // Aplicar a mutação à população
            if (genBest.getFitness() > bestOverall.getFitness()) {
                bestOverall = genBest;
            }
            System.out.println(
                "Generation:" + actualGeneration + " | Generation best fitness:" + genBest.getFitness()
                        + " | Overall best fitness:" + bestOverall.getFitness() + " | Fitness goal:" + FITNESS_GOAL
                        + " | Population size:" + POPULATION_SIZE + " | Mutation rate:" + MUTATION_RATE);
            actualGeneration++;

        }
        // Escrever o melhor indivíduo num ficheiro
        Utils.printToFile("scores/" + population[POPULATION_SIZE - 1].getFitness() + ".txt", bestOverall.getFNN());
        System.out.println(actualGeneration + " generations runned");
        Breakout game = new Breakout(bestOverall.getFNN(), SEED);
        System.out.println("Best Individuo: " + bestOverall);
        System.out.println("Best Fitness: " + bestOverall.getFitness() + " | Population size:" + POPULATION_SIZE
                + " | Selection rate:" + SELECTION_PERCENTAGE + " | Mutation rate:" + MUTATION_RATE + " | Seed:"
                + SEED
                + " | Generations number:" + NUM_GENERATIONS + " | k tournament:" + K_TOURNAMENT + " | Hidden dim:"
                + HIDDEN_DIM + " | Input dim:"
                + INPUT_DIM + " | Output dim:" + OUTPUT_DIM);

    }

    public static void main(String[] args) {
        System.out.println("Testing Genetic Algorithm");
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.run();
    }
}
