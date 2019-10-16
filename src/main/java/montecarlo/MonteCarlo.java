package montecarlo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gamecore.VierGewinnt;
import gamecore.VierGewinntInterface;


public class MonteCarlo implements ArtificialIntelligenceInterface {
    private int expansion;

    private final double bias = 2;
    private VierGewinntInterface vierGewinnt;
    private int ownPlayer;

    public MonteCarlo(VierGewinntInterface vierGewinnt) {
        this.vierGewinnt = vierGewinnt;
    }

    public void refreshBoard(VierGewinntInterface vierGewinnt) {
        this.vierGewinnt = vierGewinnt;
    }

    public int otherPlayer(int player) {
        return player == 1 ? 2 : 1;
    }

    public int[] makeNextMove() {
        expansion = 0;
        int player = vierGewinnt.getPlayer();
        ownPlayer = player;

        VierGewinntInterface simulation = new VierGewinnt();
        simulation.setPlayer(player);
        simulation.setBoard(copyBoard(vierGewinnt.getBoard()));

        Node parentNode = new Node(-1, otherPlayer(player), copyBoard(vierGewinnt.getBoard()), null);
        expand(parentNode);

        int winningMove = getBestMove(parentNode).getIndex();
        int y = vierGewinnt.placeStone(winningMove);
        int[] xy = {winningMove, y};
        return xy;
    }

    public Node getBestMove(Node parent) {
        Node bestChild = null;
        double bestValue = 0;
        for (Node child : parent.getChildren()) {
            if (child.getValue() >= bestValue) {
                bestValue = child.getValue();
                bestChild = child;
            }
        }
        return bestChild;
    }

    private void expand(Node parent) {
        while (expansion < 20 && (parent.getParent() == null || getBestMove(parent.getParent()).equals(parent))) {
            ++expansion;
            if (parent.getChildren().isEmpty()) {
                expandOnce(parent);
            }

            Node promisingChild = getBestMove(parent);
            if (promisingChild.getChildren().isEmpty()) {
                expandOnce(promisingChild);
            }


            if (promisingChild.equals(getBestMove(parent))) {
                if (promisingChild.getValue() != Integer.MAX_VALUE) {
                    expand(promisingChild);
                }
                parent.addWinnings(promisingChild.getLoosings());
                parent.addLoosings(promisingChild.getWinnings());
                parent.addDraws(promisingChild.getDraws());
            }
        }
    }

    private void expandOnce(Node parent) {
        int[] moves = getPossibleMoves(parent.getBoard());
        //Adds children to parent
        if (parent.getChildren().isEmpty()) {
            for (int i = 0; i < 7; ++i) {
                if (moves[i] == 1)
                    if (parent.getIndex() != -1) {
                        VierGewinntInterface vierGewinnt = new VierGewinnt();
                        vierGewinnt.setBoard(copyBoard(parent.getBoard()));
                        vierGewinnt.setPlayer(parent.getPlayer());
                        vierGewinnt.placeStone(parent.getIndex());
                        parent.addChild(new Node(i, otherPlayer(parent.getPlayer()), vierGewinnt.getBoard(), parent));

                    } else {
                        parent.addChild(new Node(i, otherPlayer(parent.getPlayer()), copyBoard(parent.getBoard()), parent));
                    }
            }
        }
        runGames(parent, 1000);
        if (parent.getParent() != null && ownPlayer == parent.getPlayer()) {
            parent.addWinnings(getBestMove(parent).getLoosings() - parent.getWinnings());
            parent.addLoosings(getBestMove(parent).getWinnings() - parent.getLoosings());
            parent.addDraws(getBestMove(parent).getDraws() - parent.getDraws());
        }
    }

    private void runGames(Node parent, int times) {
        if (parent.getChildren().isEmpty()) {
            return;
        }
        int playedBefore = parent.getPlayed();
        while (parent.getPlayed() < times + playedBefore) {
            Node node = getMostPromisingChild(parent);
            int win = getRandomWinning(node);
            node.updateResults(win);
            parent.updateResults(win);
        }
    }

    private Node getMostPromisingChild(Node parent) {
        double best = 0;
        Node bestChild = null;
        for (Node child : parent.getChildren()) {
            if (parent.getPlayed() == 0 || child.getPlayed() == 0) {
                return child;
            }
            double next = child.getValue() + bias * Math.sqrt((Math.log(parent.getPlayed())) / child.getPlayed());

            if (next >= best) {
                bestChild = child;
                best = next;
            }
        }
        return bestChild;
    }

    private int getRandomWinning(Node node) {
        if (node == null) {
            return -1;
        }
        VierGewinntInterface randomGame = new VierGewinnt();
        randomGame.setBoard(copyBoard(node.getBoard()));
        randomGame.setPlayer(node.getPlayer());
        int y = randomGame.placeStone(node.getIndex());
        int winner = randomGame.checkWinner();


        while (winner == 0) {
            List<Integer> movesList = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(movesList);
            int i = 0;

            randomGame.nextPlayer();
            int[] moves = getPossibleMoves(randomGame.getBoard());
            int randomNum;
            do {
                //randomNum = ThreadLocalRandom.current().nextInt(0,7);
                randomNum = movesList.get(i);
                ++i;
            } while (moves[randomNum] != 1);
            randomGame.placeStone(randomNum);
            winner = randomGame.checkWinner();
        }
        return winner;
    }

    private int[] getPossibleMoves(int[][] board) {
        int[] moves = new int[7];
        for (int x = 0; x < 7; ++x) {
            if (board[x][5] == 0)
                moves[x] = 1;
        }
        return moves;
    }

    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[7][6];
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 6; ++j) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }
}