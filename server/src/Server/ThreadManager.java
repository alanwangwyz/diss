//Bug-killer
//Lakshmi
//Thursday 10am

package Server;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//class to update the GUI for the server if the server receives any new information
public class ThreadManager extends Thread{
    private TextArea processes;
    private ServerSocket listeningSocket;
    private TextArea currentuser;
    private TextField currentuserNo;
    private static int clientNum = 0;

    public ThreadManager(ServerSocket serverSocket, TextArea processes, TextArea currentuser, TextField currentuserNo){
        this.processes = processes;
        this.listeningSocket = serverSocket;
        this.currentuser=currentuser;
        this.currentuserNo=currentuserNo;
    }

    @Override
    public void run() {
        try{
            while (true) {
                //Accept an incoming client connection request
                Socket clientSocket = listeningSocket.accept();
                processes.appendText(Thread.currentThread().getName()
                        + " - Client connection accepted"+"\n");
                clientNum++;

                //Create a client connection to listen for and process all the messages
                //sent by the client
                ClientConnection clientConnection = new ClientConnection(clientSocket, clientNum,processes,currentuser,currentuserNo);
                clientConnection.setName("Thread" + clientNum);
                clientConnection.start();
                
                //Update the server state to reflect the new connected client
                ServerState.getInstance().clientConnected(clientConnection);
                currentuserNo.setText(String.valueOf(ServerState.getInstance().getConnectedClients().size())+" client"+"\n");

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
