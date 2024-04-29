package utils;

import java.util.Arrays;
import java.util.Random;

import breakout.Breakout;
import breakout.BreakoutBoard;
import pacman.Pacman;
import pacman.PacmanBoard;

public class GeneticAlgorithm {
    private String gameName;
    private int POPULATION_SIZE = 0; // Tamanho da população inicial ;
    private int NUM_GENERATIONS = 0;
    private double MUTATION_RATE = 0;
    private double SELECTION_PERCENTAGE = 0;
    private int K_TOURNAMENT = 0;
    private int FITNESS_GOAL = 0; // O número de fitness que se pretende alcançar

    public int INPUT_DIM = 0; // Número de entradas da rede neural (estado do jogo)
    public int HIDDEN_DIM = 0; // Número de neurônios na camada oculta
    public int OUTPUT_DIM = 0; // Número de saídas da rede neural (ações do jogador)

    private Individuo[] population; // População de indivíduos

    public static final int SEED = 3;

    public GeneticAlgorithm(String gameName) {
        if (gameName.toLowerCase().equals("breakout")) {
            this.gameName = gameName.toLowerCase();
            POPULATION_SIZE = 377;
            NUM_GENERATIONS = 1000;
            MUTATION_RATE = 0.35;
            SELECTION_PERCENTAGE = 0.4;
            K_TOURNAMENT = 5;
            FITNESS_GOAL = 100000000;
            INPUT_DIM = Commons.BREAKOUT_STATE_SIZE;
            HIDDEN_DIM = 5;
            OUTPUT_DIM = Commons.BREAKOUT_NUM_ACTIONS;
            population = new Individuo[POPULATION_SIZE];

        } else if (gameName.toLowerCase().equals("pacman")) {
            this.gameName = gameName.toLowerCase();
            POPULATION_SIZE = 40;
            NUM_GENERATIONS = 50;
            MUTATION_RATE = 0.4;
            SELECTION_PERCENTAGE = 0.4;
            K_TOURNAMENT = 5;
            FITNESS_GOAL = 100000000;
            INPUT_DIM = Commons.PACMAN_STATE_SIZE;
            HIDDEN_DIM = 5;
            OUTPUT_DIM = Commons.PACMAN_NUM_ACTIONS;
            population = new Individuo[POPULATION_SIZE];

        } else {
            throw new IllegalArgumentException("Invalid game name!!! Please choose between breakout and pacman");
        }
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
        int selectedPopulationSize = POPULATION_SIZE-(int)(POPULATION_SIZE * (1 - SELECTION_PERCENTAGE));
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
        Individuo genBest = new Individuo(null, 0);
        if (gameName.equals("breakout")) {
            for (int i = 0; i < POPULATION_SIZE; i++) {
                BreakoutBoard game = new BreakoutBoard(population[i].getFNN(), false, SEED);
                game.runSimulation();
                double fitness = (double) game.getFitness();
                population[i].setFitness(fitness);
                if (fitness > genBest.getFitness()) {
                    genBest = new Individuo(population[i].getFNN(), fitness);
                }
            }

        } else if (gameName.equals("pacman")) {
            for (int i = 0; i < POPULATION_SIZE; i++) {
                PacmanBoard game = new PacmanBoard(population[i].getFNN(), false, SEED);
                game.runSimulation();
                double fitness = game.getFitness();
                population[i].setFitness(fitness);
                if (fitness > genBest.getFitness()) {
                    genBest = new Individuo(population[i].getFNN(), fitness);
                }
            }
        }else {
            throw new IllegalArgumentException("Invalid game!!!");
        }
        return genBest;
    }

    public void run() {
        int actualGeneration = 0;
        int stagnantGenerations = 0;
        double lastFitness = 0;
        Individuo bestOverall = new Individuo(null, 0);

        while (actualGeneration < NUM_GENERATIONS && bestOverall.getFitness() < FITNESS_GOAL) {
            Individuo genBest = runGeneration();
            int newPopulationSize = (int) (POPULATION_SIZE * (1 - SELECTION_PERCENTAGE));
            Individuo[] newPopulation = new Individuo[newPopulationSize];
            Individuo[] mantainedPopulation = selection();

            for (int j = 0; j < newPopulationSize; j++) {
                FeedforwardNeuralNetwork parent1 = selectParent();
                FeedforwardNeuralNetwork parent2 = selectParent();
                FeedforwardNeuralNetwork child = crossover(parent1, parent2);
                newPopulation[j] = new Individuo(child, 0);
            }

            population = createNewPopulation(newPopulation, mantainedPopulation);
            mutatePopulation();

            if (genBest.getFitness() > bestOverall.getFitness()) {
                bestOverall = genBest;
                stagnantGenerations = 0;
            } else if (genBest.getFitness() == lastFitness) {
                stagnantGenerations++;
            } else {
                stagnantGenerations = 0;
            }

            if (stagnantGenerations >= 50) {
                MUTATION_RATE += 0.01;
                if (MUTATION_RATE > 1) {
                    MUTATION_RATE = 1;
                }
                stagnantGenerations = 0;
            }

            lastFitness = genBest.getFitness();

            System.out.println("Generation:" + actualGeneration + " | Generation best fitness:" + genBest.getFitness()
                    + " | Overall best fitness:" + bestOverall.getFitness() + " | Fitness goal:" + FITNESS_GOAL
                    + " | Population size:" + POPULATION_SIZE + " | Mutation rate:" + MUTATION_RATE);
            actualGeneration++;
        }

        if(gameName.equals("breakout")){
            Utils.printToFile("scores/breakout/" + (int)bestOverall.getFitness() + ".txt", bestOverall.getFNN());
            System.out.println(actualGeneration + " generations runned");
            Breakout game = new Breakout(bestOverall.getFNN(), SEED);
        }else if(gameName.equals("pacman")){
            Utils.printToFile("scores/pacman/" + (int)bestOverall.getFitness() + ".txt", bestOverall.getFNN());
            System.out.println(actualGeneration + " generations runned");
            Pacman game = new Pacman(bestOverall.getFNN(),true, SEED);
        }
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
        GeneticAlgorithm ga = new GeneticAlgorithm("breakout");
        ga.run();
    }
}
