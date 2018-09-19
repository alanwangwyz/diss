//Yizhou Wang
//669026
//DS project1
package Server;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.ServerSocket;

public class LoginGUI extends Application{
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Server Login");

        GridPane gridPane = createRegistrationFormPane();

        Label passwordLabel = new Label("PortNumber: ");
        gridPane.add(passwordLabel, 0, 1);

        TextField passwordField = new TextField();
        passwordField.setPrefHeight(30);
        gridPane.add(passwordField, 1, 1);

        Button loginButton = new Button("Create");
        loginButton.setPrefHeight(30);
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(70);
        gridPane.add(loginButton, 0, 4, 2, 1);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setMargin(loginButton, new Insets(20, 0,20,0));
        Label headerLabel = new Label("Welcome to server");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        Scene scene = new Scene(gridPane, 400, 300);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        primaryStage.show();

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if ( passwordField.getText().isEmpty() ) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Port number Error!", "Please enter Port number!");
                }
                try{
                    ServerSocket listeningSocket = new ServerSocket(Integer.parseInt(passwordField.getText()));
                    primaryStage.close();
                    new ServerGUI(listeningSocket,passwordField).ServerGUI();
                }catch (NumberFormatException e){
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Port Number Error!", "Please enter the number!");
                }catch(IOException e){
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "IO Error!", "Error to create server!");
                }catch(Exception e){e.printStackTrace();}
                }
            });
        }

    private GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
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

