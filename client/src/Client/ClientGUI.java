//Bug-killer
//Lakshmi
//Thursday 10am

package Client;
import java.io.*;
import java.net.Socket;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javafx.beans.value.*;

//clientGUI class implements the GUI component for the client side
public class ClientGUI{	
	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;
	private String username;
	private ArrayList<String> online = new ArrayList<String>();
	private Button Game = new Button("Start Game");
	private Button Invite = new Button("Invite All");
	private static Stage clientstage;
	private TextArea gameplayer = new TextArea();
	private ListView<String> onlineplayer;
	private int length;
	private int width;
	private Label[][] Tile;
	private ArrayList<String> location=new ArrayList<>();
	private int row;
	private int column;
	private Set<String> unique=new HashSet<>();
	private Text text1 = new Text("");
	private ArrayList <String> submit_list = new ArrayList<>();
	private Button vote = new Button("       Vote      ");
	private Button pass_vote = new Button("    Pass Vote    ");
	private Button submit = new Button("       Submit      ");
	private Button cancel = new Button("       Cancel      ");
	private Button pass_submit = new Button("  Pass Submit  ");
	private Button end_game = new Button("    End Game   ");
	private Button vote_horizontal = new Button("Vote Horizontal");
	private Button vote_vertical = new Button("  Vote Vertical  ");
	private static int count = 0;
	private Text text2 = new Text("");
	private String turn = "";
	private static char newChar;
	private static int r;
	private static int c;
	private int alph_length;
	private BorderPane Grid;
	private Label [] Word;
	private static int dragCount=0;

	
	public ClientGUI(Socket socket,String username,BufferedReader reader, BufferedWriter writer)
	{
		this.socket=socket;
		this.username=username;
		this.reader = reader;
		this.writer = writer;
		online.add(username);
		onlineplayer = new ListView<>(FXCollections.observableList(online));
		clientstage = new Stage();
		clientstage.setAlwaysOnTop(true);
	}

	//create the scene1 which is the game lobby
	public Scene createScene1() {
		BorderPane GP = new BorderPane();
		BorderPane panel_1 = new BorderPane();
		GP.setTop(panel_1);
		
		Label headerLabel = new Label("Welcome! " + username);
		headerLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		GridPane.setMargin(headerLabel, new Insets(0,40,20,40));
		
		panel_1.setCenter(headerLabel);
		GP.setMargin(panel_1, new Insets(30,20,10,20));
		
		BorderPane panel_2 = new BorderPane();
		GP.setCenter(panel_2);
		GP.setMargin(panel_2, new Insets(0,20,10,20));
		
		BorderPane panel_3 = new BorderPane();
		GP.setBottom(panel_3);
		GP.setMargin(panel_3, new Insets(0,10,20,10));
		
		Label lblMessage = new Label("Message: ");
		panel_3.setCenter(lblMessage);
		
		BorderPane panel_4 = new BorderPane();
		panel_2.setCenter(panel_4);
		panel_2.setMargin(panel_4, new Insets(10,10,0,10));
		
		BorderPane panel_5 = new BorderPane();
		panel_4.setLeft(panel_5);
		panel_4.setMargin(panel_5, new Insets(10,10,10,10));
		
		Label onlinePlayer = new Label("Online Player");
		onlinePlayer.setFont(Font.font("Arial", 25));
		onlinePlayer.setAlignment(Pos.CENTER);
		onlinePlayer.setTextFill(Color.web("#42B97C"));
		panel_5.setTop(onlinePlayer);
		
		BorderPane panel_8 = new BorderPane();
		panel_5.setBottom(panel_8);
		panel_5.setMargin(panel_8, new Insets(10,10,0,10));
		panel_5.setMargin(onlinePlayer, new Insets(0,0,5,0));
		
		panel_8.setCenter(Invite);
		
		BorderPane panel_9 = new BorderPane();
		panel_5.setCenter(panel_9);
		
		BorderPane panel_6 = new BorderPane();
		panel_4.setCenter(panel_6);
		panel_4.setMargin(panel_6, new Insets(10,10,10,10));
		
		Label gamePlayer = new Label("Player in game");
		gamePlayer.setFont(Font.font("Arial", 25));
		gamePlayer.setAlignment(Pos.CENTER);
		gamePlayer.setTextFill(Color.web("#F47F42"));

		panel_6.setTop(gamePlayer);
		
		BorderPane panel_7 = new BorderPane();
		panel_6.setBottom(panel_7);
		panel_6.setMargin(panel_7, new Insets(10,10,0,10));
		panel_6.setMargin(gamePlayer, new Insets(0,0,5,0));
		
		panel_7.setCenter(Game);
		
		BorderPane panel_10 = new BorderPane();
		panel_6.setCenter(panel_10);	
		
		Game.setDisable(true);
		
		gameplayer.setWrapText(true);
		panel_10.setCenter(gameplayer);

		onlineplayer.setCellFactory(TextFieldListCell.forListView());
		onlineplayer.setEditable(false);
		panel_9.setCenter(onlineplayer);
		
		clientstage.setFullScreen(false); 
		Scene clientscene = new Scene(GP,700,500);
		return clientscene;
	}
	
	//show the stage of game lobby GUI
	public void clientGUI() {
		try
		{
			Map<String, String> json = new HashMap<>();
			json.put("username", username);
			JSONObject object = JSONObject.fromObject(json);
			writer.write(object.toString()+"\n");
			writer.flush();

			clientstage.setTitle("Scrabble Game Lobby");
			
			Scene scene1 = createScene1();
			clientstage.setScene(scene1);
			clientstage.show();

			MessageListener ml = new MessageListener(reader,writer,this);
			ml.start();

			check check = new check(socket,writer,clientstage);
			check.start();

			//button method
			Game.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
	        			try {
	        				Map<String, String> json1 = new HashMap<>();
	        				json1.put("startgame", "");
	        				JSONObject object1 = JSONObject.fromObject(json1);
	        				writer.write(object1.toString() + "\n");
	        				writer.flush();
	        				Scene scene2 = createScene2();
	        				clientstage.setScene(scene2);
	        				clientstage.setTitle("Scrabble Game");
	        			} catch(IOException e){
	        				showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
	        			}catch(Exception e){
	        				e.printStackTrace();
	        			}
                }});
		
            end_game.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {               		
	        			Scene scene1 = createScene1();
	        			clientstage.setScene(scene1);
	        			clientstage.setTitle("Scrabble Game Lobby");
	        			setText1("","");
	        			clearTile();
	        			location.clear();
	        			//unique.clear();
	        			submit_list.clear();
	        			Map<String, String> json1 = new HashMap<>();
        				json1.put("endGame", "");
        				JSONObject object1 = JSONObject.fromObject(json1);      				
        				try {
        					writer.write(object1.toString() + "\n");
							writer.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        			
                }});
            
			Invite.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try{
						Map<String, String> json1 = new HashMap<>();
						json1.put("inviteAll", "");
						JSONObject object1 = JSONObject.fromObject(json1);
						writer.write(object1.toString() + "\n");
						writer.flush();
					}catch(IOException e){
						showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
					}catch(Exception e){
						e.printStackTrace();
					}

				}});

			onlineplayer.getSelectionModel().selectedItemProperty().addListener(
					(ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
						try{
							Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
							alert.initOwner(getClientStage());
							alert.setTitle("Confirmation");
							if(oldValue != null) {
								System.out.println("o : " + oldValue);
							}else {
								System.out.println("o : null");
							}
							System.out.println("n : " + newValue);
							count++;
							if(count == 1 && newValue!=null) {
								alert.setContentText("Do you want to invite " + newValue + " ?");
								Optional<ButtonType> result = alert.showAndWait();
								if (result.get() == ButtonType.OK){
									Map<String, String> json1 = new HashMap<>();
									json1.put("invite", newValue);
									JSONObject object1 = JSONObject.fromObject(json1);	
									System.out.println(object1.toString());
									writer.write(object1.toString()+"\n");
								writer.flush();	
								} else {
								}
							}
						}catch(IOException e){
							showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
						}catch(Exception e){
							e.printStackTrace();
						}
					});

			clientstage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					try {
						Map<String, String> json1 = new HashMap<>();
        				json1.put("endGame", "");
        				JSONObject object1 = JSONObject.fromObject(json1);      				
        				writer.write(object1.toString() + "\n");
						writer.flush();
						Map<String, String> json = new HashMap<>();
						json.put("exit", "exit");
						JSONObject object = JSONObject.fromObject(json);
						writer.write(object.toString()+"\n");
						writer.flush();
						System.exit(0);
					}
					catch (IOException e)
					{
						showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
					}
				}
			});
			
			vote_horizontal.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						pass_vote.setDisable(true);
						vote_horizontal.setDisable(true);
						vote_vertical.setDisable(true);	
						cancel.setDisable(false);
						Map<String, String> json1 = new HashMap<>();					
						//vertical
						int y = 0, y1 = 0, y2 = 0;
						for(int j=0; j<width; j++) {
							if(!Tile[row][j].getText().equals("")) {
								y++;
								if(y==1) {
									y1 = j;
								}
							}
							else if(j<column&&Tile[row][j].getText().equals("")){
								y = 0;	
							}else if(j>column&&Tile[row][j].getText().equals("")){
								y2 = j-1;
								break;
							}
						}		
						String putChary = row + "," + y1 + "," + row + "," + y2;
						System.out.println(putChary);
						
						
						for(int i=0; i<Tile.length; i++) {
							for(int j=0; j<Tile.length; j++) {
								if(!Tile[i][j].getText().equals("")) {
									Tile[i][j].setStyle("-fx-background-color: pink;"+
					                        "-fx-border-width: 0.2;" +
					                        "-fx-border-insets: 1;" +
					                        "-fx-border-radius: 5;" +
					                        "-fx-border-color: black;");
								}
							}
						}
						for(int j = y1; j<= y2; j++) {
			                Tile[row][j].setStyle("-fx-background-color: lightblue;"+
			                        "-fx-border-width: 0.2;" +
			                        "-fx-border-insets: 1;" +
			                        "-fx-border-radius: 5;" +
			                        "-fx-border-color: black;");
						}
						
						row = 0;
						column = 0;
						json1.put("vote", putChary);
						JSONObject object1 = JSONObject.fromObject(json1);
						writer.write(object1.toString() + "\n");
						writer.flush();
					}catch(IOException e){
						showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
					}catch(Exception e){
						e.printStackTrace();
					}
				}});
			
			vote_vertical.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						
						Map<String, String> json1 = new HashMap<>();					
						//horizontal
						int x = 0, x1 =0, x2 = 0; 
						for(int i=0; i<length; i++) {
							if(!Tile[i][column].getText().equals("")) {
								x++;
								if(x==1) {
									x1 = i;
								}
							}
							else if(i<row&&Tile[i][column].getText().equals("")){
								x=0;	
							}else if(i>row&&Tile[i][column].getText().equals("")){
								x2 = i-1;
								break;
							}
						}		
						String putCharx = x1 + "," + column + "," + x2 + "," + column;
						System.out.println(putCharx);
						
						
						for(int i=0; i<Tile.length; i++) {
							for(int j=0; j<Tile.length; j++) {
								if(!Tile[i][j].getText().equals("")) {
									Tile[i][j].setStyle("-fx-background-color: pink;"+
					                        "-fx-border-width: 0.2;" +
					                        "-fx-border-insets: 1;" +
					                        "-fx-border-radius: 5;" +
					                        "-fx-border-color: black;");
								}
							}
						}
						for(int i = x1; i<= x2; i++) {
			                Tile[i][column].setStyle("-fx-background-color: lightblue;"+
			                        "-fx-border-width: 0.2;" +
			                        "-fx-border-insets: 1;" +
			                        "-fx-border-radius: 5;" +
			                        "-fx-border-color: black;");
						}
						row = 0;
						column = 0;
						json1.put("vote", putCharx);
						JSONObject object1 = JSONObject.fromObject(json1);
						writer.write(object1.toString() + "\n");
						writer.flush();
					}catch(IOException e){
						showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
					}catch(Exception e){
						e.printStackTrace();
					}
				}});
			vote.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						
						Map<String, String> json1 = new HashMap<>();					
						//horizontal
						int x = 0, x1 =0, x2 = 0; 
						for(int i=0; i<length; i++) {
							if(!Tile[i][column].getText().equals("")) {
								x++;
								if(x==1) {
									x1 = i;
								}
							}
							else if(i<row&&Tile[i][column].getText().equals("")){
								x=0;	
							}else if(i>row&&Tile[i][column].getText().equals("")){
								x2 = i-1;
								break;
							}
						}
						System.out.println("x1: " + x1 + "x2" + x2);
						//vertical
						int y = 0, y1 = 0, y2 = 0;
						for(int j=0; j<width; j++) {
							if(!Tile[row][j].getText().equals("")) {
								y++;
								if(y==1) {
									y1 = j;
								}
							}
							else if(j<column&&Tile[row][j].getText().equals("")){
								y = 0;	
							}else if(j>column&&Tile[row][j].getText().equals("")){
								y2 = j-1;
								break;
							}
						}		
						//horizontal
						String putCharx = x1 + "," + column + "," + x2 + "," + column;
						//vertical
						String putChary = row + "," + y1 + "," + row + "," + y2;
						System.out.println(putChary);
						json1.put("vote", putChary);
						JSONObject object1 = JSONObject.fromObject(json1);
						writer.write(object1.toString() + "\n");
						writer.flush();
					}catch(IOException e){
						showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
					}catch(Exception e){
						e.printStackTrace();
					}
				}});

			pass_vote.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						pass_vote.setDisable(true);
						vote_horizontal.setDisable(true);
						vote_vertical.setDisable(true);	
						vote.setDisable(true);
						cancel.setDisable(false);
						row = 0;
						column = 0;
						Map<String, String> json1 = new HashMap<>();
						json1.put("pass", "");
						JSONObject object1 = JSONObject.fromObject(json1);
						writer.write(object1.toString() + "\n");
						writer.flush();
					}catch(IOException e){
						showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
					}catch(Exception e){
						e.printStackTrace();
					}

				}});

			pass_submit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						if (!submit_list.contains(submit)){
							removelocation(row,column);
		                    Tile[row][column].setText("");
		                    Tile[row][column].setStyle("-fx-background-color: white;"+
			                        "-fx-border-width: 0.2;" +
			                        "-fx-border-insets: 1;" +
			                        "-fx-border-radius: 5;" +
			                        "-fx-border-color: black;");
		                }
						dragCount=0;
						cancel.setDisable(true);
						submit.setDisable(true);
						row = 0;
						column = 0;
						Map<String, String> json1 = new HashMap<>();
						json1.put("passWithoutPut", "");
						JSONObject object1 = JSONObject.fromObject(json1);
						writer.write(object1.toString() + "\n");
						writer.flush();
					}catch(IOException e){
						showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
					}catch(Exception e){
						e.printStackTrace();
					}

				}});

			cancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dragCount=0;
					int x = row+1;
	                int y = column+1;
	                cancel.setDisable(true);
	                submit.setDisable(true);
	                String submit = Integer.toString(x)+"#"+Integer.toString(y);
	                if (!submit_list.contains(submit)){
	                	removelocation(row,column);
	                    Tile[row][column].setText("");
	                    Tile[row][column].setStyle("-fx-background-color: white;"+
		                        "-fx-border-width: 0.2;" +
		                        "-fx-border-insets: 1;" +
		                        "-fx-border-radius: 5;" +
		                        "-fx-border-color: black;");
	                }
				}});

			submit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						int x = row+1;
		                int y = column+1;
		                Tile[row][column].setDisable(true);
		                String submit1 = Integer.toString(x)+"#"+Integer.toString(y);
		                submit_list.add(submit1);
		                Tile[row][column].setStyle("-fx-background-color: pink;"+
		                        "-fx-border-width: 0.2;" +
		                        "-fx-border-insets: 1;" +
		                        "-fx-border-radius: 5;" +
		                        "-fx-border-color: black;");
		                cancel.setDisable(true);
		                submit.setDisable(true);
		                pass_submit.setDisable(true);
		                vote_horizontal.setDisable(false);
						vote_vertical.setDisable(false);
		                pass_vote.setDisable(false);
						Map<String, String> json1 = new HashMap<>();
						String putChar = Tile[row][column].getText()
								+ "," + Integer.toString(row)+ "," + Integer.toString(column);
						System.out.println(putChar);
						json1.put("put", putChar);
						JSONObject object1 = JSONObject.fromObject(json1);
						System.out.println(object1.toString());
						writer.write(object1.toString() + "\n");
						writer.flush();
					}catch(IOException e){
						showAlert(Alert.AlertType.ERROR, clientstage.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});

		}catch (IOException e){
			e.printStackTrace();
		}
	}

	//setter and getter
	public Button getGame() {
		return Game;
	}

	public void setGame(Button game) {
		Game = game;
	}

	public Button getInvite() {
		return Invite;
	}

	public void setInvite(Button invite) {
		Invite = invite;
	}

	public Stage getClientstage() {
		return clientstage;
	}

	public void setClientstage(Stage clientstage) {
		this.clientstage = clientstage;
	}

	public void closeStage() {
		this.clientstage.close();
	}

	public ArrayList<String> getOnline() {
		return online;
	}

	public void setOnline(ArrayList<String> online) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ObservableList<String> items =FXCollections.observableArrayList(online);
				onlineplayer.setItems(items);
			}
		});
		this.count = 0;
	}

	public void setPlayerInGame(ArrayList<String> online) {
		this.online = new ArrayList<String>();
		String text = "";
		for(String username: online) {
			text = text + username + "\r\n";
			this.online.add(username);
		}
		gameplayer.setText(text);
	}

	//create scene2 that is Scrabble Game GUI
	private Scene createScene2() {
		GridPane subgrid = new GridPane();
		GridPane subgrid2 = new GridPane();
		Grid = new BorderPane();
		BorderPane panel_1 = new BorderPane();
		Grid.setTop(panel_1);
		Grid.setMargin(panel_1, new Insets(30,20,20,20));
		Label headerLabel = new Label("Welcome Game! " + username);
		headerLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		GridPane.setMargin(headerLabel, new Insets(0,40,40,40));
		panel_1.setCenter(headerLabel);
		BorderPane panel_2 = new BorderPane();
		Grid.setCenter(panel_2);
		GridPane.setMargin(panel_2, new Insets(0,0,40,0));
		alph_length = 26;
		Word = new Label[alph_length];
		for (int i =0;i<alph_length;i++){
			Label newWord = new Label();
			int color= i%5;
			   if(color == 1) {
			   newWord.setTextFill(Color.web("#0076a3"));
			   }else if(color == 2){
			       newWord.setTextFill(Color.web("#00a32d"));
			   }else if(color == 3){
			       newWord.setTextFill(Color.web("#a32d00"));
			   }else if (color ==4){
			       newWord.setTextFill(Color.web("#a37f00"));
			   }else{
			       newWord.setTextFill(Color.web("#ff6fff"));
			   }
			Word[i]=newWord;
			Word[i].setFont(Font.font("Arial", FontPosture.ITALIC,18));
			Word[i].setMaxWidth(40);
			Word[i].setAlignment(Pos.CENTER);
			Word[i].setText(Character.toString((char)(i+65)));
			Word[i].setVisible(false);
			subgrid2.add(Word[i],i,1,1,1);
		}
		subgrid2.setVgap(20);
		subgrid2.setHgap(20);
		subgrid2.setAlignment(Pos.BASELINE_CENTER);

		length = 20;
		width = 20;
		Label [] vertical = new Label[21];
		for(int i =0;i<vertical.length;i++){
			Label ver = new Label();
			vertical[i]=ver;
			vertical[i].setFont(Font.font("Arial", FontPosture.REGULAR,20));
			vertical[i].setMinSize(28,30);
			int content = i;
			vertical[i].setText(Integer.toString(content));
			vertical[i].setAlignment(Pos.CENTER);
			vertical[i].setStyle("-fx-background-color: lightyellow;" +
					"-fx-border-width: 0.2;" +
					"-fx-border-insets: 1;" +
					"-fx-border-radius: 5;" +
					"-fx-border-color: black;");
			subgrid.add(vertical[i],0, i+1,1,1);
		}
		Label [] horizon = new Label [20];
		for(int i =0;i<horizon.length;i++){
			Label horz = new Label();
			horizon[i]=horz;
			horizon[i].setFont(Font.font("Arial", FontPosture.REGULAR,20));
			horizon[i].setMinSize(28,30);
			int content = i+1;
			horizon[i].setText(Integer.toString(content));
			horizon[i].setAlignment(Pos.CENTER);
			horizon[i].setStyle("-fx-background-color: lightyellow;" +
					"-fx-border-width: 0.2;" +
					"-fx-border-insets: 1;" +
					"-fx-border-radius: 5;" +
					"-fx-border-color: black;");
			subgrid.add(horizon[i],i+1, 1,1,1);
		}

		this.Tile=new Label[length][width];
		for (int i = 0; i< length; i++){
			for (int j = 0; j< width; j++){
				Label newTile = new Label();
				Tile[i][j]=newTile;
				Tile[i][j].setFont(Font.font("Arial", FontPosture.REGULAR,20));
				Tile[i][j].setMinSize(28,30);
				Tile[i][j].setAlignment(Pos.CENTER);
				Tile[i][j].setStyle("-fx-background-color: white;" +
						"-fx-border-width: 0.2;" +
						"-fx-border-insets: 1;" +
						"-fx-border-radius: 5;" +
						"-fx-border-color: black;");
				subgrid.add(Tile[i][j], j+1, i+2,1,1);
			}
		}
		for (int i = 0; i< length; i++){
			for (int j = 0; j< width; j++){
				handlerDragListener(Tile[i][j],i,j,Grid);
			}
		}
		for (int i =0;i<alph_length;i++){
			handlerDragEvent(Word[i]);
		}

		Label score = new Label("Score");
		score.setTextFill(Color.web("#0022a3"));
		score.setFont(Font.font("Arial", FontPosture.REGULAR,20));
		text1.setFont(Font.font("Arial", FontPosture.REGULAR,20));
		text1.setFill(Color.web("#e25822"));

		GridPane GPscore = new GridPane();
		GPscore.setAlignment(Pos.BASELINE_CENTER);
		GPscore.setHgap(30);
		GPscore.setVgap(30);
		GPscore.add(score,0,0,1,1);
		GPscore.add(text1,0,1,1,1);

		GridPane subgrid3 = new GridPane();
		subgrid3.setAlignment(Pos.BASELINE_CENTER);
		subgrid3.setHgap(10);
		subgrid3.setVgap(10);

		subgrid.setAlignment(Pos.BASELINE_CENTER);
		subgrid2.setAlignment(Pos.BASELINE_CENTER);

		subgrid3.add(vote_horizontal, 0, 0, 1,1);
		subgrid3.add(vote_vertical, 0, 1,1,1);

		subgrid3.add(pass_vote,0,2,1,1);
		subgrid3.add(submit,0,3,1,1);
		subgrid3.add(pass_submit,0,4,1,1);
		subgrid3.add(cancel,0,5,1,1);
		subgrid3.add(end_game,0,6,1,1);
		
		BorderPane panel_10 = new BorderPane();
		panel_10.setCenter(text2);
		subgrid3.add(panel_10,0,7,1,1);
		text2.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
		panel_10.setPadding(new Insets(0,100,0,0));
			
		panel_2.setLeft(GPscore);
		panel_2.setMargin(GPscore, new Insets(0,0,20,40));
		
		panel_2.setRight(subgrid3);
		BorderPane panel_3 = new BorderPane();
		panel_2.setCenter(panel_3);
		panel_2.setMargin(subgrid3, new Insets(0,20,20,0));
		
		panel_3.setCenter(subgrid);
		panel_3.setBottom(subgrid2);
		panel_3.setMargin(subgrid2, new Insets(10,0,10,0));
		
		vote.setTextFill(Color.web("#0076a3"));
        pass_vote.setTextFill(Color.web("#00a32d"));
        submit.setTextFill(Color.web("#a32d00"));
        cancel.setTextFill(Color.web("#a37f00"));
        pass_submit.setTextFill(Color.web("#ff6fff"));
        
		Scene gamescene = new Scene(Grid);
		clientstage.setFullScreen(false);
		return gamescene;
	}

	public void open(){
		if (Word!=null){
			for (int i =0;i<alph_length;i++){
				Word[i].setVisible(true);
				System.out.println(Word[i]);
			}
			System.out.println(Word);
		}

	}

	public void setDragCount (){
		dragCount=0;
	}

	public void close(){
		if (Word!=null){
			for (int i =0;i<alph_length;i++){
				Word[i].setVisible(false);
				System.out.println(Word[i]);
			}
			System.out.println(Word);
		}

	}

	private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.initOwner(owner);
		alert.show();
	}

	private void locateNotnullcharacter (){
		for(int i=0;i<length;i++){
			for(int j=0;j<width;j++){
				if(!Tile[i][j].getText().equals("")){
					int row = i+1;
					int column = j+1;
					location.add(row+"#"+column);
					unique = new HashSet<>(location);
				}
			}
		}
	}

	private void removelocation(int row,int column){
		int x = row+1;
		int y = column+1;
		String coord=x+"#"+y;
		for (int i=0;i<location.size();i++){

			if (location.get(i).equals(coord)){
				location.remove(i);
			}
		}
		System.out.println(coord);
		unique.remove(coord);
		System.out.println(unique);
	}

	private boolean check(int row,int column){
		if (unique.size()>1){
			for (String s : unique){
				int split = s.indexOf("#");
				String substring=s.substring(0,split);
				int x=Integer.parseInt(substring);
				String rest = s.substring(split+1,s.length());
				int y=Integer.parseInt(rest);
				if (x==row+1){
					if(y==column||y==column+2){
						return true;
					}
				}
				else if (y==column+1){
					if(x==row||x==row+2){
						return true;
					}
				}
			}
			return false;
		}else{
			return true;
		}

	}

	//drag action that set available for each tile
	private void handlerDragListener(Label grid, int i,int j,BorderPane grid2){
		grid.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getGestureSource() != grid &&
						event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
				
				event.consume();
			}
		});
		grid.setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != grid &&
						event.getDragboard().hasString()) {
				}
				event.consume();
			}
		});
		grid.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				event.consume();
			}
		});
		grid.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {

				if (dragCount>0){
					showAlert(Alert.AlertType.ERROR, grid2.getScene().getWindow(), "Drag Error!", "You can only drag once!");
				}else{
					Dragboard db = event.getDragboard();
					boolean success = false;
					if (db.hasString()) {
						grid.setText(db.getString());
						locateNotnullcharacter();
						success = true;
						grid.setStyle("-fx-background-color: lightblue;"+
								"-fx-border-width: 0.2;" +
								"-fx-border-insets: 1;" +
								"-fx-border-radius: 5;" +
								"-fx-border-color: black;");
						grid.setText(db.getString());
					}
					System.out.println(grid.getText()+"\n"+"row "+(i+1)+"\n"+"column "+(j+1));
					System.out.println(location);
					System.out.println(1+Tile[i][j].getText()+1);
					if(!check(i,j)){
						showAlert(Alert.AlertType.ERROR, grid2.getScene().getWindow(), "Drag Error!", "Please drag to the adjacent!");
						removelocation(i,j);
						grid.setText("");
						grid.setStyle("-fx-background-color: white;"+
								"-fx-border-width: 0.2;" +
								"-fx-border-insets: 1;" +
								"-fx-border-radius: 5;" +
								"-fx-border-color: black;");
						dragCount=-1;
					}else{
						row = i;
						column=j;
						cancel.setDisable(false);
						submit.setDisable(false);
					}
					dragCount++;
					System.out.println(unique);
					event.setDropCompleted(success);
					event.consume();
				}

			}
		});

	}

	//method to detect the drag action
	private void handlerDragEvent(Label Tile){
		Tile.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				/* drag was detected, start a drag-and-drop gesture*/
				/* allow any transfer mode */
				Dragboard db = Tile.startDragAndDrop(TransferMode.ANY);

				/* Put a string on a dragboard */
				ClipboardContent content = new ClipboardContent();
				content.putString(Tile.getText());
				db.setContent(content);
				Image img = new Image(getClass().getResource(Tile.getText().toLowerCase()+".png").toExternalForm());
				db.setDragView(img);
				event.consume();
			}
		});
	}

	
	public void setText(String msg) {
		String[] input = msg.split(",");
		char c = input[0].charAt(0);
		int x = Integer.parseInt(input[1]);
		int y = Integer.parseInt(input[2]);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Tile[x][y].setText(""+c);
				Tile[x][y].setDisable(true);
			}
		});
	}

	public String getWord(int x1,int y1,int x2,int y2) {
		String word = "";
		if(x1 == x2 && y1 == y2) {
			word = Tile[x1][y1].getText();
		} else if(x1 == x2) {
			for(int j = y1; j <= y2; j++) {
				word += Tile[x1][j].getText();
			}	
		} else if(y1 == y2) {
			for(int i = x1; i <= x2; i++) {
				word += Tile[i][y1].getText();
			}	
		}
		return word;
	}
	
	private void clearTile() {
		for(int i=0; i<Tile.length;i++) {
			for(int j=0; j<Tile.length; j++) {
				Tile[i][j].setText("");
			}
		}
	}
	
	public void setText1(String text, String text2) {	
		System.out.println("yes: +" + text);
		System.out.println("no: +" + text2);
		String previous = this.text1.getText();
		if (text2.equals("")) {
			this.text1.setText("");
		} else {
			String[] scoreList =  previous.trim().split("\n");
			
			int index = -1;
			for(int i = 0; i < scoreList.length; i++) {
				System.out.println(i + scoreList[i]);
				if(scoreList[i].equals("")) continue;
				String[] scoreList1 = scoreList[i].split(":");
				if(scoreList1[0].trim().equals(text2)) {
					index = i;
					break;
				}
			}
			scoreList[index] = text;
			String newList = "";
			for(String line : scoreList) {
				newList = newList + line + "\n";
			}
			this.text1.setText(newList);
		}
		
	}
	
	public void setText2(String text) {
		this.turn = text;
		this.text2.setText("Turn: " + text);
	}
	
	
	public void appendText1(String text) {
		String value = text1.getText();
		this.text1.setText(value + "\n" + text);
	}

	//setter and getter
	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}


	public Button getPass_submit() {
		return pass_submit;
	}

	public void setPass_submit(Button pass_submit) {
		this.pass_submit = pass_submit;
	}


	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedWriter getWriter() {
		return writer;
	}

	public void setWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public TextArea getGameplayer() {
		return gameplayer;
	}

	public void setGameplayer(TextArea gameplayer) {
		this.gameplayer = gameplayer;
	}

	public ListView<String> getOnlineplayer() {
		return onlineplayer;
	}

	public void setOnlineplayer(ListView<String> onlineplayer) {
		this.onlineplayer = onlineplayer;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Label getTile(int x, int y) {
		return Tile[x][y];
	}

	public void setTile(Label[][] tile) {
		Tile = tile;
	}

	public ArrayList<String> getLocation() {
		return location;
	}

	public void setLocation(ArrayList<String> location) {
		this.location = location;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Set<String> getUnique() {
		return unique;
	}

	public void setUnique(Set<String> unique) {
		this.unique = unique;
	}

	public Text getText1() {
		return text1;
	}

	public void setText1(Text text1) {
		this.text1 = text1;
	}

	public ArrayList<String> getSubmit_list() {
		return submit_list;
	}

	public void setSubmit_list(ArrayList<String> submit_list) {
		this.submit_list = submit_list;
	}

	public Button getVoteHorizontal() {
		return vote_horizontal;
	}

	public Button getVoteVertical() {
		return vote_vertical;
	}

	public Button getVote() {
		return vote;
	}

	public void setVote(Button vote) {
		this.vote = vote;
	}

	public Button getPass_vote() {
		return pass_vote;
	}

	public void setPass_vote(Button pass_vote) {
		this.pass_vote = pass_vote;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}
	
	public static void setScene(Stage stage, Scene scene) {
		stage.setScene(scene);
	}

	public Button getEnd_game() {
		return end_game;
	}

	public void setEnd_game(Button end_game) {
		this.end_game = end_game;
	}
	
	public Stage getClientStage() {
		return this.clientstage;
	}
	public void recoverTileColor() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<Tile.length; i++) {
					for(int j=0; j<Tile.length; j++) {
						if(!Tile[i][j].getText().equals("")) {
							Tile[i][j].setStyle("-fx-background-color: pink;"+
			                        "-fx-border-width: 0.2;" +
			                        "-fx-border-insets: 1;" +
			                        "-fx-border-radius: 5;" +
			                        "-fx-border-color: black;");
						}
					}
				}
			}
		});
		
	}
	
	public void highlightWord(int x1,int y1,int x2,int y2) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i=x1; i<=x2; i++) {
					for(int j=y1; j<=y2; j++) {
						if(!Tile[i][j].getText().equals("")) {
							Tile[i][j].setStyle("-fx-background-color: lightblue;"+
			                        "-fx-border-width: 0.2;" +
			                        "-fx-border-insets: 1;" +
			                        "-fx-border-radius: 5;" +
			                        "-fx-border-color: black;");
						}
					}
				}
			}
		});
	}

}
