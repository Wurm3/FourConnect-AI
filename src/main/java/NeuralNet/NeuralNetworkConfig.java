package NeuralNet;

import org.nd4j.shade.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class NeuralNetworkConfig {

    public static void saveNeuralNetwork(String filePath, NeuralNetwork neuralNetwork) throws IOException {
        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file);
        String json = new ObjectMapper().writeValueAsString(neuralNetwork);
        fileWriter.write(json);
        fileWriter.close();
    }

    public static NeuralNetwork loadNeuralNetwork(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        br.close();

        return new ObjectMapper().readValue(sb.toString(),NeuralNetwork.class);
    }
}