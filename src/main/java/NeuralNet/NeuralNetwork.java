package NeuralNet;

public class NeuralNetwork {
    private Neuron[] inputLayer;
    private Neuron[][] hiddenLayers;
    private Neuron[] outputLayer;

    public NeuralNetwork(){}

    public NeuralNetwork(int inputLayerSize, int hiddenLayerCount, int hiddenLayersSize, int outputLayerSize){
        inputLayer = new Neuron[inputLayerSize];

        //Initialize input layers
        for(int i = 0; i < inputLayerSize; ++i){
            inputLayer[i] = new Neuron();
            inputLayer[i].setWeights(initializeRandomWeights(hiddenLayersSize));
        }

        //Initialize hidden layers
        hiddenLayers = new Neuron[hiddenLayerCount][hiddenLayersSize];
        for(int hidden = 0; hidden < hiddenLayerCount - 1; ++hidden){
            for(int neuronIndex = 0; neuronIndex < hiddenLayersSize; ++neuronIndex){
                hiddenLayers[hidden][neuronIndex] = new Neuron();
                hiddenLayers[hidden][neuronIndex].setWeights(initializeRandomWeights(hiddenLayersSize));
            }
        }
        //connect to ouputLayer
        for(int i = 0; i < hiddenLayers[hiddenLayerCount - 1].length; ++i){
            hiddenLayers[hiddenLayerCount-1][i] = new Neuron();
            hiddenLayers[hiddenLayerCount-1][i].setWeights(initializeRandomWeights(outputLayerSize));
        }

        outputLayer = new Neuron[outputLayerSize];
    }

    public NeuralNetwork(Neuron[] inputLayer, Neuron[][] hiddenLayers, Neuron[] outputLayer){
        this.hiddenLayers = hiddenLayers;
        this.inputLayer = inputLayer;
        this.outputLayer = outputLayer;
    }

    public double[] getOutput(double[] input, NeuralNetworkInterface networkCalculationType){
        if(input.length != inputLayer.length){
            throw new IllegalArgumentException("input and inputLayer must be of the same size");
        }

        for(int i = 0; i < inputLayer.length; ++i){
            inputLayer[i].setActivation(input[i]);
        }
        return networkCalculationType.getOutput(inputLayer,hiddenLayers,outputLayer);
    }

    private double[] initializeRandomWeights(int size){
        double[] randomWeights = new double[size];

        for(int i = 0; i < size; ++i){
            randomWeights[i] = randomWeight();
        }
        return randomWeights;
    }


    private double randomWeight(){
        return Math.random() * 2 - 1;
    }

    private void randomizeWeights(double[] weights, double mutationRate){
        for(int i = 0; i < weights.length; ++i){
            if(Math.random() <= mutationRate){
                weights[i] += randomWeight();
            }
        }
    }

    public void changeNetwork(double mutationRate){
        for(int i = 0; i < inputLayer.length; ++i){
            randomizeWeights(inputLayer[i].getWeights(), mutationRate);
        }

        for(Neuron[] hiddenLayer : hiddenLayers){
            for(int i = 0; i < hiddenLayer.length; ++i){
                randomizeWeights(hiddenLayer[i].getWeights(),mutationRate);
            }
        }
    }

    public NeuralNetwork copyNetwork(){
        Neuron[] copyInputLayer = new Neuron[inputLayer.length];
        for(int i = 0; i < inputLayer.length;++i){
            copyInputLayer[i] = inputLayer[i].clone();
        }

        Neuron[] copyOutputLayer = new Neuron[outputLayer.length];
        /**for(int i = 0; i < outputLayer.length;++i){
            copyOutputLayer[i] = outputLayer[i].clone();
        }**/

        Neuron[][] copyhiddenLayers = new Neuron[hiddenLayers.length][hiddenLayers[0].length];
        for(int i = 0; i < hiddenLayers.length; ++i){
            for(int j = 0; j < hiddenLayers[i].length; ++j){
                copyhiddenLayers[i][j] = hiddenLayers[i][j].clone();
            }
        }
        return new NeuralNetwork(copyInputLayer,copyhiddenLayers,copyOutputLayer);
    }

    public Neuron[] getInputLayer() {
        return inputLayer;
    }

    public void setInputLayer(Neuron[] inputLayer) {
        this.inputLayer = inputLayer;
    }

    public Neuron[][] getHiddenLayers() {
        return hiddenLayers;
    }

    public void setHiddenLayers(Neuron[][] hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    public Neuron[] getOutputLayer() {
        return outputLayer;
    }

    public void setOutputLayer(Neuron[] outputLayer) {
        this.outputLayer = outputLayer;
    }
}
