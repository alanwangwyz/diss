//Yizhou Wang
//669026
//DS project1
package Client;

import java.io.*;
import java.net.Socket;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ClientGUI {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public ClientGUI(Socket socket)
    {
        try
        {
            this.socket=socket;
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

            Stage clientstage = new Stage();
            clientstage.setTitle("Online Dictionary");
            Scene clientscene;
            GridPane GP = createClientGridPane();
            Label headerLabel = new Label("Welcome!");
            headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            GridPane.setHalignment(headerLabel, HPos.CENTER);
            GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
            GP.add(headerLabel,0,0,2,1);
            Button ADD = new Button();
            Button QUERY = new Button();
            Button REMOVE = new Button();
            TextField query = new TextField();
            TextField addkey = new TextField();
            addkey.setMinWidth(270);
            TextField addvalue = new TextField();
            addvalue.setMaxWidth(270);
            TextField remove = new TextField();
            TextArea msg = new TextArea();
            msg.setWrapText(true);
            msg.setPrefRowCount(6);

            ADD.setText("ADD");
            QUERY.setText("Query");
            REMOVE.setText("Remove");

            GP.add(ADD,2,2,1,1);
            GP.add(QUERY, 2,1,1,1);
            GP.add(REMOVE, 2,3,1,1);
            GP.add(query, 0,1,2,1);
            GP.add(addkey, 0,2,1,1);
            GP.add(addvalue,1,2,1,1);
            GP.add(remove, 0,3,2,1);
            GP.add(msg, 0,4,2,1);
            clientscene=new Scene(GP,700,350);
            clientstage.setScene(clientscene);
            MessageListener ml = new MessageListener(reader, msg);
            ml.start();


            //Use a scanner to read input from the console

            controller send = new controller(QUERY,ADD,REMOVE,writer,query,addkey,addvalue,remove,msg,GP);
            send.start();

            check check = new check(socket,writer,msg);
            check.start();

            clientstage.show();
            clientstage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        writer.write("EXIT" + "\n");
                        writer.flush();
                    }
                    catch (IOException e)
                    {
                        msg.setText("Server is offline! Please restart the server or try later!");
                    }
                }
            });
        }catch (Exception e){
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

}



