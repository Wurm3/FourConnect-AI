package montecarlo;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final float winningWeight = 0.8f;
    private final float loosingWeight = 1;
    private final float drawWeight = 0.4f;

    private int index;
    private int[][] boardState;
    private List<Node> children;
    private Node parent;
    private int winnings;
    private int loosings;
    private int draws;
    private int player;


    public Node(int index,int player,int[][] boardState){
        this.index = index;
        this.boardState = boardState;
        children = new ArrayList<>();
        this.player = player;

        winnings = 0;
        loosings = 0;
        draws = 0;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public Node getParent(){return parent;}
    public List<Node> getChildren(){return children;}

    public void addChild(Node child){
        children.add(child);
    }

    public int getIndex(){
        return index;
    }

    public int getPlayer(){return player;}

    public int[][] getBoard(){return boardState;}

    public void addWinnings(int winnings){this.winnings += winnings;}
    public void addLoosings(int loosings){this.loosings += loosings;}
    public void addDraws(int draws){this.draws += draws;}

    public int getWinnings(){return winnings;}
    public int getLoosings(){return loosings;}
    public int getDraws(){return draws;}

    public void setResults(int winnings, int loosings, int draws){
        this.winnings = winnings;
        this.loosings = loosings;
        this.draws = draws;
    }

    public double getValue(){
        if(loosings + draws == 0){
            return winnings * winningWeight * 10;
        }
        return (winningWeight * winnings) / ((loosingWeight * loosings) + (drawWeight * draws));
    }

    public int getPlayed(){
        return winnings + loosings + draws;
    }

    public void updateResults(int result){
        switch(result){
            case 1:
                if(player == 1)
                    ++winnings;
                else
                    ++loosings;
                break;
            case 2:
                if(player == 2)
                    ++winnings;
                else
                    ++loosings;
                break;
            case -1:
                ++draws;
                break;
        }
    }

    @Override
    public boolean equals(Object o){
        if(this.getClass() != o.getClass())
            return false;
        Node other = (Node) o;
        if(!this.getBoard().equals(other.getBoard()))
            return false;
        return true;
    }


}
