package NeuralNet.learning;

import NeuralNet.NeuralNetwork;
import java.util.List;
import gamecore.VierGewinnt;
import gamecore.VierGewinntInterface;
import montecarlo.ArtificialIntelligenceInterface;
import montecarlo.NeuralNetworkGameConnector;

public class TrainingThread extends Thread {
    private int totalGames;

    private List<ArtificialIntelligenceInterface> ais;
    private NeuralNetwork neuralNetwork;
    private double score = 0;

    public TrainingThread(NeuralNetwork network, List<ArtificialIntelligenceInterface> ais, int totalGames) {
        this.ais = ais;
        this.neuralNetwork = network;
        this.totalGames = totalGames;
    }

    public void setAis(List<ArtificialIntelligenceInterface> ais) {
        this.ais = ais;
    }

    @Override
    public void run() {
        score = 0;
        for (ArtificialIntelligenceInterface ai : ais) {
            int games;
            VierGewinntInterface vierGewinnt = new VierGewinnt();
            NeuralNetworkGameConnector gameConnector = new NeuralNetworkGameConnector(vierGewinnt);
            gameConnector.setNeuralNetwork(neuralNetwork);
            ai.refreshBoard(vierGewinnt);

            for (games = 0; games < totalGames; ++games) {
                runGame(ai,gameConnector,vierGewinnt);
            }
        }
    }

    private void runGame(ArtificialIntelligenceInterface ai, NeuralNetworkGameConnector gameConnector, VierGewinntInterface vierGewinnt) {
        playGame(ai, gameConnector, vierGewinnt);
        int winner = vierGewinnt.checkWinner();
        if (gameConnector.isPlacedRandomStone()) {
            winner = 2;
        }
        if (winner == 1) {
            //((LevelOneAlgo) ai).reshuffle();
            score += 1.2;
        } else if(winner == 2){
            score -= 0.8;
        }

        vierGewinnt = new VierGewinnt();
        gameConnector.setVierGewinnt(vierGewinnt);
        ai.refreshBoard(vierGewinnt);

        playGame(gameConnector, ai, vierGewinnt);
        winner = vierGewinnt.checkWinner();
        if (gameConnector.isPlacedRandomStone()) {
            winner = 1;
        }
        if (winner == 2) {
            //((LevelOneAlgo) ai).reshuffle();
            score += 1;
        } else if(winner == 1){
            score -= 1.2;
        }

        vierGewinnt = new VierGewinnt();
        gameConnector.setVierGewinnt(vierGewinnt);
        ai.refreshBoard(vierGewinnt);
    }

    public double getScore() {
        return score;
    }

    private void playGame(ArtificialIntelligenceInterface player1, ArtificialIntelligenceInterface player2, VierGewinntInterface vierGewinnt) {
        while (vierGewinnt.checkWinner() == 0) {
            vierGewinnt.nextPlayer();
            player1.makeNextMove();
            if (vierGewinnt.checkWinner() != 0) {
                break;
            }
            vierGewinnt.nextPlayer();
            player2.makeNextMove();
        }
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }
}

