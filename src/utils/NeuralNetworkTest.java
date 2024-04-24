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
    }
}
