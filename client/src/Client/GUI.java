//Bug-killer
//Lakshmi
//Thursday 10am

package Client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import net.sf.json.JSONObject;

//GUI is to initialize the login interface for the client
public class GUI extends Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Login");
        GridPane gridPane = createRegistrationFormPane();
        Label nameLabel = new Label("Hostname: ");
        nameLabel.setTextFill(Color.web("#0076a3"));//changed
        gridPane.add(nameLabel, 0,1);

        TextField nameField = new TextField();
        nameField.setPrefHeight(30);
        gridPane.add(nameField, 1,1);

        Label passwordLabel = new Label("PortNumber: ");
        passwordLabel.setTextFill(Color.web("#00a32d"));//changed
        gridPane.add(passwordLabel, 0, 2);

        TextField passwordField = new TextField();
        passwordField.setPrefHeight(30);
        gridPane.add(passwordField, 1, 2);

        Label usernameLabel = new Label("Username: ");
        usernameLabel.setTextFill(Color.web("#a32d00"));//changed
        gridPane.add(usernameLabel, 0, 3);

        TextField usernameField = new TextField();
        usernameField.setPrefHeight(30);
        gridPane.add(usernameField, 1, 3);


        Button loginButton = new Button("Login");
        loginButton.setPrefHeight(30);
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(70);
        gridPane.add(loginButton, 0, 4, 2, 1);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setMargin(loginButton, new Insets(20, 0,20,0));
        Label headerLabel = new Label("Welcome to Scrabble!");
        headerLabel.setTextFill(Color.web("#0076a3"));//changed
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
                if(nameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your hostname");
                    return;
                }

                if(passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter the port number");
                    return;
                }
                
                if(usernameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter the username");
                    return;
                }
                try{
                    String hostname = nameField.getText().trim();
                    int portnnumber = Integer.valueOf(passwordField.getText().trim());
                    Socket socket = new Socket(hostname, portnnumber);
                    String username = usernameField.getText().trim();
                    
                    
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        			
        			//login username check
        			Map<String, String> json1 = new HashMap<>();
        			json1.put("login", username);
					JSONObject object1 = JSONObject.fromObject(json1);
					System.out.println(object1.toString());
					writer.write(object1.toString() + "\n");
					writer.flush();
        			String line;
        			line = reader.readLine();
        			if (Boolean.parseBoolean(line)) {
        				primaryStage.close();
            			new ClientGUI(socket,username,reader,writer).clientGUI();
        			} else {
        				Map<String, String> json = new HashMap<>();
						json.put("exit", "exit");
						JSONObject object = JSONObject.fromObject(json);
						writer.write(object.toString()+"\n");
						writer.flush();
        				socket.close();
        				throw new UsernameExistException();
        			}
        			
        			
                }
                catch(UsernameExistException e) 
                {
                	showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Username Already Exist!", "Please change username!");
                }
                catch (UnknownHostException e)
                {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Host Name Error!", "Please enter the host name again");
                }
                catch(SocketException e)
                {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Port Number Error!", "Please enter the port number again or check if the server is on");
                }
                catch(NumberFormatException e)
                {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Port Number Error!", "Please enter number!");
                }
                catch(IOException e)
                {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "IO Error!", "Fail to create to the server!");
                }
                catch(Exception e){e.printStackTrace();}
//
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }});
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

    public static void main(String[] args) {
        launch(args);
    }
    
}
