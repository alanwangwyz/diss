//Bug-killer
//Lakshmi
//Thursday 10am
package Server;

import java.net.ServerSocket;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

//class is used to construct GUI for the server
public class ServerGUI {
    private ServerSocket listeningSocket;
    private TextField passwordFiled;


    public ServerGUI(ServerSocket listeningSocket, TextField passwordFiled)
    {
        this.listeningSocket=listeningSocket;
        this.passwordFiled = passwordFiled;
    }

    public void ServerGUI() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Server");
        Scene scene;
        GridPane gridPane = createClientGridPane();

        Label headerLabel = new Label("Current user");
        Label Process = new Label("Process");
        Text sceneTitle = new Text("Server");
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
        sceneTitle.setFont(Font.font("Arial", 25));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
        GridPane.setMargin(sceneTitle, new Insets(20, 0, 20, 0));
        // current user
        headerLabel.setTextFill(Color.web("#10a200"));
        headerLabel.setFont(Font.font("Arial",17));
        Process.setTextFill(Color.web("#bc0f9f"));
        Process.setFont(Font.font("Arial",17));
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setHalignment(Process, HPos.CENTER);
        gridPane.add(headerLabel, 0, 1);
        gridPane.add(Process, 1, 1);
        TextArea currentUser = new TextArea();
        currentUser.setWrapText(true);
        gridPane.add(currentUser, 0, 3, 1, 3);

        TextArea processes = new TextArea();
        processes.setWrapText(true);
        gridPane.add(processes, 1, 2, 1, 4);
        TextField currentUser1 = new TextField(String.valueOf(ServerState.getInstance().getConnectedClients().size())+" client"+"\n");
        currentUser1.setAlignment(Pos.CENTER);
        gridPane.add(currentUser1, 0, 2, 1, 1);

        processes.appendText(Thread.currentThread().getName() +
                " - Server listening on port "+passwordFiled.getText()+" for a connection"+"\n");
        ThreadManager threadManager = new ThreadManager(listeningSocket,processes,currentUser,currentUser1);
        threadManager.start();

        scene = new Scene(gridPane, 650, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });


    }
    public GridPane createClientGridPane ()
        {
            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            return gridPane;
        }
}
