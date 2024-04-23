package breakout;

import utils.GameController;

public class NeuralNetwork{
    // A rede neuronal irá ter 1 input layers e 1 output layer, que corresponde ao movimento que a bola irá fazer, que será o resultado da função nextMove
    private int inputDim; //1
	private int hiddenDim; 
	private int outputDim; //1
    private double[] hiddenWeights; //lista de pesos entre input e hidden layer
	private double[] hiddenBiases; //vetor de biases da hidden layer
	private double[] outputWeights; //vetor de pesos entre hidden e output layer
	private double outputBias; //bias da output layer

    public NeuralNetwork(int inputDim, int hiddenDim, int outputDim) {
		
	}


    
    
}
