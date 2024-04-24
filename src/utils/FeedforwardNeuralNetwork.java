package utils;

public class FeedforwardNeuralNetwork implements GameController {

    private int inputDim; // Number of input nodes
    private int hiddenDim; // Number of hidden nodes  
    private int outputDim; // Number of output nodes
    double[][] inputHiddenWeights; // Weights between input and hidden layer
    private double[] hiddenBiases; // Biases of hidden layer
    private double[][] hiddenOutputWeights; // Weights between hidden and output layer
    private double[] outputBiases; // Biases of output layer


    // Initialize the weights and biases of the network
    public FeedforwardNeuralNetwork(int inputDim, int hiddenDim, int outputDim) {
        this.inputDim = inputDim;
        this.hiddenDim = hiddenDim;
        this.outputDim = outputDim;
        initializeParameters(); 
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
            outputBiases[i] = Math.random() * 0.5;
        }
    }

    // Forward pass of the network
    public double[] forward(double[] inputValues) {
        double[] hiddenLayer = new double[hiddenDim];
        double[] outputLayer = new double[outputDim];

        // Calculate activations for hidden layer
        for (int i = 0; i < hiddenDim; i++) {
            hiddenLayer[i] = 0;
            for (int j = 0; j < inputDim; j++) {
                hiddenLayer[i] += inputValues[j] * inputHiddenWeights[j][i];
            }
            hiddenLayer[i] += hiddenBiases[i];
            hiddenLayer[i] = sigmoid(hiddenLayer[i]);
        }

        // Calculate activations for output layer
        for (int i = 0; i < outputDim; i++) {
            outputLayer[i] = 0;
            for (int j = 0; j < hiddenDim; j++) {
                outputLayer[i] += hiddenLayer[j] * hiddenOutputWeights[j][i];
            }
            outputLayer[i] += outputBiases[i];
            outputLayer[i] = sigmoid(outputLayer[i]);
        }

        return outputLayer;
    }

    // Sigmoid activation function
    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nextMove'");
    }
}
