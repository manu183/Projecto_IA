package utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
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

    public static int[] generateNDifferentsNumbers(int numbersToGenerate, int max){
        Random random = new Random();
        int[] numbers = new int[numbersToGenerate];
        for(int i = 0; i < numbersToGenerate; i++){
            int generatedNumber = random.nextInt(max);
            while(contains(numbers, generatedNumber)){
                generatedNumber = random.nextInt(max);
            }
            numbers[i] = generatedNumber;
            
        }
        return numbers;
    }

    private static boolean contains(int[] numbers, int number){
        for(int i = 0; i < numbers.length; i++){
            if(numbers[i] == number){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        //FeedforwardNeuralNetwork nn = new FeedforwardNeuralNetwork(7,10,2);
        //printToFile("neuralnetwork.txt", nn);
        //FeedforwardNeuralNetwork nn2 = fileToNeuralNetwork("best.txt");
        //System.out.println(nn2.toString());

        int[] numbers = generateNDifferentsNumbers(10, 20);
        for(int i = 0; i < numbers.length; i++){
            System.out.println(numbers[i]);
        }
    }

}
