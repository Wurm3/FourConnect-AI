package montecarlo;

import NeuralNet.ForwardPropagation;
import NeuralNet.NeuralNetwork;
import NeuralNet.NeuralNetworkConfig;
import gamecore.VierGewinntInterface;


public class NeuralNetworkGameConnector implements ArtificialIntelligenceInterface {
    private VierGewinntInterface vierGewinnt;
    private NeuralNetwork neuralNetwork;
    private boolean placedRandomStone;

    public NeuralNetworkGameConnector(VierGewinntInterface vierGewinnt) {
        this.vierGewinnt = vierGewinnt;
    }

    public NeuralNetworkGameConnector(VierGewinntInterface vierGewinnt, String path){
        this.vierGewinnt = vierGewinnt;
        try {
            neuralNetwork = NeuralNetworkConfig.loadNeuralNetwork("src/main/resources/test.json");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void setNeuralNetwork(NeuralNetwork network){
        neuralNetwork = network;
    }


    public void setVierGewinnt(VierGewinntInterface vierGewinnt) {
        this.vierGewinnt = vierGewinnt;
    }

    @Override
    public int[] makeNextMove() {
        placedRandomStone = false;
        int[][] input = vierGewinnt.getBoard();
        int player = vierGewinnt.getPlayer();

        double[] sanatizedInput = new double[6 * 7 * 2 + 1];

        boolean allNull = true;
        for (int playerCounter = 1; playerCounter < 3; ++playerCounter) {
            for (int x = 0; x < input.length; ++x) {
                for (int y = 0; y < input[x].length; ++y) {
                    if(playerCounter == 0){
                        if(player == input[x][y]){
                            sanatizedInput[playerCounter*x*y] = 1.0;
                            allNull = false;
                        }else if(input[x][y] == 0){
                            sanatizedInput[playerCounter*x*y] = 0.0;
                        }
                    }else{
                        if(player != input[x][y] && input[x][y] != 0){
                            sanatizedInput[playerCounter*x*y] = 1.0;
                            allNull = false;
                        }else if(input[x][y] == 0){
                            sanatizedInput[playerCounter*x*y] = 0.0;
                        }
                    }
                }
            }
        }
        if(allNull){
            sanatizedInput[sanatizedInput.length-1] = 1;
        }else{
            sanatizedInput[sanatizedInput.length-1] = 0;
        }

        double[] output = neuralNetwork.getOutput(sanatizedInput, new ForwardPropagation());

        double highest = 0;
        int index = -1;

        for(int i = 0; i < output.length;++i){
            if(output[i] > highest){
                highest = output[i];
                index = i;
            }
        }

        int y = vierGewinnt.placeStone(index);
        while(y == -1){
            placedRandomStone = true;
            index = placeRandomStone();
            y = vierGewinnt.placeStone(index);
        }
        int[] xy = {index, y};

        return xy;
    }

    public boolean isPlacedRandomStone(){
        return placedRandomStone;
    }

    private int placeRandomStone(){
        return (int) (Math.random() * 6);
    }

    @Override
    public void refreshBoard(VierGewinntInterface vierGewinnt) {
        this.vierGewinnt = vierGewinnt;
    }
}
