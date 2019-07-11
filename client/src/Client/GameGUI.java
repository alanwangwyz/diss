//package Client;
//
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.HPos;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.input.*;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontPosture;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextAlignment;
//import javafx.stage.Stage;
//import javafx.stage.Window;
//import javafx.stage.WindowEvent;
//import net.sf.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.net.Socket;
//import java.util.*;
//
//public class GameGUI{
//	private int length;
//	private int width;
//	private Label[][] Tile;
//	private ArrayList<String> location=new ArrayList<>();
//	private int row;
//	private int column;
//	private Set<String> unique;
//	private BufferedWriter writer;
//	private BufferedReader reader;
//	private GridPane Grid = new GridPane();
//	private Text text1 = new Text("");
//	private ArrayList <String> submit_list = new ArrayList<>();
//	private Button vote = new Button("Vote");
//	private Button pass_vote = new Button("Pass Vote");
//	private Button submit = new Button("Submit");
//	private Button cancel = new Button("Cancel");
//	private Button pass_submit = new Button("Pass Submit");
//	private GridPane GPscore = new GridPane();
//	private Scene gamescene;
//	//private BufferedWriter writer;
//
//	public GameGUI(BufferedWriter writer) {
//		this.writer = writer;
//	}
//
//
//	public void GameGUI(){
//
//		//Parent root = FXMLLoader.load(getClass().getResource("/GameGUI.fxml"));
//
//		//Stage gameStage = new Stage();
//		//gameStage.setTitle("Scrabble Game");
//
//		//StackPane rootPane = new StackPane();
//		GridPane subgrid = createGameGridPane();
//		GridPane subgrid2 = createGameGridPane();
//
//		Grid.setHgap(10);
//		Grid.setVgap(10);
//		Grid.setPadding(new Insets(40, 40, 40, 40));
//		Grid.setAlignment(Pos.BASELINE_CENTER);
//
//		int alph_length = 26;
//		Label[] Word = new Label[alph_length];
//		for (int i =0;i<alph_length;i++){
//			Label newWord = new Label();
//			Word[i]=newWord;
//			Word[i].setFont(Font.font("Arial", FontPosture.ITALIC,18));
//			Word[i].setMaxWidth(40);
//			Word[i].setAlignment(Pos.CENTER);
//			Word[i].setText(Character.toString((char)(i+65)));
//			subgrid2.add(Word[i],i,1,1,1);
//
//		}
//		//Word[0].setStyle("-fx-background-image:url('/Client/A.png')");
//		subgrid2.setVgap(20);
//		subgrid2.setHgap(20);
//		subgrid2.setAlignment(Pos.BASELINE_CENTER);
//
//
//		length = 20;
//		width = 20;
//		Label [] vertical = new Label[21];
//		for(int i =0;i<vertical.length;i++){
//			Label ver = new Label();
//			vertical[i]=ver;
//			vertical[i].setFont(Font.font("Arial", FontPosture.REGULAR,20));
//			vertical[i].setMinSize(28,30);
//			int content = i;
//			vertical[i].setText(Integer.toString(content));
//			vertical[i].setAlignment(Pos.CENTER);
//			vertical[i].setStyle("-fx-background-color: lightyellow;" +
//					"-fx-border-width: 0.2;" +
//					"-fx-border-insets: 1;" +
//					"-fx-border-radius: 5;" +
//					"-fx-border-color: black;");
//			subgrid.add(vertical[i],0, i+1,1,1);
//		}
//		Label [] horizon = new Label [20];
//		for(int i =0;i<horizon.length;i++){
//			Label horz = new Label();
//			horizon[i]=horz;
//			horizon[i].setFont(Font.font("Arial", FontPosture.REGULAR,20));
//			horizon[i].setMinSize(28,30);
//			int content = i+1;
//			horizon[i].setText(Integer.toString(content));
//			horizon[i].setAlignment(Pos.CENTER);
//			horizon[i].setStyle("-fx-background-color: lightyellow;" +
//					"-fx-border-width: 0.2;" +
//					"-fx-border-insets: 1;" +
//					"-fx-border-radius: 5;" +
//					"-fx-border-color: black;");
//			subgrid.add(horizon[i],i+1, 1,1,1);
//
//		}
//
//		this.Tile=new Label[length][width];
//		for (int i = 0; i< length; i++){
//			for (int j = 0; j< width; j++){
//				Label newTile = new Label();
//				Tile[i][j]=newTile;
//				Tile[i][j].setFont(Font.font("Arial", FontPosture.REGULAR,20));
//				Tile[i][j].setMinSize(28,30);
//				Tile[i][j].setAlignment(Pos.CENTER);
//				Tile[i][j].setStyle("-fx-background-color: white;" +
//						"-fx-border-width: 0.2;" +
//						"-fx-border-insets: 1;" +
//						"-fx-border-radius: 5;" +
//						"-fx-border-color: black;");
//				subgrid.add(Tile[i][j], j+1, i+2,1,1);
//			}
//		}
//		for (int i = 0; i< length; i++){
//			for (int j = 0; j< width; j++){
//				handlerDragListener(Tile[i][j],i,j,Grid);
//			}
//		}
//		for (int i =0;i<alph_length;i++){
//			handlerDragEvent(Word[i]);
//		}
//		Label score = new Label("Score");
//		score.setFont(Font.font("Arial", FontPosture.REGULAR,20));
//
//		//Text text2 = new Text("Client 2");
//		text1.setFont(Font.font("Arial", FontPosture.REGULAR,20));
//		//text2.setFont(Font.font("Arial", FontPosture.REGULAR,20));
//		GridPane GPscore = new GridPane();
//		GPscore.setAlignment(Pos.BASELINE_LEFT);
//		GPscore.setHgap(30);
//		GPscore.setVgap(30);
//		GPscore.add(score,0,0,1,1);
//		GPscore.add(text1,0,1,1,1);
//		//GPscore.add(text2,0,2,1,1);
//		GridPane subgrid3 = new GridPane();
//		subgrid3.setAlignment(Pos.BASELINE_RIGHT);
//		subgrid3.setHgap(10);
//		subgrid3.setVgap(10);
//
//
//		subgrid.setAlignment(Pos.BASELINE_CENTER);
//		subgrid2.setAlignment(Pos.BASELINE_CENTER);
//
//		subgrid3.add(vote,11,0,1,1);
//		subgrid3.add(pass_vote,11,1,1,1);
//		subgrid3.add(submit,11,2,1,1);
//		subgrid3.add(cancel,11,4,1,1);
//		subgrid3.add(pass_submit,11,3,1,1);
//		//        Grid.add(pass,1,4,1,1);
//
//		Grid.add(GPscore,1,0,1,1);
//		Grid.add(subgrid,2,0,10,10);
//		Grid.add(subgrid2,2,11,10,1);
//		Grid.add(subgrid3,16,0,1,1);
//
//		gamescene = new Scene(Grid,1700,1000);
//		//gameStage.setScene(gamescene);
//		//gameStage.show();
//
//		vote.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				try {
//					pass_vote.setDisable(true);
//					vote.setDisable(true);
//					cancel.setDisable(false);
//					Map<String, String> json1 = new HashMap<>();
//					//horizontal
//					int x = 0, x1 =0, x2 = 0;
//					for(int i=0; i<length; i++) {
//						if(!Tile[i][column].getText().equals("")) {
//							x++;
//							if(x==1) {
//								x1 = i;
//							}
//						}
//						else if(i<row&&Tile[i][column].getText().equals("")){
//							x=0;
//						}else if(i>row&&Tile[i][column].getText().equals("")){
//							x2 = i-1;
//							break;
//						}
//					}
//					System.out.println("x1: " + x1 + "x2" + x2);
//					//vertical
//					int y = 0, y1 = 0, y2 = 0;
//					for(int j=0; j<width; j++) {
//						if(!Tile[row][j].getText().equals("")) {
//							y++;
//							if(y==1) {
//								y1 = j;
//							}
//						}
//						else if(j<column&&Tile[row][j].getText().equals("")){
//							y = 0;
//						}else if(j>column&&Tile[row][j].getText().equals("")){
//							y2 = j-1;
//							break;
//						}
//					}
//					//horizontal
//					String putCharx = x1 + "," + column + "," + x2 + "," + column;
//					//vertical
//					String putChary = row + "," + y1 + "," + row + "," + y2;
//					System.out.println(putChary);
//					json1.put("vote", putChary);
//					JSONObject object1 = JSONObject.fromObject(json1);
//					writer.write(object1.toString() + "\n");
//					writer.flush();
//				}catch(IOException e){
//					showAlert(Alert.AlertType.ERROR, Grid.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}});
//
//		pass_vote.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				try {
//					pass_vote.setDisable(true);
//					vote.setDisable(true);
//					cancel.setDisable(false);
//					Map<String, String> json1 = new HashMap<>();
//					json1.put("pass", "");
//					JSONObject object1 = JSONObject.fromObject(json1);
//					writer.write(object1.toString() + "\n");
//					writer.flush();
//				}catch(IOException e){
//					showAlert(Alert.AlertType.ERROR, Grid.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//
//			}});
//
//		pass_submit.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				try {
//					cancel.setDisable(true);
//					Map<String, String> json1 = new HashMap<>();
//					json1.put("passWithoutPut", "");
//					JSONObject object1 = JSONObject.fromObject(json1);
//					writer.write(object1.toString() + "\n");
//					writer.flush();
//				}catch(IOException e){
//					showAlert(Alert.AlertType.ERROR, Grid.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//
//			}});
//
//		cancel.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				int x = row+1;
//                int y = column+1;
//                String submit = Integer.toString(x)+"#"+Integer.toString(y);
//                if (submit_list.contains(submit)){
//
//                }
//                else {
//                    removelocation(row,column);
//                    Tile[row][column].setText("");
//                }
//			}});
//
//		submit.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				try {
//					cancel.setDisable(true);
//					int x = row+1;
//	                int y = column+1;
//	                String submit1 = Integer.toString(x)+"#"+Integer.toString(y);
//	                submit_list.add(submit1);
//	                Tile[row][column].setStyle("-fx-background-color: pink;"+
//	                        "-fx-border-width: 0.2;" +
//	                        "-fx-border-insets: 1;" +
//	                        "-fx-border-radius: 5;" +
//	                        "-fx-border-color: black;");
//	                submit.setDisable(true);
//	                pass_submit.setDisable(true);
//	                vote.setDisable(false);
//	                pass_vote.setDisable(false);
//					Map<String, String> json1 = new HashMap<>();
//					String putChar = Tile[row][column].getText()
//							+ "," + Integer.toString(row)+ "," + Integer.toString(column);
//					System.out.println(putChar);
//					json1.put("put", putChar);
//					JSONObject object1 = JSONObject.fromObject(json1);
//					System.out.println(object1.toString());
//					writer.write(object1.toString() + "\n");
//					writer.flush();
//				}catch(IOException e){
//					showAlert(Alert.AlertType.ERROR, Grid.getScene().getWindow(), "IO Error!", "Server is offline! Please restart the server or try later!");
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//
//			}});
//		/*
//		gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//			@Override
//			public void handle(WindowEvent event) {
//				System.exit(0);
//			}
//		});
//		*/
//	}
//
//	public Scene getGamescene() {
//		return gamescene;
//	}
//
//
//	public void setGamescene(Scene gamescene) {
//		this.gamescene = gamescene;
//	}
//
//
//	private GridPane createGameGridPane ()
//	{
//		GridPane gridPane = new GridPane();
//		gridPane.setAlignment(Pos.CENTER_LEFT);
//		return gridPane;
//	}
//
//	private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
//		Alert alert = new Alert(alertType);
//		alert.setTitle(title);
//		alert.setHeaderText(null);
//		alert.setContentText(message);
//		alert.initOwner(owner);
//		alert.show();
//	}
//
//	private void locateNotnullcharacter (){
//		for(int i=0;i<length;i++){
//			for(int j=0;j<width;j++){
//				if(!Tile[i][j].getText().equals("")){
//					int row = i+1;
//					int column = j+1;
//					location.add(row+"#"+column);
//					unique = new HashSet<>(location);
//				}
//			}
//		}
//	}
//
//	private void removelocation(int row,int column){
//		int x = row+1;
//		int y = column+1;
//		String coord=x+"#"+y;
//		for (int i=0;i<location.size();i++){
//
//			if (location.get(i).equals(coord)){
//				location.remove(i);
//			}
//		}
//		System.out.println(coord);
//		unique.remove(coord);
//		System.out.println(unique);
//	}
//
//
//
//	private boolean check(int row,int column){
//		//String newLocation = row+1+"#"+column+1;
//		if (unique.size()>1){
//			for (String s : unique){
//				int split = s.indexOf("#");
//				String substring=s.substring(0,split);
//				int x=Integer.parseInt(substring);
//				String rest = s.substring(split+1,s.length());
//				int y=Integer.parseInt(rest);
//				if (x==row+1){
//					if(y==column||y==column+2){
//						return true;
//					}
//				}
//				else if (y==column+1){
//					if(x==row||x==row+2){
//						return true;
//					}
//				}
//			}
//			return false;
//		}else{
//			return true;
//		}
//
//	}
//
//	private void handlerDragListener(Label grid, int i,int j,GridPane Grid){
//		grid.setOnDragOver(new EventHandler<DragEvent>() {
//			public void handle(DragEvent event) {
//				/* data is dragged over the target */
//				/* accept it only if it is not dragged from the same node
//				 * and if it has a string data */
//				if (event.getGestureSource() != grid &&
//						event.getDragboard().hasString()) {
//					/* allow for moving */
//					event.acceptTransferModes(TransferMode.MOVE);
//				}
//				event.consume();
//			}
//		});
//		grid.setOnDragEntered(new EventHandler<DragEvent>() {
//			public void handle(DragEvent event) {
//				/* the drag-and-drop gesture entered the target */
//				/* show to the user that it is an actual gesture target */
//				if (event.getGestureSource() != grid &&
//						event.getDragboard().hasString()) {
////					grid.setStyle("-fx-background-color: lightyellow;"+
////							"-fx-border-width: 0.2;" +
////							"-fx-border-insets: 1;" +
////							"-fx-border-radius: 5;" +
////							"-fx-border-color: black;");
//				}
//				event.consume();
//			}
//		});
//		grid.setOnDragExited(new EventHandler<DragEvent>() {
//			public void handle(DragEvent event) {
//				/* mouse moved away, remove the graphical cues */
////				grid.setStyle("-fx-background-color: white;"+
////						"-fx-border-width: 0.2;" +
////						"-fx-border-insets: 1;" +
////						"-fx-border-radius: 5;" +
////						"-fx-border-color: black;");
//				event.consume();
//			}
//		});
//		grid.setOnDragDropped(new EventHandler<DragEvent>() {
//			public void handle(DragEvent event) {
//				/* data dropped */
//				/* if there is a string data on dragboard, read it and use it */
//				Dragboard db = event.getDragboard();
//				boolean success = false;
//				if (db.hasString()) {
//					grid.setText(db.getString());
//					locateNotnullcharacter();
//					success = true;
//				}
//				System.out.println(grid.getText()+"\n"+"row "+(i+1)+"\n"+"column "+(j+1));
//				System.out.println(location);
//				if(!check(i,j)){
//					showAlert(Alert.AlertType.ERROR, Grid.getScene().getWindow(), "Drag Error!", "Please drag to the adjacent!");
//					removelocation(i,j);
//					grid.setText("");
//				}else{
//					row = i;
//					column=j;
//				}
//				//System.out.println("After!!!"+grid.getText()+"\n"+"row "+(i+1)+"\n"+"column "+(j+1));
//				vote.setDisable(true);
//				pass_vote.setDisable(true);
//				System.out.println(unique);
//
//				/* let the source know whether the string was successfully
//				 * transferred and used */
//				event.setDropCompleted(success);
//				event.consume();
//			}
//		});
//
//	}
//
//	private void handlerDragEvent(Label Tile){
//		Tile.setOnDragDetected(new EventHandler<MouseEvent>() {
//			public void handle(MouseEvent event) {
//				/* drag was detected, start a drag-and-drop gesture*/
//				/* allow any transfer mode */
//				Dragboard db = Tile.startDragAndDrop(TransferMode.ANY);
//
//				/* Put a string on a dragboard */
//				ClipboardContent content = new ClipboardContent();
//				content.putString(Tile.getText());
//				db.setContent(content);
//				event.consume();
//			}
//		});
//	}
//
//	public void setText(String msg) {
//		String[] input = msg.split(",");
//		char c = input[0].charAt(0);
//		int x = Integer.parseInt(input[1]);
//		int y = Integer.parseInt(input[2]);
//		Tile[x][y].setText(""+c);
//	}
//
//	public String getWord(int x1,int y1,int x2,int y2) {
//		String word = "";
//		if(x1 == x2 && y1 == y2) {
//			word = Tile[x1][y1].getText();
//		} else if(x1 == x2) {
//			for(int j = y1; j <= y2; j++) {
//				word += Tile[x1][j].getText();
//			}
//		} else if(y1 == y2) {
//			for(int i = x1; i <= x2; i++) {
//				word += Tile[i][x1].getText();
//			}
//		}
//		return word;
//	}
//
//	public void setText1(String text) {
//		this.text1.setText(text);
//	}
//
//	public Button getSubmit() {
//		return submit;
//	}
//
//
//	public void setSubmit(Button submit) {
//		this.submit = submit;
//	}
//
//
//	public Button getPass_submit() {
//		return pass_submit;
//	}
//
//	public void setPass_submit(Button pass_submit) {
//		this.pass_submit = pass_submit;
//	}
//
//	public GridPane getGPscore() {
//		return GPscore;
//	}
//
//	//    private static Label[][] initgrid(int length, int width, GridPane GP){
//	//        Label[][] grid = new Label[length][width];
//	//        for (int i = 0;i<length;i++){
//	//            for (int j=0;j<width;j++){
//	//                Label subgrid=new Label();
//	//                GP.setRowIndex(subgrid,j);
//	//                GP.setColumnIndex(subgrid,i);
//	//                GP.getChildren().add(subgrid);
//	//            }
//	//        }
//	//        return grid;
//	//    }
//}
