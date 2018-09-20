//Yizhou Wang
//669026
//DS project1
package Client;

import java.io.*;
import java.net.Socket;

import javafx.beans.Observable;
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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.*;
import org.json.simple.JSONObject;


public class ClientGUI {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String username;

    public ClientGUI(Socket socket,String username)
    {
        try
        {
            this.socket=socket;
            this.username=username;
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            this.reader = reader;
            this.writer = writer;
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    public void clientGUI() {
        try
        {
            writer.write("username"+"\t");
            writer.write(username+"\n");
            writer.flush();
            Stage clientstage = new Stage();
            clientstage.setTitle("Scrabble Game Lobby");
            Scene clientscene;
            GridPane GP = createClientGridPane();
            Label headerLabel = new Label("Welcome! "+username);
            headerLabel.setFont(Font.font("Lucida Handwriting", FontWeight.BOLD, 20));
            GridPane.setHalignment(headerLabel, HPos.CENTER);
            GridPane.setMargin(headerLabel, new Insets(0, 0,20,0));
            GP.add(headerLabel,0,0,2,1);

//            Button ADD = new Button();
//            Button QUERY = new Button();
//            Button REMOVE = new Button();
//            TextField query = new TextField();
//            TextField addkey = new TextField();
//            addkey.setMinWidth(270);
//            TextField addvalue = new TextField();
//            addvalue.setMaxWidth(270);
//            TextField remove = new TextField();
//            TextArea msg = new TextArea();
//            msg.setWrapText(true);
//            msg.setPrefRowCount(6);
//
//            ADD.setText("ADD");
//            QUERY.setText("Query");
//            REMOVE.setText("Remove");
//
//            GP.add(ADD,2,2,1,1);
//            GP.add(QUERY, 2,1,1,1);
//            GP.add(REMOVE, 2,3,1,1);
//            GP.add(query, 0,1,2,1);
//            GP.add(addkey, 0,2,1,1);
//            GP.add(addvalue,1,2,1,1);
//            GP.add(remove, 0,3,2,1);
//            GP.add(msg, 0,4,2,1);
            Label onlinePlayer = new Label("Online Player");
            onlinePlayer.setFont(Font.font("Arial", 25));
            onlinePlayer.setAlignment(Pos.CENTER);
            onlinePlayer.setTextFill(Color.web("#42B97C"));
            GP.add(onlinePlayer,0,1);

            Label gamePlayer = new Label("Player in game");
            gamePlayer.setFont(Font.font("Arial", 25));
            gamePlayer.setAlignment(Pos.CENTER);
            gamePlayer.setTextFill(Color.web("#F47F42"));
            GP.add(gamePlayer,1,1);


            ArrayList<String> online = new ArrayList<>();
            online.add("Clinet1 Evan");
            online.add("Clinet2 Happy");
            ListView<String> onlineplayer=new ListView<>(FXCollections.observableArrayList(online));
            onlineplayer.setCellFactory(TextFieldListCell.forListView());
            onlineplayer.setEditable(false);
//            onlineplayer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


            GP.add(onlineplayer,0,2,1, 5);

            TextArea gameplayer = new TextArea();
            gameplayer.setWrapText(true);
            GP.add(gameplayer,1,2,1, 5);




            Button Invite = new Button("Invite All");
            Invite.setPrefHeight(30);
            Invite.setDefaultButton(true);
            Invite.setPrefWidth(70);
            Button Game = new Button("Start Game");
            Game.setPrefHeight(30);
            Game.setDefaultButton(true);
            Game.setPrefWidth(70);
            GP.add(Game,1,8);
            GP.add(Invite, 0,8);

            clientscene=new Scene(GP,700,600);
            clientstage.setScene(clientscene);
            clientstage.show();
//            MessageListener ml = new MessageListener(reader, msg);
//            ml.start();


            //Use a scanner to read input from the console

//            controller send = new controller(QUERY,ADD,REMOVE,writer,query,addkey,addvalue,remove,msg,GP);
//            send.start();

//            check check = new check(socket,writer,msg);
//            check.start();
            Game.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    clientstage.close();
                    new GameGUI().GameGUI();
                }});

            Invite.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        writer.write("INVITEALL"+"\n");
                        writer.flush();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }});

            onlineplayer.getSelectionModel().selectedItemProperty().addListener(
                    (ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
                        try{
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
//                        alert.setHeaderText("Look, a Confirmation Dialog");
                            alert.setContentText("Do you want to invite this player?");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                writer.write(newValue+"\n");
                                writer.flush();
                            } else {
                                // ... user chose CANCEL or closed the dialog
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

//                        showAlert(Alert.AlertType.CONFIRMATION, GP.getScene().getWindow(), "Invite Confirmation", "Do you want to invite this client?");
//                        Optional<ButtonType> result = alert.showAndWait();
//                        if (result.get() == ButtonType.OK){

                    });

            clientstage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        writer.write("EXIT" + "\n");
                        writer.flush();
                    }
                    catch (IOException e)
                    {
//                        msg.setText("Server is offline! Please restart the server or try later!");
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private GridPane createClientGridPane ()
    {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        return gridPane;
    }

//    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.initOwner(owner);
//        alert.show();
//    }

}



