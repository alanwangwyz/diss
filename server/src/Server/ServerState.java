//Bug-killer
//Lakshmi
//Thursday 10am
package Server;

import java.util.ArrayList;
import java.util.List;
//monitor the client activities, for example if they are online or not
public class ServerState {

    private static ServerState instance;
    private List<ClientConnection> connectedClients;
    private List<ClientConnection> connectedPlayers;
    
    private ServerState() {
        connectedClients = new ArrayList<>();
        connectedPlayers = new ArrayList<>();
    }

    public static synchronized ServerState getInstance() {
        if(instance == null) {
            instance = new ServerState();
        }
        return instance;
    }

    public synchronized void clientConnected(ClientConnection client) {
        connectedClients.add(client);
    }

    public synchronized void clientDisconnected(ClientConnection client) {
        connectedClients.remove(client);
    }

    public synchronized List<ClientConnection> getConnectedClients() {
        return connectedClients;
    }
    
    public synchronized void playerConnected(ClientConnection client) {
    	if(!connectedPlayers.contains(client)) {
    		connectedPlayers.add(client);
    	}
    	
    }

    public synchronized void playerDisconnected(ClientConnection client) {
    	if(connectedPlayers.contains(client)) {
    		connectedPlayers.remove(client);
    	}
    }

    public synchronized List<ClientConnection> getConnectedPlayers() {
        return connectedPlayers;
    }

}
