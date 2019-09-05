package NeuralNet.learning;

import NeuralNet.NeuralNetwork;
import gamecore.VierGewinnt;
import gamecore.VierGewinntInterface;
import montecarlo.ArtificialIntelligenceInterface;
import montecarlo.NeuralNetworkGameConnector;

public class TrainingThread extends Thread {
    private ArtificialIntelligenceInterface ai;
    private NeuralNetwork neuralNetwork;
    private double score = 0;

    public TrainingThread(NeuralNetwork network, ArtificialIntelligenceInterface ai) {
        this.ai = ai;
        this.neuralNetwork = network;
    }

    @Override
    public void run() {
        VierGewinntInterface vierGewinnt = new VierGewinnt();
        NeuralNetworkGameConnector gameConnector = new NeuralNetworkGameConnector(vierGewinnt);
        gameConnector.setNeuralNetwork(neuralNetwork);
        ai.refreshBoard(vierGewinnt);

        int winning = 0;
        int loosing = 0;
        for (int i = 0; i < 200; ++i) {
            while (vierGewinnt.checkWinner() == 0) {
                vierGewinnt.nextPlayer();
                gameConnector.makeNextMove();
                if (vierGewinnt.checkWinner() != 0 || gameConnector.isPlacedRandomStone()) {
                    break;
                }
                vierGewinnt.nextPlayer();
                ai.makeNextMove();
            }
            int winner = vierGewinnt.checkWinner();
            if(gameConnector.isPlacedRandomStone()){
                winner = 1;
            }
            if (winner == 2) {
                winning += 1;
            } else {
                loosing += 1;
            }
            vierGewinnt = new VierGewinnt();
            gameConnector.setVierGewinnt(vierGewinnt);
            ai.refreshBoard(vierGewinnt);
        }

        loosing = (loosing == 0 ? 1 : loosing);
        score = (double) winning / (double) loosing;
    }

    public double getScore() {
        return score;
    }


    public NeuralNetwork getNeuralNetwork(){
        return neuralNetwork;
    }
}

