package NeuralNet;

import org.nd4j.shade.jackson.annotation.JsonIgnore;

public class Neuron {
    private double[] weights;
    private double activation;

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double[] getWeights() {
        return weights;
    }

    public void changeWeight(int index, double weight){
        weights[index] = weight;
    }

    public void setActivation(double activation) {
        this.activation = activation;
    }

    public double getActivation() {
        return activation;
    }

    @JsonIgnore
    public double[] getActivationWeight(){
        double[] activationWeight = new double[weights.length];

        for(int i = 0; i < weights.length; ++i){
            activationWeight[i] = weights[i] * activation;
        }

        return activationWeight;
    }

    @Override
    public Neuron clone(){
        double[] copyWeights = weights.clone();
        Neuron other = new Neuron();
        other.setWeights(copyWeights);
        return other;
    }
}
