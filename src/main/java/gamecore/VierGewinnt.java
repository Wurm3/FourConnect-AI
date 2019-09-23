package gamecore;


public class VierGewinnt implements VierGewinntInterface {
    private int player;
    private int[][] board;

    public VierGewinnt(){
        player = 1;
        board = new int[7][6];
    }

    public int[][] getBoard() {
        return board;
    }

    public void setPlayer(int player){
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public void nextPlayer() {
        if(player == 1){
            player = 2;
        }else{
            player = 1;
        }
    }

    public void setBoard(int[][] board){
        this.board = board;
    }

    public int placeStone(int x) {
        if(x < 0 || x > 6)
            throw new IllegalArgumentException("Can not place: " + x);
        for(int i = 0; i < 6;++i){
            if(board[x][i] == 0){
                board[x][i] = player;
                return i;
            }
        }
        return -1;
    }

    public int checkWinner(){
        int winner = checkSurround();
        return winner;
    }

    private int checkSurround(){
        for(int i = 0; i < 6;++i){
            for(int j = 0; j < 7; ++j){
                //vertical check
                if(i < 3)
                    if(board[j][i + 1] == board[j][i] && board[j][i + 2] == board[j][i] && board[j][i + 3] == board[j][i] && board[j][i] != 0)
                        return board[j][i];
                //horizontal check
                if(j < 4) {
                    if (board[j + 1][i] == board[j][i] && board[j + 2][i] == board[j][i] && board[j + 3][i] == board[j][i] && board[j][i] != 0)
                        return board[j][i];
                    //diagonal up
                    if(i < 3) {
                        if (board[j + 1][i + 1] == board[j][i] && board[j + 2][i + 2] == board[j][i] && board[j + 3][i + 3] == board[j][i] && board[j][i] != 0)
                            return board[j][i];
                        //diagonal down
                        if (board[j][i + 3] == board[j + 3][i] && board[j + 1][i + 2] == board[j + 3][i] && board[j + 2][i + 1] == board[j + 3][i] && board[j + 3][i] != 0)
                            return board[j + 3][i];
                    }
                }
            }
        }
        boolean draw = true;
        for(int i = 0; i < 7;++i){
            if(board[i][5] == 0)
                draw = false;
        }
        if(draw)
            return -1;
        return 0;
    }
}
