//Yizhou Wang
//669026
//DS project1
package Client;

import java.io.BufferedReader;
import java.net.SocketException;
import javafx.scene.control.TextArea;

public class MessageListener extends Thread {

    private BufferedReader reader;
    private TextArea msg;

    public MessageListener(BufferedReader reader, TextArea msg) {
        this.reader = reader;
        this.msg = msg;
        }

        @Override
        public void run() {

        try {
            String line;
            while((line = reader.readLine()) != null) {
                line = line.replaceAll("#","\n");
                msg.setText(line);
            }

        } catch (SocketException e) {
            msg.setText("Socket closed! Please restart it later");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

