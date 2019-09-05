package NeuralNet;

public interface NeuralNetworkInterface {
    double[] getOutput(Neuron[] inputLayer, Neuron[][] hiddenLayers, Neuron[] outputLayer);
}
