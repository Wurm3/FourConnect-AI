package montecarlo;

import gamecore.VierGewinnt;
import gamecore.VierGewinntInterface;

import java.util.concurrent.ThreadLocalRandom;

public class MonteCarlo implements ArtificialIntelligenceInterface{
    private final double bias = 0.3;
    private VierGewinntInterface vierGewinnt;

    public MonteCarlo(VierGewinntInterface vierGewinnt){
        this.vierGewinnt = vierGewinnt;
    }

    public void refreshBoard(VierGewinntInterface vierGewinnt){
        this.vierGewinnt = vierGewinnt;
    }

    public int otherPlayer(int player){
        return player == 1? 2 : 1;
    }

    public int[] makeNextMove(){
        int player = vierGewinnt.getPlayer();
        VierGewinntInterface simulation = new VierGewinnt();
        simulation.setPlayer(player);
        simulation.setBoard(copyBoard(vierGewinnt.getBoard()));

        Node parentNode = new Node(0,otherPlayer(player),copyBoard(vierGewinnt.getBoard()));
        expand(parentNode,0);

        int winningMove = getBestMove(parentNode).getIndex();
        int y = vierGewinnt.placeStone(winningMove);
        int[] xy = {winningMove, y};
        return xy;
    }

    public Node getBestMove(Node parent){
        Node bestChild = null;
        double bestValue = 0;
        for(Node child : parent.getChildren()){
            if(child.getValue() >= bestValue){
                bestValue = child.getValue();
                bestChild = child;
            }
        }
        return bestChild;
    }

    private void expand(Node parent,int expansion){
        ++expansion;
        if(expansion < 40){

            int[] moves = getPossibleMoves(parent.getBoard());

            //Adds children to parent
            for(int i = 0; i < 7; ++i){
                if(moves[i] == 1)
                    parent.addChild(new Node(i,otherPlayer(parent.getPlayer()),copyBoard(parent.getBoard())));
            }
            runGames(parent,1000);

            while(!getMostPromisingChild(parent).equals(getBestMove(parent))){
                Node promisingChild = getMostPromisingChild(parent);

                expand(promisingChild,expansion);
                int winnings = promisingChild.getWinnings();
                int loosings = promisingChild.getLoosings();
                int draws = promisingChild.getDraws();
                parent.addWinnings(promisingChild.getLoosings() - loosings);
                parent.addLoosings(promisingChild.getWinnings() - winnings);
                parent.addDraws(promisingChild.getDraws() - draws);
            }
        }
    }

    private void runGames(Node parent, int times){
        while(parent.getPlayed() < times){
            Node node = getMostPromisingChild(parent);
            int win = getRandomWinning(node);
            node.updateResults(win);
            parent.updateResults(win);
        }
    }

    private Node getMostPromisingChild(Node parent){
        double best = 0;
        Node bestChild = null;
        for(Node child : parent.getChildren()){
            if(parent.getPlayed() == 0|| child.getPlayed() == 0){
                return child;
            }
            double next = child.getValue() + bias * Math.sqrt((Math.log(parent.getPlayed()))/child.getPlayed());

            if(next >= best){
                bestChild = child;
                best = next;
            }
        }
        return bestChild;
    }

    private int getRandomWinning(Node node){
        VierGewinntInterface randomGame = new VierGewinnt();
        randomGame.setBoard(copyBoard(node.getBoard()));
        randomGame.setPlayer(node.getPlayer());
        int y = randomGame.placeStone(node.getIndex());
        int winner = randomGame.checkWinner();


        while(winner == 0){
            randomGame.nextPlayer();
            int[] moves = getPossibleMoves(randomGame.getBoard());
            int randomNum;
            do{
                randomNum = ThreadLocalRandom.current().nextInt(0,7);
            } while(moves[randomNum] != 1);
            randomGame.placeStone(randomNum);
            winner = randomGame.checkWinner();
        }
        return winner;
    }

    private int[] getPossibleMoves(int[][] board){
        int[] moves = new int[7];
        for(int x = 0; x < 7; ++x) {
            if (board[x][5] == 0)
                moves[x] = 1;
        }
        return moves;
    }

    private int[][] copyBoard(int [][] board){
        int[][] newBoard = new int[7][6];
        for(int i = 0; i < 7; ++i){
            for(int j = 0; j < 6; ++j){
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }
}