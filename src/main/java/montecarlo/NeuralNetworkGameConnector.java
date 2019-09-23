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

        /**
         * TODO fix that mess
         */
        boolean allNull = true;
        for (int playerCounter = 1; playerCounter < 3; ++playerCounter) {
            for (int x = 1; x <= input.length; ++x) {
                for (int y = 1; y <= input[x-1].length; ++y) {
                    if(playerCounter == 1){
                        if(player == input[x-1][y-1]){
                            sanatizedInput[x*y] = 1.0;
                            allNull = false;
                        }else if(input[x - 1][y - 1] == 0){
                            sanatizedInput[x*y] = 0.0;
                        }
                    }else{
                        if(player != input[x - 1][y - 1] && input[x - 1][y - 1] != 0){
                            sanatizedInput[x*y + 42] = 1.0;
                            allNull = false;
                        }else if(input[x - 1][y - 1] == 0){
                            sanatizedInput[x*y + 42] = 0.0;
                        }
                    }
                }
            }
        }
        if(allNull){
            sanatizedInput[0] = 1;
        }else{
            sanatizedInput[0] = 0;
        }

        double[] output = neuralNetwork.getOutput(sanatizedInput, new ForwardPropagation());

        double highest = -1;
        int index = -1;

        for(int i = 0; i < output.length;++i){
            if(output[i] > highest){
                highest = output[i];
                index = i;
            }
        }


        int y;
        if(index != -1 ){
            y = vierGewinnt.placeStone(index);
        }else{
            y = -1;
        }
        int randomIndex = 0;
        while(y == -1){
            if(randomIndex > 6){
                return null;
            }
            placedRandomStone = true;
            //index = placeRandomStone();
            y = vierGewinnt.placeStone(randomIndex);
            index = randomIndex;
            ++randomIndex;
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
