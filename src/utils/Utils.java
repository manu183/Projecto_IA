package utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileWriter;

public class Utils {
    public static void printToFile(String filename, FeedforwardNeuralNetwork nn) {
        try {
            File file = new File(filename);
            PrintWriter writer = new PrintWriter(new FileWriter(file));

            // Escreve a representação da rede neural diretamente no arquivo
            writer.println(nn.toString());

            writer.close();
            System.out.println("Dados gravados com sucesso em " + filename);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao gravar o arquivo.");
            e.printStackTrace();
        }
    }
    /**
     * @param filename
     * @return
     */
    public static FeedforwardNeuralNetwork fileToNeuralNetwork(String filename) {
        FeedforwardNeuralNetwork nn = null;
        double[] hiddenBiases = null;
        double[] outputBiases = null;
        double[][] inputHiddenWeights = null;
        double[][] hiddenOutputWeights = null;

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                //scanner.nextLine();
                System.out.println(linha);
                

                //TODO - Implementar a leitura dos dados do arquivo
                

            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao gravar o arquivo.");
            e.printStackTrace();
        }
       // nn=new FeedforwardNeuralNetwork(hiddenBiases, outputBiases, inputHiddenWeights, hiddenOutputWeights);
        if(nn!=null)
            System.out.println("Dados lidos com sucesso de " + filename);
        return nn;
    }

    public static void main(String[] args) {
        //FeedforwardNeuralNetwork nn = new FeedforwardNeuralNetwork(7,10,2);
        //printToFile("neuralnetwork.txt", nn);
        //FeedforwardNeuralNetwork nn2 = fileToNeuralNetwork("best.txt");
        //System.out.println(nn2.toString());
    }

}
