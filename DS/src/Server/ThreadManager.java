//Yizhou Wang
//669026
//DS project1
package Server;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadManager extends Thread{
    private TextArea processes;
    private ServerSocket listeningSocket;
    private TextArea currentuser;

    public ThreadManager(ServerSocket serverSocket, TextArea processes, TextArea currentuser){
        this.processes = processes;
        this.listeningSocket = serverSocket;
        this.currentuser=currentuser;
    }

    @Override
    public void run() {
        int clientNum = 0;
        try{
            while (true) {
                //Accept an incoming client connection request
                Socket clientSocket = listeningSocket.accept();
                processes.appendText(Thread.currentThread().getName()
                        + " - Client connection accepted"+"\n");
                clientNum++;

                //Create a client connection to listen for and process all the messages
                //sent by the client
                ClientConnection clientConnection = new ClientConnection(clientSocket, clientNum,processes,currentuser);
                clientConnection.setName("Thread" + clientNum);
                clientConnection.start();

                //Update the server state to reflect the new connected client
                ServerState.getInstance().clientConnected(clientConnection);
                currentuser.setText(String.valueOf(ServerState.getInstance().getConnectedClients().size())+" client"+"\n");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if ( listeningSocket != null ) {
                try {
                    listeningSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
