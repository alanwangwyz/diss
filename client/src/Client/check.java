//Bug-killer
//Lakshmi
//Thursday 10am
package Client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.sf.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class check extends Thread{
    private Socket socket;
    private BufferedWriter write;
    private Stage GP;

    //check class is to test whether server is online
    public check(Socket socket, BufferedWriter write, Stage GP)
    {
        this.socket = socket;
        this.write = write;
        this.GP = GP;
    }

    @Override
    public void run() {
        try {
            write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            while (true) {
                Thread.sleep(3 * 1000);
                Map<String, String> json1 = new HashMap<>();
                json1.put("CONNECTED or NOT", "");
                JSONObject object1 = JSONObject.fromObject(json1);
                write.write(object1.toString() + "\n");
                write.flush();
            }
        } catch (SocketException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showAlert(Alert.AlertType.ERROR, GP.getScene().getWindow(), "Server Error!", "Server is offline! Please try again later!");
                }
            });
        }catch(Exception e)
        {
            e.printStackTrace();
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
}
