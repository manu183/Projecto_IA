package utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

}
