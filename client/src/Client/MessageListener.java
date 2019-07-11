//Bug-killer
//Lakshmi
//Thursday 10am
package Client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MessageListener extends Thread{
	private BufferedWriter writer;
	private BufferedReader reader;
	private ClientGUI clientGUI;
	private ArrayList<String> online;
	//MessageListener class is used to receive the message sent from server and perform correspondent
    //operations

	public MessageListener(BufferedReader reader, BufferedWriter writer,  ClientGUI clientGUI) {
		this.reader = reader;
		this.writer = writer;
		this.clientGUI = clientGUI;
	}

	@Override
	public void run() {
		try {
			String line;
			while((line = reader.readLine()) != null) {
				System.out.println("JSON: " + line);
				String command = parseJsonKey(line);
				operation(command, line);
			}
		} catch (SocketException e) {
			showAlert(Alert.AlertType.ERROR, clientGUI.getClientStage().getScene().getWindow(), "Socket Error!", "Socket closed! Please restart it later");//msg.setText("Socket closed! Please restart it later");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void operation(String command, String line) {
		Scanner keyboard = new Scanner(System.in);
		String value = parseJsonValue(line, command);
		try{
			switch (command) {
			case "update_userList":	
				System.out.println("update");
				ArrayList<String> onlineList = parseJsonArrayValue(value);
				clientGUI.setOnline(onlineList);   
				break;
			case "invite_request": //one player sends invitation to the other player
				String username = parseJsonValue(line, command);
				Platform.runLater(new Runnable() {
					@Override public void run() {
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						alert.setTitle("Invitation");
						alert.initOwner(clientGUI.getClientStage());
						alert.setContentText(username+ " invites you to the game!");
						Optional<ButtonType> invitation = alert.showAndWait();
						if (invitation.get() == ButtonType.OK){
							try {
								writer.write(encapsulateJson("accept_invite", username) + "\n");
								writer.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
				break;
			case "player_in_game_List": //player in the game
				String list = parseJsonValue(line, command);
				System.out.println("Player_in_game_list: ");
				ArrayList<String> player_in_game = parseJsonArrayValue(list);
				for(String name: player_in_game) {
					System.out.println(name); 			
				}
				clientGUI.setPlayerInGame(player_in_game);
				break;
			case "openGameUI": //start game
				System.out.println("Open all player's UI");
				String initialMark = String.format("%-6s: %-5s\n", value, "0");
				clientGUI.appendText1(initialMark);
				break;
			case "board": //get the information referring to the character position
				String coordinate_char=parseJsonValue(line, "board");
				System.out.println("Put character on board's UI:"+coordinate_char);
				clientGUI.setText(coordinate_char);
				break;
			case "pass": //pass turn
				String WhoPassVote=parseJsonValue(line, "pass");
				System.out.println(WhoPassVote+" give up the right to vote");
				break;
			case "whoPlay": //get the who's turn information
				String WhoPlay=parseJsonValue(line, "whoPlay");
				clientGUI.setDragCount();
				clientGUI.setText2(WhoPlay);
				currentThread().sleep(1500);
				if (!WhoPlay.equals(clientGUI.getUsername())){
					clientGUI.close();
				}else{
					clientGUI.open();
				}
				System.out.println(WhoPlay+" 's turn to play");
				if (WhoPlay.equals(clientGUI.getUsername())) {
					clientGUI.getPass_submit().setDisable(false);
					clientGUI.getVoteHorizontal().setDisable(true);
					clientGUI.getVoteVertical().setDisable(true);
					clientGUI.getSubmit().setDisable(true);
					clientGUI.getPass_vote().setDisable(true);
					clientGUI.getCancel().setDisable(true);
				} else {
					clientGUI.getVoteHorizontal().setDisable(true);
					clientGUI.getVoteVertical().setDisable(true);	
					clientGUI.getPass_submit().setDisable(true);
					clientGUI.getSubmit().setDisable(true);
					clientGUI.getPass_vote().setDisable(true);
					clientGUI.getCancel().setDisable(true);
				}
				break;
			case "passWithoutPut": //pass without dragging any tiles
				String WhoPassWithoutPut=parseJsonValue(line, "passWithoutPut");
				System.out.println(WhoPassWithoutPut+" pass without put any character!");
				break;
			case "end": //end game message
				Platform.runLater(new Runnable() {
					@Override public void run() {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Game Over"); 
						String gameResult =parseJsonValue(line, "end");
						JSONArray jsonArray = JSONArray.fromObject(gameResult);
						ArrayList<String> markList = new ArrayList<String>();
						String overall_mark = "";
						for (int i = 0; i < jsonArray.size(); i++) {
							markList.add(jsonArray.getString(i));
							String[] end_mark = jsonArray.getString(i).split(",");
							overall_mark += String.format("%-6s: %-5s\n", end_mark[0], end_mark[1]);
						}
						alert.setContentText(overall_mark);
						alert.initOwner(clientGUI.getClientStage());
						Optional<ButtonType> invitation = alert.showAndWait();
						if (invitation.get() == ButtonType.OK){

						}
						clientGUI.getPass_submit().setDisable(true);
						System.out.println("the game ends, the result is :\n");
						System.out.println(gameResult);
					}
				});
				break;
			case "openVoteUI": //vote character message
				String voteWhatWord =parseJsonValue(line, "openVoteUI");
				System.out.println("open all player's Vote UI");
				System.out.println("show vote Word: " + voteWhatWord);
				
				String[] wordIndex = voteWhatWord.split(",");
				int x1 = Integer.parseInt(wordIndex[0]);
				int y1 = Integer.parseInt(wordIndex[1]);
				int x2 = Integer.parseInt(wordIndex[2]);
				int y2 = Integer.parseInt(wordIndex[3]);
				clientGUI.highlightWord(x1,y1,x2,y2);
				String word = clientGUI.getWord(x1,y1,x2,y2);
				Platform.runLater(new Runnable() {
					@Override public void run() {           
						ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
						ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Word: " + 
								word + " \nVote: yes or no?", yes, no);
						alert.initOwner(clientGUI.getClientStage());
						alert.setTitle("Vote");
						Optional<ButtonType> vote = alert.showAndWait();
						if (vote.orElse(no) == yes) {
							try {
								writer.write(encapsulateJson("voteResult", "approve") + "\n");
								writer.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else if (vote.get() == ButtonType.CLOSE ) {
							try {
								writer.write(encapsulateJson("voteResult", "disapprove") + "\n");
								writer.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							try {
								writer.write(encapsulateJson("voteResult", "disapprove") + "\n");
								writer.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
				break;
			case "voteResult": //vote yes or no to get the mark
				String getMark_or_voteFalse = parseJsonValue(line, "voteResult");
				System.out.println("show vote Result: " + getMark_or_voteFalse);
				String[] mark = getMark_or_voteFalse.split(",");
				String text = String.format("%-6s: %-5s", mark[0],mark[1]);
				clientGUI.setText1(text,mark[0]);
				clientGUI.recoverTileColor();
				break;
			case "acceptInvite": //accept the invitation from others
				clientGUI.getGame().setDisable(false);
				break;
			case "cannot_invite": //the player is in the game and can't be invited
				Platform.runLater(new Runnable() {
					@Override public void run() {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("In Game");
						alert.initOwner(clientGUI.getClientStage());
						alert.setContentText("There is a game! Cannot Invite");
						Optional<ButtonType> invitation = alert.showAndWait();
						if (invitation.get() == ButtonType.OK){
						}
					}
					});
					break;
			default:
				break;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


	//encapsulate JSON
	private String encapsulateJson(String key, String value) {
		Map<String, String> json = new HashMap<>();
		json.put(key, value);
		JSONObject object = JSONObject.fromObject(json);
		return object.toString();
	}

    //parse JSON value method
	private String parseJsonValue(String json, String key) {
		String message = json.replaceAll("\n", "");
		JSONObject jsonObject = JSONObject.fromObject(message);
		Map object = (Map) jsonObject;
		return (String)object.get(key);
	}

	//parse JSON key method
	private String parseJsonKey(String jsonStr) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map object = (Map) jsonObject;
		if(object.containsKey("update_userList"))
			return "update_userList";
		if(object.containsKey("player_in_game_List"))
			return "player_in_game_List";
		else if(object.containsKey("invite_request"))
			return "invite_request";
		else if(object.containsKey("openGameUI"))
			return "openGameUI";
		if (object.containsKey("board"))
			return "board";
		if (object.containsKey("whoPlay"))
			return "whoPlay";
		if (object.containsKey("whoVote"))
			return "whoVote";
		if (object.containsKey("pass"))
			return "pass";
		if (object.containsKey("passWithoutPut"))
			return "passWithoutPut";
		if (object.containsKey("end"))
			return "end";
		if (object.containsKey("openVoteUI"))
			return "openVoteUI";
		if (object.containsKey("voteResult"))
			return "voteResult";
		if (object.containsKey("acceptInvite"))
			return "acceptInvite";
		if (object.containsKey("cannot_invite"))
			return "cannot_invite";
		return null;
	}

	private ArrayList<String> parseJsonArrayValue(String json) {
		JSONArray jsonArray = JSONArray.fromObject(json);
		ArrayList<String> userList = new ArrayList<String>();

		for (int i = 0; i < jsonArray.size(); i++) {
			userList.add(jsonArray.getString(i));
		}

		this.online = new ArrayList<>();
		for (int i = 0; i < userList.size(); i++) {
			int j = i+1;
			online.add(userList.get(i));
		}
		return online;
	}

	private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.initOwner(owner);
		alert.show();
	}

}