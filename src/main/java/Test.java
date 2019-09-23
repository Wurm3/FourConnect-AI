import NeuralNet.ForwardPropagation;
import NeuralNet.NeuralNetwork;
import NeuralNet.NeuralNetworkConfig;
import NeuralNet.NeuralNetworkInterface;
import NeuralNet.learning.MinMaxSimpleAlgo;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args){
        MinMaxSimpleAlgo algo = new MinMaxSimpleAlgo(100000);
        //algo.iterateChanges();
        algo.startCalculating();

    }
}
