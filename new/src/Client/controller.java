//Yizhou Wang
//669026
//DS project1
package Client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.io.BufferedWriter;
import java.io.IOException;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

public class controller extends Thread{
    private Button REMOVE;
    private Button QUERY;
    private Button ADD;
    private BufferedWriter writer;
    private TextField query;
    private TextField addkey;
    private TextField addvalue;
    private TextField remove;
    private TextArea msg;
    private GridPane gridPane;

    public controller(Button QUERY, Button ADD, Button REMOVE, final BufferedWriter writer, final TextField query, final TextField addkey, final TextField addvalue, final TextField remove, TextArea msg, GridPane gridPane)
    {
        this.ADD = ADD;
        this.QUERY = QUERY;
        this.REMOVE = REMOVE;
        this.writer = writer;
        this.query = query;
        this.addkey = addkey;
        this.addvalue = addvalue;
        this.remove = remove;
        this.msg = msg;
        this.gridPane = gridPane;
    }

    public void run(){
        ADD.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {

                    if (addkey.getText().isEmpty()||addvalue.getText().isEmpty())
                    {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Key and value Error!", "Key and Value can't be empty!");
                    }else{
                        writer.write("ADD"+"\t");
                        writer.write(addkey.getText()+" ");
                        writer.write(addvalue.getText()+"\n");
                        writer.flush();
                        addkey.setText("");
                        addvalue.setText("");
                    }
                }catch (IOException e)
                {
                    msg.setText("Server is offline! Please restart the server or try later!");
                }
            }
        });
        QUERY.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                try{

                    if (query.getText().isEmpty())
                    {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Query Error!", "Query can't be empty!");
                    }else{
                            writer.write("QUERY"+"\t");
                            writer.write(query.getText()+"\n");
                            writer.flush();
                            query.setText("");
                    }
                }
                catch(IOException e){
                    msg.setText("Server is offline! Please restart the server or try later!");
                }
            }
        });
        REMOVE.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{

                    if (remove.getText().isEmpty())
                    {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Remove Error!", "Remove can't be empty!");
                    }else{
                            writer.write("REMOVE"+"\t");
                            writer.write(remove.getText()+"\n");
                            writer.flush();
                            remove.setText("");
                    }
                }
                catch(IOException e){
                    msg.setText("Server is offline! Please restart the server or try later!");
                }
            }
        });
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

