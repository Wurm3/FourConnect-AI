package gui;

import gamecore.VierGewinnt;
import gamecore.VierGewinntCore;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import montecarlo.ArtificialIntelligenceInterface;
import montecarlo.MonteCarlo;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static final double height = 500;
    private VierGewinntCore vierGewinnt;
    private List<List<Rectangle>> rectangleBoard = new ArrayList();
    private Stage primaryStage;
    private ArtificialIntelligenceInterface artificialPlayer;
    private int[][] oldBoard;

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) {
        vierGewinnt = new VierGewinnt();

        //Artificial Player
        artificialPlayer = new MonteCarlo(vierGewinnt);

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        primaryStage.setTitle("VierGewinnt");
        primaryStage.setWidth(560 + 90);
        primaryStage.setHeight(height);

        addButtons(root);

        for(int i = 0; i < 7; ++i){
            rectangleBoard.add(new ArrayList<Rectangle>());
            for(int j = 0; j < 6; ++j){
                Rectangle rec = new Rectangle(80*i,height-20-80-80*j,80,80);
                rec.setStroke(Color.BLACK);
                rec.setStrokeWidth(3);
                rec.setFill(Color.VIOLET);

                rectangleBoard.get(i).add(rec);
                root.getChildren().add(rec);
            }
        }
        theScene.addEventFilter(KeyEvent.KEY_PRESSED,keyEvent -> keyPressed(keyEvent));
        this.primaryStage = primaryStage;
        primaryStage.show();
    }

    private void addButtons(Group root){
        Pane nextMovePane = new Pane();
        nextMovePane.setLayoutX(563);
        nextMovePane.setLayoutY(0);

        Pane backPane = new Pane();
        backPane.setLayoutX(563);
        backPane.setLayoutY(30);

        Button next_move = new Button("Next Move");
        Button back = new Button("Back");

        next_move.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nextMove();
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                oneMoveBack();
            }
        });
        nextMovePane.getChildren().add(next_move);
        backPane.getChildren().add(back);

        root.getChildren().addAll(nextMovePane, backPane);
    }

    public int otherPlayer(int player){
        return player == 1? 2 : 1;
    }

    private void oneMoveBack(){
        int player = otherPlayer(vierGewinnt.getPlayer());

        vierGewinnt = new VierGewinnt();
        vierGewinnt.setBoard(oldBoard);
        vierGewinnt.setPlayer(player);
        colorCurrentBoard(vierGewinnt.getBoard());
    }

    private void colorCurrentBoard(int[][] board){
        for(int i = 0; i < 7; ++i){
            for(int j = 0; j < 6; ++j){
                changeFieldWithPlayer(i,j,board[i][j]);
            }
        }
        primaryStage.show();
    }

    private void changeFieldWithPlayer(int x, int y, int player){
        rectangleBoard.get(x).get(y).setFill(getPlayerColor(player));
    }

    private void nextMove(){
        oldBoard = copyBoard(vierGewinnt.getBoard());
        int[] xy = artificialPlayer.makeNextMove();
        changeField(xy[0],xy[1]);
    }

    public void changeField(int x, int y){
        if(x != -1 && y != -1){
            rectangleBoard.get(x).get(y).setFill(getPlayerColor(vierGewinnt.getPlayer()));
            displayWinner(vierGewinnt.checkWinner());
            vierGewinnt.nextPlayer();
            primaryStage.show();
        }
    }

    private void displayWinner(int x){
        if(x != 0){
            setAllRectangles(getPlayerColor(x));
        }
    }

    private Color getPlayerColor(int player){
        if(player == 1)
            return Color.RED;
        else if(player == 2)
            return Color.YELLOW;
        return Color.VIOLET;
    }

    private void setAllRectangles(Color color){
        for(List<Rectangle> rectangles: rectangleBoard){
            for(Rectangle rec: rectangles){
                rec.setFill(color);
            }
        }
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

    private void keyPressed(KeyEvent e){
        int y = -1;
        int x = -1;
        switch(e.getCode()){
            case DIGIT1:
                oldBoard = copyBoard(vierGewinnt.getBoard());
                y = vierGewinnt.placeStone(0);
                x = 0;
                changeField(x,y);
                break;
            case DIGIT2:
                oldBoard = copyBoard(vierGewinnt.getBoard());
                y = vierGewinnt.placeStone(1);
                x = 1;
                changeField(x,y);
                break;
            case DIGIT3:
                oldBoard = copyBoard(vierGewinnt.getBoard());
                y = vierGewinnt.placeStone(2);
                x = 2;
                changeField(x,y);
                break;
            case DIGIT4:
                oldBoard = copyBoard(vierGewinnt.getBoard());
                y = vierGewinnt.placeStone(3);
                x = 3;
                changeField(x,y);
                break;
            case DIGIT5:
                oldBoard = copyBoard(vierGewinnt.getBoard());
                y = vierGewinnt.placeStone(4);
                x = 4;
                changeField(x,y);
                break;
            case DIGIT6:
                oldBoard = copyBoard(vierGewinnt.getBoard());
                y = vierGewinnt.placeStone(5);
                x = 5;
                changeField(x,y);
                break;
            case DIGIT7:
                oldBoard = copyBoard(vierGewinnt.getBoard());
                y = vierGewinnt.placeStone(6);
                x = 6;
                changeField(x,y);
                break;
            case R:
                vierGewinnt = new VierGewinnt();
                artificialPlayer.refreshBoard(vierGewinnt);
                setAllRectangles(Color.VIOLET);
                break;
            default:
                break;
        }
    }
}
