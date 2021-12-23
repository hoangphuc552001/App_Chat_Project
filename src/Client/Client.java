package Client;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
/**
 * Created by Lê Hoàng Phúc - 19127059
 */
public class Client {
    public static String localhost = "localhost";
    public static int port = 3200;
    public static Socket socket;
    public static DataInputStream distream;
    public static DataOutputStream dostream;
    /**
     * Connect to Server
     */
    public static void Connect() {
        String[] fi_=new String[2];
        try {
            if (socket != null) {
                socket.close();
            }
            socket = new Socket(localhost, port);
            distream = new DataInputStream(socket.getInputStream());
            dostream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Can not connect to database!","Connected Fail",JOptionPane.ERROR_MESSAGE);
        }
    }
}