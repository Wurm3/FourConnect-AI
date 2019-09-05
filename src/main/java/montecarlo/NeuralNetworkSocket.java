package montecarlo;

import gamecore.VierGewinntInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Deprecated
public class NeuralNetworkSocket extends Thread implements ArtificialIntelligenceInterface {
    private VierGewinntInterface vierGewinnt;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private int port = 1025;

    public NeuralNetworkSocket(VierGewinntInterface vierGewinnt) throws IOException{
        this.vierGewinnt = vierGewinnt;
        serverSocket = new ServerSocket(port);

        while(true){
            clientSocket = serverSocket.accept();
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(input.readLine());
            System.out.println("check");
        }
    }


    @Override
    public int[] makeNextMove() {

        return new int[0];
    }

    @Override
    public void refreshBoard(VierGewinntInterface vierGewinnt) {
        this.vierGewinnt = vierGewinnt;
    }
}
