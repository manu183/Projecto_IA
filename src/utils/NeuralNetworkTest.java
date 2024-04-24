package utils;

import breakout.BreakoutBoard;

public class NeuralNetworkTest {

    public static void main(String[] args) {
        // Parâmetros da rede neural e do jogo
        int inputDim = 7; // Dimensão da camada de entrada da rede neural
        int hiddenDim = 10; // Dimensão da camada oculta da rede neural
        int outputDim = 2; // Dimensão da camada de saída da rede neural
        int seed = 42; // Semente para inicialização de números aleatórios

        // Criação da rede neural
        FeedforwardNeuralNetwork nn = new FeedforwardNeuralNetwork(inputDim, hiddenDim, outputDim);

        // Avaliação do desempenho da rede neural
        int numSimulations = 10; // Número de simulações a serem executadas
        for (int i = 0; i < numSimulations; i++) {
            // Criação do objeto BreakoutBoard para execução do jogo
            BreakoutBoard b = new BreakoutBoard(nn, false, seed);
            b.setSeed(i);

            // Execução da simulação do jogo e obtenção da aptidão da rede neural
            b.runSimulation();
            double fitness = b.getFitness(); // Change the type of fitness to double

            // Exibição da aptidão da rede neural
            System.out.println("Fitness of neural network in simulation " + i + ": " + (int)fitness); // Cast fitness to int
        }

        for(int i=0;i<50;i++)
            System.out.print("-");
        System.out.println();

        //Criação da rede neuronal 2 com valores das bias e pesos definidos
        double[] hiddenBiases = new double[]{1,2,3,4,5,6,7,5,3,4}; //dimensão 10
        double[] outputBiases = new double[]{5,2}; //dimensão 2
        double[][] inputHiddenWeights = new double[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
            {5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
            {6, 6, 6, 6, 6, 6, 6, 6, 6, 6},
            {7, 7, 7, 7, 7, 7, 7, 7, 7, 7}
        }; //dimensão 7x10
        double[][] hiddenOutputWeights = new double[][]{
            {1, 3},
            {5, 4},
            {5, 3},
            {2, 1},
            {3, 2},
            {4, 3},
            {5, 4},
            {6, 5},
            {7, 6},
            {8, 7}
        }; //dimensão 10x2 
        FeedforwardNeuralNetwork nn2 = new FeedforwardNeuralNetwork(hiddenBiases,outputBiases, inputHiddenWeights, hiddenOutputWeights);
        // Avaliação do desempenho da rede neural 2
        for (int i = 0; i < numSimulations; i++) {
            // Criação do objeto BreakoutBoard para execução do jogo
            BreakoutBoard b = new BreakoutBoard(nn2, false, seed);
            b.setSeed(i);

            // Execução da simulação do jogo e obtenção da aptidão da rede neural
            b.runSimulation();
            double fitness = b.getFitness(); // Change the type of fitness to double

            // Exibição da aptidão da rede neural
            System.out.println("Fitness of neural network 2 in simulation " + i + ": " + (int)fitness); // Cast fitness to int
        }

    }
}
