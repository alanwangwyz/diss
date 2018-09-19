//Yizhou Wang
//669026
//DS project1
package Client;

import javafx.scene.control.TextArea;
import java.net.*;
import java.io.*;

public class check extends Thread{
    private Socket socket;
    private BufferedWriter write;
    private TextArea msg;

    public check(Socket socket, BufferedWriter write, TextArea msg)
    {
        this.socket = socket;
        this.write = write;
        this.msg = msg;

    }

    @Override
    public void run() {
        try {
            final String socketContent = "CONNECTED or NOT";
            write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            while (true) {
                Thread.sleep(3 * 1000);
                write.write(socketContent+"\n");
                write.flush();
            }
        } catch (SocketException e) {
            msg.setText("Server is offline! Please restart the server or try later!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
