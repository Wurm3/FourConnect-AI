package NeuralNet;

public class NeuralNetwork {
    private Neuron[] inputLayer;
    private Neuron[][] hiddenLayers;
    private Neuron[] outputLayer;

    public NeuralNetwork() {
    }

    public NeuralNetwork(int inputLayerSize, int hiddenLayerCount, int hiddenLayersSize, int outputLayerSize) {
        inputLayer = new Neuron[inputLayerSize];

        //Initialize input layers
        for (int i = 0; i < inputLayerSize; ++i) {
            inputLayer[i] = new Neuron();
            inputLayer[i].setWeights(initializeRandomWeights(hiddenLayersSize));
            inputLayer[i].setBias(0);
        }

        //Initialize hidden layers
        hiddenLayers = new Neuron[hiddenLayerCount][hiddenLayersSize];
        for (int hidden = 0; hidden < hiddenLayerCount - 1; ++hidden) {
            for (int neuronIndex = 0; neuronIndex < hiddenLayersSize; ++neuronIndex) {
                hiddenLayers[hidden][neuronIndex] = new Neuron();
                hiddenLayers[hidden][neuronIndex].setWeights(initializeRandomWeights(hiddenLayersSize));
                hiddenLayers[hidden][neuronIndex].setBias(0);
            }
        }
        //connect to ouputLayer
        for (int i = 0; i < hiddenLayers[hiddenLayerCount - 1].length; ++i) {
            hiddenLayers[hiddenLayerCount - 1][i] = new Neuron();
            hiddenLayers[hiddenLayerCount - 1][i].setWeights(initializeRandomWeights(outputLayerSize));
            hiddenLayers[hiddenLayerCount - 1][i].setBias(0);
        }

        //initialize output layer
        //actually pretty pointles but makes it easier for me down the road
        outputLayer = new Neuron[outputLayerSize];
        for (int i = 0; i < outputLayerSize; ++i) {
            outputLayer[i] = new Neuron();
            outputLayer[i].setBias(0);
            outputLayer[i].setWeights(new double[1]);
        }
    }

    public NeuralNetwork(Neuron[] inputLayer, Neuron[][] hiddenLayers, Neuron[] outputLayer) {
        this.hiddenLayers = hiddenLayers;
        this.inputLayer = inputLayer;
        this.outputLayer = outputLayer;
    }

    public double[] getOutput(double[] input, NeuralNetworkInterface networkCalculationType) {
        if (input.length != inputLayer.length) {
            throw new IllegalArgumentException("input and inputLayer must be of the same size");
        }

        for (int i = 0; i < inputLayer.length; ++i) {
            inputLayer[i].setActivation(input[i]);
        }
        return networkCalculationType.getOutput(inputLayer, hiddenLayers, outputLayer);
    }

    private double[] initializeRandomWeights(int size) {
        double[] randomWeights = new double[size];

        for (int i = 0; i < size; ++i) {
            randomWeights[i] = randomWeight();
        }
        return randomWeights;
    }


    private double randomWeight() {
        return Math.random() * 2 - 1;
    }

    private void randomizeWeights(double[] weights, double mutationRate) {
        for (int i = 0; i < weights.length; ++i) {
            if (Math.random() <= mutationRate) {
                weights[i] += randomWeight();
            }
        }
    }

    public void changeNetwork(double mutationRate) {
        for (int i = 0; i < inputLayer.length; ++i) {
            randomizeWeights(inputLayer[i].getWeights(), mutationRate);
            if(Math.random() <= mutationRate){
                inputLayer[i].setBias(0);
            }
        }

        for (Neuron[] hiddenLayer : hiddenLayers) {
            for (int i = 0; i < hiddenLayer.length; ++i) {
                randomizeWeights(hiddenLayer[i].getWeights(), mutationRate);
                if(Math.random() <= mutationRate) {
                    hiddenLayer[i].setBias(0);
                }
            }
        }
    }

    public NeuralNetwork copyNetwork() {
        Neuron[] copyInputLayer = new Neuron[inputLayer.length];
        for (int i = 0; i < inputLayer.length; ++i) {
            copyInputLayer[i] = inputLayer[i].clone();
        }

        Neuron[] copyOutputLayer = new Neuron[outputLayer.length];
        for (int i = 0; i < outputLayer.length; ++i) {
            copyOutputLayer[i] = outputLayer[i].clone();
        }

        Neuron[][] copyhiddenLayers = new Neuron[hiddenLayers.length][hiddenLayers[0].length];
        for (int i = 0; i < hiddenLayers.length; ++i) {
            for (int j = 0; j < hiddenLayers[i].length; ++j) {
                copyhiddenLayers[i][j] = hiddenLayers[i][j].clone();
            }
        }
        return new NeuralNetwork(copyInputLayer, copyhiddenLayers, copyOutputLayer);
    }

    public NeuralNetwork copyNetworkAndChange() {
        NeuralNetwork network = copyNetwork();

        //Change inputlayer
        for (int neuronIndex = 0; neuronIndex < network.getInputLayer().length; ++neuronIndex) {
            for (int weightIndex = 0; weightIndex < network.getHiddenLayers()[0].length; ++weightIndex) {
                changeWeightOfNeuron(network.getInputLayer(), neuronIndex, weightIndex);
            }
        }

        //Change hiddenLayers except last
        for (int layer = 0; layer < hiddenLayers.length - 1; ++layer) {
            for (int neuronIndex = 0; neuronIndex < network.getHiddenLayers()[0].length; ++neuronIndex) {
                for (int weightIndex = 0; weightIndex < network.getHiddenLayers()[0].length; ++weightIndex) {
                    changeWeightOfNeuron(network.getHiddenLayers()[layer], neuronIndex, weightIndex);
                    changeBias(hiddenLayers[layer][neuronIndex]);
                }

            }
        }

        for (int neuronIndex = 0; neuronIndex < network.getHiddenLayers()[0].length; ++neuronIndex) {
            for (int weightIndex = 0; weightIndex < network.getOutputLayer().length; ++weightIndex) {
                changeWeightOfNeuron(network.getHiddenLayers()[network.getHiddenLayers().length - 1], neuronIndex, weightIndex);
                changeBias(hiddenLayers[hiddenLayers.length - 1][neuronIndex]);
            }
        }

        return network;
    }

    public void changeWeightOf(int layer, int neuronIndex, int weightIndex) {
        if (layer == 0) {
            changeWeightOfNeuron(inputLayer, neuronIndex, weightIndex);
        } else if (layer > 0 && layer <= hiddenLayers.length) {
            changeWeightOfNeuron(hiddenLayers[layer - 1], neuronIndex, weightIndex);
        } else if (layer == hiddenLayers.length + 1) {
            changeWeightOfNeuron(outputLayer, neuronIndex, weightIndex);
        } else {
            throw new IllegalArgumentException("Not a valid layer");
        }
    }

    public void changeWeightOfNeuron(Neuron[] layer, int index, int weightIndex) {
        Neuron neuron = layer[index];
        double[] weights = neuron.getWeights();
        weights[weightIndex] = weights[weightIndex] + (Math.random() - 0.5);
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

    public void changeBias(Neuron neuron){
        double currentBias = neuron.getBias();
        neuron.setBias(currentBias + ((Math.random() * 2) - 1));
    }
}
