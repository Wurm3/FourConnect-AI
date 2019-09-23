package NeuralNet;

import java.util.ArrayList;

public class ForwardPropagation implements NeuralNetworkInterface {


    public ForwardPropagation() {
    }

    @Override
    public double[] getOutput(Neuron[] inputLayer, Neuron[][] hiddenLayers, Neuron[] outputLayer) {

        //Propagate input Layer
        double[][] output = new double[inputLayer.length][];

        for (int i = 0; i < inputLayer.length; ++i) {
            output[i] = inputLayer[i].getActivationWeight();
        }

        double[] nextActivation = addWeights(output, inputLayer);

        for (int i = 0; i < hiddenLayers.length - 1; ++i) {
            double[][] hiddenOutput = new double[hiddenLayers[i + 1].length][];
            for (int j = 0; j < hiddenLayers[i].length; ++j) {
                hiddenLayers[i][j].setActivation(nextActivation[j]);
                hiddenOutput[j] = hiddenLayers[i][j].getActivationWeight();
            }
            nextActivation = addWeights(hiddenOutput, hiddenLayers[i + 1]);
        }

        double[][] hiddenOutput = new double[hiddenLayers[hiddenLayers.length - 1].length][outputLayer.length];
        for (int i = 0; i < hiddenLayers[hiddenLayers.length - 1].length; ++i) {
            hiddenLayers[hiddenLayers.length - 1][i].setActivation(nextActivation[i]);
            hiddenOutput[i] = hiddenLayers[hiddenLayers.length - 1][i].getActivationWeight();
        }

        return getSoftmax(addWeights(hiddenOutput, outputLayer));
    }

    private double[] addWeights(double[][] activation, Neuron[] net) {
        double[] nextActivation = new double[activation[0].length];
        for (int i = 0; i < activation[0].length; ++i) {
            double sum = 0;
            for (int j = 0; j < activation.length; ++j) {
                sum += activation[j][i];
            }
            sum += net[i].getBias();

            //nextActivation[i] = sum;
            nextActivation[i] = Math.pow(Math.E, sum) / (1 + Math.pow(Math.E, sum));
        }
        return nextActivation;
    }

    private double[] getSoftmax(double[] activation) {
        double[] output = new double[activation.length];
        double product = 1;
        for (double d : activation) {
            product += Math.pow(Math.E, d);
        }
        for (int i = 0; i < activation.length; ++i) {
            output[i] = Math.pow(Math.E, activation[i]) / product;
        }
        return output;
    }
}
