package Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Created by Lê Hoàng Phúc - 19127059
 */
public class Server {
    /**
     * properties
     */
    private ServerSocket serverSocket;
    private Socket socket;
    public static Vector<Handler> clients = new Vector<Handler>();
    private String dataAccountFile = "useraccount.txt";
    /**
     * methods
     */
    /**
     * get account to file to check user
     //     */
    private void getAccountFromFile() {
        File f = new File(dataAccountFile);
        if (f.exists())
            try {
                BufferedReader br = new BufferedReader(new FileReader(f.getName()));
                String line = br.readLine();
                while (line != null) {
                    clients.add(new Handler(line.split("@")[0], line.split("@")[1],false));
                    line = br.readLine();
                }
                br.close();

            } catch (Exception e) {
                e.printStackTrace();

            }
    }

    /**
     * Save account to file
     */
    private void saveAccountToFile(String username,String password) throws IOException {
        FileWriter writer = new FileWriter(dataAccountFile,true);
        BufferedWriter buffer = new BufferedWriter(writer);
        buffer.write(username+"@"+password+"\n");
        buffer.close();
    }

    /**
     * Update online users
     */
    public static void updateOnlineUsers() {
        String message = " ";
        for (Handler client : clients) {
            if (client.getIsLoggedIn() == true) {
                message += ",";
                message += client.getUsername();
            }
        }
        for (Handler client : clients) {
            if (client.getIsLoggedIn() == true) {
                try {
                    client.getDostream().writeUTF("#onlineusers");
                    client.getDostream().writeUTF(message);
                    client.getDostream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Server() throws IOException {
        DataInputStream distream=null;
        DataOutputStream dostream=null;
        try {
            // Update account list from file
            this.getAccountFromFile();
            // process server
            serverSocket = new ServerSocket(3200);
            while (true) {
                // wait request from clients
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                distream = new DataInputStream(is);
                dostream= new DataOutputStream(os);
                // process signin signup
                String request = distream.readUTF();
                if (request.equals("#signup")) {
                    String username = distream.readUTF();
                    String password = distream.readUTF();
                    //Check exist user by username and password
                    if (isExistedUser(username) == false) {
                        //add client to array storing
                        Handler newHandler = new Handler(socket, username, password, false);
                        clients.add(newHandler);
                        //Save account
                        this.saveAccountToFile(username,password);
                        dostream.writeUTF("#signupsuccessful");
                        dostream.flush();
                    } else {
                        dostream.writeUTF("#alreadyused");
                        dostream.flush();
                    }
                } else if (request.equals("#login")) {
                    String username = distream.readUTF();
                    String password = distream.readUTF();
                    //Check exist user by username and password
                    if (isExistedUser(username) == true) {
                        for (Handler client : clients) {
                            if (client.getUsername().equals(username)) {
                                // Check Password
                                if (password.equals(client.getPassword())) {
                                    ServerFrame.upDateUserOnline(1);
                                    ServerFrame.txtMessage.append(username + " joined chat app\n");
                                    // Set object handler when login successfull
                                    Handler newHandler = client;
                                    newHandler.setSocket(socket);
                                    newHandler.setIsLoggedIn(true);
                                    // announce user
                                    dostream.writeUTF("#logok");
                                    dostream.flush();
                                    // Create thread for user to send and receive message
                                    new Thread(newHandler).start();
                                    // Update user online
                                    updateOnlineUsers();
                                } else {
                                    dostream.writeUTF("#incorrectpw");
                                    dostream.flush();
                                }
                                break;
                            }
                        }
                    } else {
                        dostream.writeUTF("#incorrectpw");
                        dostream.flush();
                    }
                }else if (request.equals("#stopserver")){
                    if (serverSocket != null) {
                        serverSocket.close();
                        distream.close();
                        dostream.close();
                    }
                }
            }

        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                distream.close();
                dostream.close();
            }
        }
    }

    /**
     * Check User Function
     */
    public boolean isExistedUser(String name) {
        for (Handler client : clients) {
            if (client.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

}

/**
 * Thread from server
 */
class Handler implements Runnable {
    private Socket socket;
    private DataInputStream distream;
    private DataOutputStream dostream;
    private String username;
    private String password;
    private boolean isLoggedIn;

    public Handler(Socket socket, String username, String password, boolean isLoggedIn) throws IOException {
        this.socket = socket;
        this.username = username;
        this.password = password;
        this.distream = new DataInputStream(socket.getInputStream());
        this.dostream = new DataOutputStream(socket.getOutputStream());
        this.isLoggedIn = isLoggedIn;
    }

    public Handler(String username, String password, boolean isLoggedIn) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }

    public void setIsLoggedIn(boolean IsLoggedIn) {
        this.isLoggedIn = IsLoggedIn;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.distream = new DataInputStream(socket.getInputStream());
            this.dostream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close socket
     */
    public void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
                distream.close();
                dostream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public DataOutputStream getDostream() {
        return this.dostream;
    }
    public DataInputStream getDistream() {
        return this.distream;
    }
    @Override
    public void run() {

        while (true) {
            try {
                String message = null;
                message = distream.readUTF();
                if (message.equals("#logout")) {
                    ServerFrame.upDateUserOnline(2);
                    ServerFrame.txtMessage.append(username + " leaved chat app\n");
                    dostream.writeUTF("#leaving");
                    dostream.flush();
                    closeSocket();
                    this.isLoggedIn = false;
                    Server.updateOnlineUsers();
                    break;
                }
                else if (message.equals("#msgtext")) {
                    String receiver = distream.readUTF();
                    String content = distream.readUTF();
                    Lock lock = new ReentrantLock();
                    for (Handler client : Server.clients) {
                        if (client.getUsername().equals(receiver)) {
                            lock.lock();
                            try {
                                client.getDostream().writeUTF("#msgtext");
                                client.getDostream().writeUTF(this.username);
                                client.getDostream().writeUTF(content);
                                client.getDostream().flush();
                                break;
                            } finally {
                                lock.unlock();
                            }
                        }
                    }
                }
                else if (message.equals("#msgfile")) {
                    String receiver = distream.readUTF();
                    String filename = distream.readUTF();
                    int size = Integer.parseInt(distream.readUTF());
                    int bufferSize = 2048;
                    byte[] buffer = new byte[bufferSize];
                    Lock lock = new ReentrantLock();
                    for (Handler client : Server.clients) {
                        if (client.getUsername().equals(receiver)) {
                            lock.lock();
                            try{
                                client.getDostream().writeUTF("#msgfile");
                                client.getDostream().writeUTF(this.username);
                                client.getDostream().writeUTF(filename);
                                client.getDostream().writeUTF(String.valueOf(size));
                                while (size > 0) {
                                    distream.read(buffer, 0, Math.min(size, bufferSize));
                                    client.getDostream().write(buffer, 0, Math.min(size, bufferSize));
                                    size -= bufferSize;
                                }
                                client.getDostream().flush();
                                break;}
                            finally {
                                lock.unlock();
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}