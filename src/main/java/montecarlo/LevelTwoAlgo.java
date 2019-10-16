package montecarlo;

import gamecore.VierGewinntInterface;

import java.util.Collections;

public class LevelTwoAlgo extends LevelOneAlgo implements ArtificialIntelligenceInterface {

    public LevelTwoAlgo(VierGewinntInterface vierGewinnt) {
        super(vierGewinnt);
    }

    @Override
    public int[] makeNextMove() {
        int player = vierGewinnt.getPlayer();
        int[][] board = vierGewinnt.getBoard();

        int otherPlayer = player == 1 ? 2 : 1;

        nextMove = -1;

        checkHorizontal(board,player);
        if(nextMove == -1){
            nextMove = checkHorizontal(board, otherPlayer);
        }
        checkVertical(board, player);
        if (nextMove == -1) {
            nextMove = checkVertical(board, otherPlayer);
        }

        int y;
        if (nextMove != -1) {
            y = vierGewinnt.placeStone(nextMove);
        } else {
            if(possibleMoves.size() < 7){
                reshuffle();
            }
            nextMove = possibleMoves.get(0);
            y = makeRandomNextMove();
        }

        int[] xy = {nextMove, y};
        return xy;
    }

    protected int checkHorizontal(int[][] board, int player) {
        int nextMove = -1;
        for (int y = 0; y < 6; ++y) {
            for (int x = 0; x < 4; ++x) {
                if(board[x][y] == 0 && board[x + 1][y] == player && board[x + 2][y] == player && board[x + 3][y] == player){
                    nextMove = x;
                    break;
                }
                if (board[x][y] == player && board[x + 1][y] == player && board[x + 2][y] == player && board[x + 3][y] == 0) {
                    nextMove = x + 3;
                    break;
                }
            }
        }
        return nextMove;
    }

    protected int makeRandomNextMove(){
        reshuffle();
        return super.makeRandomNextMove();
    }
}
