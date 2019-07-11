//Bug-killer
//Lakshmi
//Thursday 10am

package Server;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//message receive and display in the server panel
public class ClientConnection extends Thread {
	private String username;
	private static String userListStr = "";
	private Socket clientSocket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private int clientNum;
	private TextArea process;
	private TextArea currentuser;
	private TextField currentuserNo;
	private boolean inGame = false;

	public ClientConnection(Socket clientSocket, int clientNum, TextArea process, TextArea currentusers, TextField currentuserNo) {
		try {
			this.clientSocket = clientSocket;
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
			this.clientNum = clientNum;
			this.process = process;
			this.currentuser = currentusers;
			this.currentuserNo = currentuserNo;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String parseJsonValue(String json, String key) {
		String message = json.replaceAll("\n", "");
		JSONObject jsonObject = JSONObject.fromObject(message);
		Map object = (Map) jsonObject;
		return (String)object.get(key);
	}

	private String parseJsonKey(String jsonStr) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map object = (Map) jsonObject;
		if(object.containsKey("username"))
			return "username";
		else if (object.containsKey("startgame"))
			return "startgame";
		else if (object.containsKey("invite"))
			return "invite";
		else if (object.containsKey("inviteAll"))
			return "inviteAll";
		else if (object.containsKey("accept_invite"))
			return "accept_invite";  	
		else if(object.containsKey("exit"))
			return "exit";
		else if (object.containsKey("put"))
			return "put";
		else if (object.containsKey("pass"))
			return "pass";
		else if (object.containsKey("passWithoutPut"))
			return "passWithoutPut";
		else if (object.containsKey("vote"))
			return "vote";
		else if (object.containsKey("voteResult"))
			return "voteResult";
		else if (object.containsKey("endGame"))
			return "endGame";
		else if (object.containsKey("login"))
			return "login";
		else if (object.containsKey("CONNECTED or NOT"))
			return "CONNECTED or NOT";
		return null;
	}

	private void printUsers() {
		if(ServerState.getInstance().getConnectedClients().size() > 0) {
			userListStr = "";
			for(ClientConnection user : ServerState.getInstance().getConnectedClients())
				userListStr += (user.getUsername() + "\n");
		}else {
			userListStr = "";
		}
		currentuser.setText(userListStr);
	}

	private String encapsulateJson(String key, String value) {
		Map<String, String> json = new HashMap<>();
		json.put(key, value);
		JSONObject object = JSONObject.fromObject(json);
		return object.toString();
	}

	private String encapsulateJsonArray_String(ArrayList<String> arrayList) {
		return JSONArray.fromObject(arrayList).toString();
	}

	private String encapsulateJsonArray_Connection (List<ClientConnection> list) {
		ArrayList<String> usernames = new ArrayList<String>();
		for(ClientConnection client: list) {
			usernames.add(client.getUsername());
		}   		
		return JSONArray.fromObject(usernames).toString();
	}

	private void broadcast(String key, String value) {
		for(ClientConnection client: ServerState.getInstance().getConnectedClients()) {
			String broadcast = encapsulateJson(key,value);
			try {
				client.getWriter().write(broadcast+"\n");
				client.getWriter().flush();
				System.out.println(client.getWriter() + " " + broadcast);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void broadcast(String key, String value, List<ClientConnection> list) {
		for(ClientConnection client: list) {
			String broadcast = encapsulateJson(key,value);
			try {
				client.getWriter().write(broadcast+"\n");
				client.getWriter().flush();
				System.out.println(client.getWriter() + " " + broadcast);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void broadcastPlayer(String key, String value) {
		broadcast(key, value, ServerState.getInstance().getConnectedPlayers());
	}

	@Override
	public void run() {
		try {
			process.appendText(Thread.currentThread().getName()
					+ " - Reading messages from client's " + clientNum + " connection"+"\n");

			String clientMsg;
			while ((clientMsg = reader.readLine()) != null) {
				String key = parseJsonKey(clientMsg);
				clientMsg = parseJsonValue(clientMsg, key);
				if(!key.equals("CONNECTED or NOT")){
					System.out.println(key + " " + clientMsg) ;
					if(key.equals("login")) {
						String msg = "true";
						for(ClientConnection client: ServerState.getInstance().getConnectedClients()) {
							if((client.getClientNum() != (this.clientNum)
									&& client.getUsername().equals(clientMsg))) {
								msg = "false";
							}
						}
						System.out.println(msg);
						this.getWriter().write(msg + "\n");
						this.getWriter().flush();
					}
					if(key.equals("username")){
						this.username = clientMsg;
						printUsers();
						broadcastList();
					}
					else if ((key.equals("invite")|| key.equals("inviteAll")||key.equals("accept_invite")) && GameSystem.inGame) {
						String json = encapsulateJson("cannot_invite","").toString()+"\n";
						this.getWriter().write(json);
						this.getWriter().flush();
					}
					else if(key.equals("invite")) {
						String json = encapsulateJson("acceptInvite","").toString()+"\n";
						this.getWriter().write(json);
						this.getWriter().flush();
						if(!ServerState.getInstance().getConnectedPlayers().contains(this))
							ServerState.getInstance().playerConnected(this);
						for(ClientConnection client: ServerState.getInstance().getConnectedClients()) {
							if(client.getUsername().equals(clientMsg)
									&& !client.getUsername().equals(this.username)) {
								json = encapsulateJson("invite_request",this.username).toString()+"\n";
								client.getWriter().write(json);
								client.getWriter().flush();
							}
						}
						broadcastList();
					}
					else if(key.equals("inviteAll")) {
						String json = encapsulateJson("acceptInvite","").toString()+"\n";
						this.getWriter().write(json);
						this.getWriter().flush();
						if(!ServerState.getInstance().getConnectedPlayers().contains(this))
							ServerState.getInstance().playerConnected(this);
						for(ClientConnection client: ServerState.getInstance().getConnectedClients()) {
							if(!ServerState.getInstance().getConnectedPlayers().contains(client)
									&& !client.getUsername().equals(this.username)) {
								json = encapsulateJson("invite_request",this.username).toString()+"\n";
								client.getWriter().write(json);
								client.getWriter().flush();
							}
						}
						broadcastList();
					}
					else if(key.equals("accept_invite")) {
						ServerState.getInstance().playerConnected(this);
						String json = encapsulateJson("acceptInvite","").toString()+"\n";
						this.getWriter().write(json);
						this.getWriter().flush();
						broadcastList();
					}
					else if (!(clientMsg.equals("CONNECTED or NOT")||clientMsg.equals("exit")))
					{
						process.appendText(Thread.currentThread().getName()
								+ " - Message from client " + clientNum + " received: " + clientMsg+"\n");
					}
					else if(clientMsg.equals("exit"))
					{
						process.appendText(Thread.currentThread().getName()
								+ " - Message from client " + clientNum + " received: " + clientMsg+"\n");
						ServerState.getInstance().playerDisconnected(this);
						ServerState.getInstance().clientDisconnected(this);
						System.out.println(ServerState.getInstance().getConnectedPlayers());
						System.out.println(ServerState.getInstance().getConnectedClients());
						broadcastList();
						printUsers();
						break;
					}
					if (key.equals("startgame") && ServerState.getInstance().getConnectedPlayers().contains(this)) {
						if (GameSystem.getPlayerReady() == 0) {
							GameSystem.initialize();
						} else {
							int playerReady = GameSystem.getPlayerReady();
							GameSystem.setPlayerReady(playerReady+1);
						}
						broadcastPlayer("openGameUI",this.username);

						if(GameSystem.getPlayerReady() == ServerState.getInstance().getConnectedPlayers().size()) {
							GameSystem.inGame = true;
							broadcastPlayer("whoPlay", (GameSystem.whoPlay));
							System.out.println(GameSystem.getMap());
							GameSystem.setPlayerReady(0);
						}
					}
					else if (key.equals("put") && (username.equals(GameSystem.whoPlay))) {
						GameSystem.putCharacter(clientMsg);
						System.out.println("put success");
						broadcastPlayer("board", clientMsg);// after "put", player need to "pass" in order to finish his step.
						//send a String "c,x,y"
					} else if (key.equals("pass") && (username.equals(GameSystem.whoPlay))) {
						broadcastPlayer("pass", (GameSystem.whoPlay)); //return String whoPlay
						GameSystem.pass();
						broadcastPlayer("whoPlay", (GameSystem.whoPlay));
					} else if (key.equals("passWithoutPut") && (username.equals(GameSystem.whoPlay))) {
						//broadcastPlayer("passWithoutPut", (GameSystem.whoPlay));
						GameSystem.passWithoutPut();
						System.out.println(GameSystem.getMap());//
						if (GameSystem.passComplete) {
							endGame();
							//need to end game in client side
						} else {
							broadcastPlayer("whoPlay", (GameSystem.whoPlay));
						}
					} else if (key.equals("vote") && (username.equals(GameSystem.whoPlay))) {
						broadcastPlayer("openVoteUI", clientMsg); //receive 2 index: int[4]
						GameSystem.vote(clientMsg);
					} else if (key.equals("voteResult")) {
						// after player vote, player will send "approve" or "disapprove" in ClientMsg
						GameSystem.voteResult(clientMsg);
						if (GameSystem.voteComplete) {
							broadcastPlayer("voteResult", GameSystem.voteComplete());
							GameSystem.pass();//duplicate code
							broadcastPlayer("whoPlay", (GameSystem.whoPlay));
						}
					} else if(key.equals("endGame")) {
						if (ServerState.getInstance().getConnectedPlayers().contains(this) && ServerState.getInstance().getConnectedPlayers().size()!=0 &&GameSystem.inGame) {
							endGame();
						}
						broadcastList();
					}
				}

			}
			clientSocket.close();
			ServerState.getInstance().clientDisconnected(this);
			currentuserNo.setText(String.valueOf(ServerState.getInstance().getConnectedClients().size())+" client"+"\n");
			process.appendText(Thread.currentThread().getName()
					+ " - Client " + clientNum + " disconnected"+"\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void endGame() {
		ArrayList<String> result = new ArrayList<String>();
		result = GameSystem.getFinalScore();
		System.out.println("result" + result);
		String finalScore = encapsulateJsonArray_String(result);
		System.out.println("finalScore" + finalScore);
		broadcastPlayer("end", finalScore);
		ServerState.getInstance().getConnectedPlayers().clear();
		GameSystem.inGame = false;
		System.out.println(ServerState.getInstance().getConnectedPlayers());
	}
	
	private void broadcastList() {
		ArrayList<String> userList = new ArrayList<String>();
		for (ClientConnection c: ServerState.getInstance().getConnectedClients()) {
			if (!ServerState.getInstance().getConnectedPlayers().contains(c)) {
				userList.add(c.getUsername());
			}
		}
		String broadcast = encapsulateJsonArray_String(userList);
		broadcast("update_userList", broadcast);
		broadcast = encapsulateJsonArray_Connection(ServerState.getInstance().getConnectedPlayers());
		broadcast("player_in_game_List", broadcast, ServerState.getInstance().getConnectedClients());
	}

	public synchronized void write(String msg, TextArea process) {
		try {
			writer.write(msg + "\n");
			writer.flush();
			process.appendText(Thread.currentThread().getName() + " - Message sent to client " + clientNum+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object o) {
		ClientConnection c = (ClientConnection)o;
		return this.clientNum == c.getClientNum();
	}


	//setter and getter
	public int getClientNum() {
		return clientNum;
	}
	
	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setInGame(boolean x) {
		this.inGame = x;
	}

	public boolean getInGame() {
		return inGame;
	}

	public String getUsername() {
		return username;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	private BufferedWriter getWriter() {
		return writer;
	}

	public void setWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
}