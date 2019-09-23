package montecarlo;

import java.util.Collections;
import java.util.List;

import gamecore.VierGewinntInterface;

import java.util.ArrayList;

public class LevelOneAlgo implements ArtificialIntelligenceInterface {
    protected VierGewinntInterface vierGewinnt;
    protected List<Integer> possibleMoves;
    protected int nextMove;

    public LevelOneAlgo(VierGewinntInterface vierGewinnt) {
        this.vierGewinnt = vierGewinnt;

        possibleMoves = new ArrayList<>();

        for (int i = 0; i < 7; ++i) {
            possibleMoves.add(i);
        }

        Collections.shuffle(possibleMoves);
    }


    @Override
    public int[] makeNextMove() {
        int player = vierGewinnt.getPlayer();
        int[][] board = vierGewinnt.getBoard();

        int otherPlayer = player == 1 ? 2 : 1;

        nextMove = -1;
        checkVertical(board, player);
        if (nextMove == -1) {
            nextMove = checkVertical(board, otherPlayer);
        }

        int y;
        if (nextMove != -1) {
            y = vierGewinnt.placeStone(nextMove);
        } else {
            nextMove = possibleMoves.get(0);
            y = makeRandomNextMove();
        }

        int[] xy = {nextMove, y};
        return xy;

    }

    protected int makeRandomNextMove() {
        int y = vierGewinnt.placeStone(nextMove);
        if (y == -1) {
            for (int i : possibleMoves) {
                y = vierGewinnt.placeStone(i);
                if (y != -1) {
                    nextMove = i;
                    return y;
                }
            }
        }
        return y;
    }


    protected int checkVertical(int[][] board, int player) {
        int nextMove = -1;
        for (int x = 0; x < 7; ++x) {
            for (int y = 0; y < 3; ++y) {
                if (board[x][y] == player && board[x][y + 1] == player && board[x][y + 2] == player && board[x][y + 3] == 0) {
                    nextMove = x;
                    break;
                }
            }
        }
        return nextMove;
    }

    @Override
    public void refreshBoard(VierGewinntInterface vierGewinnt) {
        this.vierGewinnt = vierGewinnt;
    }

    public void reshuffle(){
        possibleMoves = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            possibleMoves.add(i);
        }
        Collections.shuffle(possibleMoves);
    }
}
