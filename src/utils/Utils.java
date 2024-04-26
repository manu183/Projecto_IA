package utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static double[] readFromFile(String filename) {

        List<Double> nnData = new ArrayList<Double>();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] tokens = linha.split(" ");

                for (String token : tokens) {
                    nnData.add(Double.parseDouble(token));
                }

                // TODO - Implementar a leitura dos dados do arquivo

            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao gravar o arquivo.");
            e.printStackTrace();
        }
        double[] values = new double[nnData.size()];
        for (int i = 0; i < nnData.size(); i++) {
            values[i] = nnData.get(i);
        }

        return values;
    }

    public static int[] generateNDifferentsNumbers(int numbersToGenerate, int max) {
        Random random = new Random();
        int[] numbers = new int[numbersToGenerate];
        for (int i = 0; i < numbersToGenerate; i++) {
            int generatedNumber = random.nextInt(max);
            while (contains(numbers, generatedNumber)) {
                generatedNumber = random.nextInt(max);
            }
            numbers[i] = generatedNumber;

        }
        return numbers;
    }

    private static boolean contains(int[] numbers, int number) {
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == number) {
                return true;
            }
        }
        return false;
    }
}
