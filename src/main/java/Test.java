import NeuralNet.ForwardPropagation;
import NeuralNet.NeuralNetwork;
import NeuralNet.NeuralNetworkConfig;
import NeuralNet.NeuralNetworkInterface;
import NeuralNet.learning.MinMaxSimpleAlgo;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args){
        int inputSize = 6*7*2 + 1;
        int hiddenLayerCount = 3;
        int hiddenLayerSize = 50;
        int outputSize = 7;

        MinMaxSimpleAlgo algo = new MinMaxSimpleAlgo(10000);
        algo.startCalculating();
        //NeuralNetwork network1 = new NeuralNetwork(inputSize,hiddenLayerCount,hiddenLayerSize,outputSize);
        //NeuralNetwork network2 = new NeuralNetwork(inputSize,hiddenLayerCount,hiddenLayerSize,outputSize);
/*
        NeuralNetworkInterface forward = new ForwardPropagation();

        double[] mememe = new double[]{1,0};

        for(int i = 0; i < 50000; ++i){
            double[] out1 = network1.getOutput(mememe,forward);
            double[] out2 = network2.getOutput(mememe,forward);


            double[] best;
            if(out1[0] < out2[0]){
                network1 = network2.copyNetwork();
                network1.changeNetwork(0.6 / i + 1);
                best = out2;
            }else{
                network2 = network1.copyNetwork();
                network2.changeNetwork(0.6 / i + 1);
                best = out1;
            }

            if(i % 20 == 0){
                System.out.println(best[0] + ":" + best[1]);
            }
        }*/


        /*try{
            NeuralNetworkConfig.saveNeuralNetwork("src/main/resources/test.json",network1);
            NeuralNetwork mememe = NeuralNetworkConfig.loadNeuralNetwork("src/main/resources/test.json");
            System.out.println(mememe);
        }catch(Exception e){
            System.out.println("ohm god :" + e);
        }*/
    }
}
