import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static String host = "localhost";
    public static int port = 3200;
    public static Socket socket;

    public static DataInputStream dis;
    public static DataOutputStream dos;
    /**
     * Connect to Server
     */
    public static void Connect() {
        String[] fi_=new String[2];
        try {
            if (socket != null) {
                socket.close();
            }
            socket = new Socket(host, port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Can not connect to database!","Connected Fail",JOptionPane.ERROR_MESSAGE);
        }
    }
}
