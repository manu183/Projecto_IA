package utils;

import breakout.BreakoutBoard;
import pacman.PacmanBoard;

public class FeedforwardNeuralNetwork implements GameController {

    private int inputDim; // Number of input nodes
    private int hiddenDim; // Number of hidden nodes
    private int outputDim; // Number of output nodes
    private double[] hiddenBiases; // Biases of hidden layer
    private double[] outputBiases; // Biases of output layer
    private double[][] inputHiddenWeights; // Weights between input and hidden layer
    private double[][] hiddenOutputWeights; // Weights between hidden and output layer

    // Initialize the weights and biases of the network
    public FeedforwardNeuralNetwork(int inputDim, int hiddenDim, int outputDim) {
        this.inputDim = inputDim;
        this.hiddenDim = hiddenDim;
        this.outputDim = outputDim;

        initializeParameters();
    }

    public FeedforwardNeuralNetwork(double[] hiddenBiases, double[] outputBiases, double[][] inputHiddenWeights,
            double[][] hiddenOutputWeights) {
        this.inputDim = inputHiddenWeights.length;
        this.hiddenDim = hiddenOutputWeights.length;
        this.outputDim = hiddenOutputWeights[0].length;
        this.hiddenBiases = hiddenBiases;
        this.outputBiases = outputBiases;
        this.inputHiddenWeights = inputHiddenWeights;
        this.hiddenOutputWeights = hiddenOutputWeights;
    }

    public FeedforwardNeuralNetwork(int inputDim, int hiddenDim, int outputDim, double[] values) {
        this(inputDim, hiddenDim, outputDim);
        if (values.length != (inputDim * hiddenDim + hiddenDim + hiddenDim * outputDim + outputDim)) {
            throw new IllegalArgumentException("The number of values is not correct");
        }
        int aux = 0;
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                inputHiddenWeights[i][j] = values[aux];
                aux++;
            }
        }
        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = values[aux];
            aux++;
        }
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                hiddenOutputWeights[i][j] = values[aux];
                aux++;
            }
        }
        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = values[aux];
            aux++;
        }
    }

    // Create a FeedforwardNeuralNetwork object from a file
    public static FeedforwardNeuralNetwork FNNfromFile(int inputDim, int hiddenDim, int outputDim, String filename) {
        double[] values = Utils.readFromFile(filename);
        return new FeedforwardNeuralNetwork(inputDim, hiddenDim, outputDim, values);
    }

    // Initialize the weights and biases of the network with random values
    public void initializeParameters() {
        inputHiddenWeights = new double[inputDim][hiddenDim];
        hiddenBiases = new double[hiddenDim];
        hiddenOutputWeights = new double[hiddenDim][outputDim];
        outputBiases = new double[outputDim];

        // Initialize input-hidden weights with random values
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                inputHiddenWeights[i][j] = Math.random();

            }
        }

        // Initialize hidden biases with random values
        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = Math.random();
        }

        // Initialize hidden-output weights with random values
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                hiddenOutputWeights[i][j] = Math.random();
            }
        }

        // Initialize output biases with random values
        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = Math.random();
        }
    }

    // Forward pass of the network
    public double[] forward(int[] currentState) {
        double[] hiddenLayer = new double[hiddenDim];
        double[] outputLayer = new double[outputDim];

        // Calculate activations for hidden layer
        for (int i = 0; i < hiddenDim; i++) {
            hiddenLayer[i] = 0;
            for (int j = 0; j < inputDim; j++) {
                hiddenLayer[i] += currentState[j] * inputHiddenWeights[j][i];
            }
            hiddenLayer[i] += hiddenBiases[i];
            // hiddenLayer[i] = sigmoid(hiddenLayer[i]);
            hiddenLayer[i] = reLu(hiddenLayer[i]);
        }

        // Calculate activations for output layer
        for (int i = 0; i < outputDim; i++) {
            outputLayer[i] = 0;
            for (int j = 0; j < hiddenDim; j++) {
                outputLayer[i] += hiddenLayer[j] * hiddenOutputWeights[j][i];
            }
            outputLayer[i] += outputBiases[i];
            // outputLayer[i] = sigmoid(outputLayer[i]);
            outputLayer[i] = reLu(outputLayer[i]);
        }
        return outputLayer;
    }

    private double reLu(double x) {
        return Math.max(0, x);
    }

    // Get the weights and biases of the network
    public double[] getNeuralNetwork() {
        double[] values = new double[inputDim * hiddenDim + hiddenDim + hiddenDim * outputDim + outputDim];
        int aux = 0;

        // Copy input-hidden weights
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                values[aux++] = inputHiddenWeights[i][j];
            }
        }

        // Copy hidden biases
        for (int i = 0; i < hiddenDim; i++) {
            values[aux++] = hiddenBiases[i];
        }

        // Copy hidden-output weights
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                values[aux++] = hiddenOutputWeights[i][j];
            }
        }

        // Copy output biases
        for (int i = 0; i < outputDim; i++) {
            values[aux++] = outputBiases[i];
        }

        return values;
    }

    @Override
    public int nextMove(int[] currentState) {
        double[] outputValues = forward(currentState);
        if (currentState.length == Commons.BREAKOUT_STATE_SIZE) {
            if (outputValues[0] > outputValues[1]) {
                return BreakoutBoard.LEFT; // Mova para a esquerda
            } else if (outputValues[0] < outputValues[1]) {
                return BreakoutBoard.RIGHT; // Mova para a direita
            }
        }
        if (currentState.length == Commons.PACMAN_STATE_SIZE) {
            int max = 0;
            for (int i = 1; i < outputValues.length; i++) {
                if (outputValues[i] > outputValues[max]) {
                    max = i;
                }
            }
            if (max == 0) {
                return PacmanBoard.NONE; // Stop
            }
            if (max == 1) {
                return PacmanBoard.LEFT; // Move to the left
            }
            if (max == 2) {
                return PacmanBoard.RIGHT; // Mova to the right
            }
            if (max == 3) {
                return PacmanBoard.UP; // Mova to the top
            }
            if (max == 4) {
                return PacmanBoard.DOWN; // Mova to the bottom
            }
        }
        return 0; // Stop
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < getNeuralNetwork().length; i++) {
            res += getNeuralNetwork()[i] + " ";
        }
        return res;
    }

}
