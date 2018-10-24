package gamecore;

public interface VierGewinntCore
{
    int getPlayer();
    void nextPlayer();
    int placeStone(int x);
    int checkWinner();
    int[][] getBoard();
    void setBoard(int[][] board);
    void setPlayer(int player);
}
