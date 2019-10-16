package NeuralNet.learning;

import NeuralNet.NeuralNetwork;
import NeuralNet.NeuralNetworkConfig;
import montecarlo.ArtificialIntelligenceInterface;
import montecarlo.LevelTwoAlgo;
import montecarlo.MonteCarlo;
import montecarlo.NeuralNetworkGameConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MinMaxSimpleAlgo implements LearningAlgoInterface {
    private static final int INPUT_LAYER_SIZE = 85; //number of tiles
    private static final int HIDDEN_LAYER_COUNT = 2;
    private static final int HIDDEN_LAYER_SIZE = 60; // 6 + 7 + 5
    private static final int OUTPUT_LAYER_SIZE = 7;
    private static final int numberOfThreads = 40;

    private NeuralNetwork network1;

    private List<NeuralNetwork> bestNetworks;
    private NeuralNetwork trainingNetwork;


    private int gameCount;

    public MinMaxSimpleAlgo(int gameCount) {
        network1 = new NeuralNetwork(INPUT_LAYER_SIZE, HIDDEN_LAYER_COUNT, HIDDEN_LAYER_SIZE, OUTPUT_LAYER_SIZE);

        this.gameCount = gameCount;
    }

    public NeuralNetwork loadExistingNetwork() {
        try {

            return NeuralNetworkConfig.loadNeuralNetwork("src/main/resources/test.json");
        } catch (IOException e) {
            return null;
        }
    }


    public void iterateChanges() {
        trainingNetwork = loadExistingNetwork();
        //trainingNetwork = initializeNeuralNetwork();
        bestNetworks = new ArrayList<>();
        bestNetworks.add(trainingNetwork);

        List<NeuralNetwork> neuralNetworks = new ArrayList<>();
        addMultipleToList(neuralNetworks, trainingNetwork, 10);
        //addRandomToList(neuralNetworks, 80);

        for (int neuronIndex = 0; neuronIndex < INPUT_LAYER_SIZE; ++neuronIndex) {
            for (int weightIndex = 0; weightIndex < HIDDEN_LAYER_SIZE; ++weightIndex) {
                for (NeuralNetwork neuralNetwork : neuralNetworks) {
                    neuralNetwork.changeWeightOf(0, neuronIndex, weightIndex);
                }
                List<TrainingThread> sortedList = runThreadsGetBest(neuralNetworks);
                if (sortedList.get(sortedList.size() - 1).getScore() > 0.5) {
                    bestNetworks = new ArrayList<>();
                    bestNetworks.add(sortedList.get(sortedList.size() - 1).getNeuralNetwork());
                    saveBest();
                } else {
                    neuralNetworks = new ArrayList<>();
                    addMultipleToList(neuralNetworks, trainingNetwork, 10);
                    break;
                }

            }
        }
    }

    private void addRandomToList(List<NeuralNetwork> neuralNetworks, int count) {
        for (int i = 0; i < count; ++i) {
            neuralNetworks.add(initializeNeuralNetwork());
        }
    }

    private void addMultipleToList(List<NeuralNetwork> neuralNetworks, NeuralNetwork copy, int times) {
        for (int i = 0; i < times; ++i) {
            neuralNetworks.add(copy.copyNetwork());
        }
    }

    public void startCalculating() {
        trainingNetwork = loadExistingNetwork();
        //trainingNetwork = initializeNeuralNetwork();
        bestNetworks = new ArrayList<>();
        bestNetworks.add(trainingNetwork);

        double maxLastRounds = -100;
        int nanCounter = 0;
        System.out.println("Round,Score");

        List<NeuralNetwork> neuralNetworks = new ArrayList<>();
        neuralNetworks.add(trainingNetwork);
        double threshold = 5;
        for (int i = 0; i < gameCount; ++i) {
            List<TrainingThread> sortedList = runThreadsGetBest(neuralNetworks);
            maxLastRounds = maxLastRounds < sortedList.get(sortedList.size() - 1).getScore() ? sortedList.get(sortedList.size() - 1).getScore() : maxLastRounds;


            if (sortedList.get(sortedList.size() - 1).getScore() > threshold) {
                maxLastRounds = -100;
                if (nanCounter == 0) {
                    threshold += 0.1;
                }
                nanCounter = 0;
                System.out.print(i + "," + sortedList.get(sortedList.size() - 1).getScore());

                trainingNetwork = sortedList.get(sortedList.size() - 1).getNeuralNetwork();
                bestNetworks = new ArrayList<>();
                double score;
                int number = 4;
                int counter = sortedList.size() - 1;
                do {
                    bestNetworks.add(sortedList.get(counter).getNeuralNetwork());
                    score = counter == 0 ? 0 : sortedList.get(counter - 1).getScore();
                    --counter;
                    --number;
                } while (counter >= 0 && number > 0 && score > threshold - 5);
                int surrived = (sortedList.size() - counter - 1);
                System.out.print(";\t" + surrived + " surrived\n");

                saveBest();
            } else {
                ++nanCounter;
                if (nanCounter % 50 == 0) {
                    System.out.println(i + ", NaN\tMax: " + maxLastRounds);
                    maxLastRounds = -100;
                }
                bestNetworks = new ArrayList<>();
                bestNetworks.add(trainingNetwork);

            }
            neuralNetworks = new ArrayList<>();
            neuralNetworks.addAll(bestNetworks);
            for (int j = 0; j < numberOfThreads - bestNetworks.size(); j += bestNetworks.size()) {

                for (NeuralNetwork best : bestNetworks) {
                    NeuralNetwork network = best.copyNetworkAndChange();
                    network.changeNetwork(0.3);
                    neuralNetworks.add(network);
                }
            }
        }
    }

    private void changeWeight(NeuralNetwork network, int layer, int neuronIndex, int weightIndex) {
        network.changeWeightOf(layer, neuronIndex, weightIndex);
    }

    private void saveBest() {
        try {
            NeuralNetworkConfig.saveNeuralNetwork("src/main/resources/test.json", bestNetworks.get(bestNetworks.size() - 1));
        } catch (IOException e) {
            System.out.println("something");
        }
    }

    private List<TrainingThread> runThreadsGetBest(List<NeuralNetwork> neuralNetworks) {
        List<TrainingThread> trainingThreads = new ArrayList<>();

        List<List<ArtificialIntelligenceInterface>> aiss = new ArrayList<>();
        for (int j = 0; j < neuralNetworks.size(); ++j) {
            List<ArtificialIntelligenceInterface> ais = new ArrayList<>();
            for (int i = 0; i < 10; ++i) {//(NeuralNetwork network : neuralNetworks){
                //NeuralNetworkGameConnector ai = new NeuralNetworkGameConnector(null);
                ArtificialIntelligenceInterface ai = new LevelTwoAlgo(null);
                //ai.setNeuralNetwork(network);
                ais.add(ai);
            }
            aiss.add(ais);
        }

        for (int i = 0; i < neuralNetworks.size(); ++i) {
            TrainingThread thread = new TrainingThread(neuralNetworks.get(i), aiss.get(i), 1);
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
        List<TrainingThread> sorted = trainingThreads.stream().sorted(Comparator.comparing(TrainingThread::getScore)).collect(Collectors.toList());
        return sorted;

    }

    private NeuralNetwork initializeNeuralNetwork() {
        return new NeuralNetwork(INPUT_LAYER_SIZE, HIDDEN_LAYER_COUNT, HIDDEN_LAYER_SIZE, OUTPUT_LAYER_SIZE);
    }

    private void initializeFromNetwork(List<NeuralNetwork> networks, NeuralNetwork network, int number) {
        for (int i = 0; i < number; ++i) {
            networks.add(network.copyNetworkAndChange());
        }
    }
}
