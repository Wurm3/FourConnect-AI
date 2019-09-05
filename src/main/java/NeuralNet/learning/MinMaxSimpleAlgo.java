package NeuralNet.learning;

import NeuralNet.NeuralNetwork;
import NeuralNet.NeuralNetworkConfig;
import gamecore.VierGewinnt;
import gamecore.VierGewinntInterface;
import montecarlo.ArtificialIntelligenceInterface;
import montecarlo.MonteCarlo;
import montecarlo.NeuralNetworkGameConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class MinMaxSimpleAlgo implements LearningAlgoInterface {
    private static final int INPUT_LAYER_SIZE = 6 * 7 * 2 + 1;
    private static final int HIDDEN_LAYER_COUNT = 3;
    private static final int HIDDEN_LAYER_SIZE = 3;
    private static final int OUTPUT_LAYER_SIZE = 7;

    private NeuralNetwork network1;
    private NeuralNetwork network2;

    private int gameCount;

    public MinMaxSimpleAlgo(int gameCount) {
        network1 = new NeuralNetwork(INPUT_LAYER_SIZE, HIDDEN_LAYER_COUNT, HIDDEN_LAYER_SIZE, OUTPUT_LAYER_SIZE);
        network2 = new NeuralNetwork(INPUT_LAYER_SIZE, HIDDEN_LAYER_COUNT, HIDDEN_LAYER_SIZE, OUTPUT_LAYER_SIZE);

        this.gameCount = gameCount;
    }


    public void startCalculating() {
        int numberOfThreads = 20;
        System.out.println("Round,Score");
        NeuralNetwork best = null;
        List<NeuralNetwork> neuralNetworks = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; ++i) {
            neuralNetworks.add(initializeNeuralNetwork());
        }

        for (int i = 0; i < 200; ++i) {
            System.out.print(i + ",");
            best = runThreadsGetBest(neuralNetworks, numberOfThreads);

            neuralNetworks = new ArrayList<>();
            neuralNetworks.add(best);
            for (int j = 0; j < numberOfThreads - 1; ++j) {
                NeuralNetwork network = best.copyNetwork();
                network.changeNetwork(0.3);
                neuralNetworks.add(network);
            }

            try {
                NeuralNetworkConfig.saveNeuralNetwork("src/main/resources/test.json", best);
            } catch (IOException e) {
                System.out.println("something");
            }
        }
    }

    private NeuralNetwork runThreadsGetBest(List<NeuralNetwork> neuralNetworks, int numberOfThreads) {
        List<TrainingThread> trainingThreads = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; ++i) {
            ArtificialIntelligenceInterface ai = new MonteCarlo(null);
            TrainingThread thread = new TrainingThread(neuralNetworks.get(i), ai);
            thread.start();
            trainingThreads.add(thread);
        }


        for (TrainingThread thread : trainingThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("got interrupted");
            }
        }

        TrainingThread best = trainingThreads.stream().max(Comparator.comparing(TrainingThread::getScore)).orElseThrow(NoSuchElementException::new);
        System.out.print(best.getScore() + "\n");
        return best.getNeuralNetwork();

    }

    private NeuralNetwork initializeNeuralNetwork() {
        return new NeuralNetwork(INPUT_LAYER_SIZE, HIDDEN_LAYER_COUNT, HIDDEN_LAYER_SIZE, OUTPUT_LAYER_SIZE);
    }
}
