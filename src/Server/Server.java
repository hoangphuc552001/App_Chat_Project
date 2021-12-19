package Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
     * Gửi yêu cầu các user đang online cập nhật lại danh sách người dùng trực tuyến
     * Được gọi mỗi khi có 1 user online hoặc offline
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
                    client.getDos().writeUTF("Online users");
                    client.getDos().writeUTF(message);
                    client.getDos().flush();
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
                        dostream.writeUTF("Password is not correct");
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
 * Luồng riêng dùng để giao tiếp với mỗi user
 */
class Handler implements Runnable {
    // Object để synchronize các hàm cần thiết
    // Các client đều có chung object này được thừa hưởng từ chính server
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String username;
    private String password;
    private boolean isLoggedIn;

    public Handler(Socket socket, String username, String password, boolean isLoggedIn) throws IOException {
        this.socket = socket;
        this.username = username;
        this.password = password;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
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
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Đóng socket kết nối với client
     * Được gọi khi người dùng offline
     */
    public void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
                dis.close();
                dos.close();
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

    public DataOutputStream getDos() {
        return this.dos;
    }

    @Override
    public void run() {

        while (true) {
            try {
                String message = null;

                // Đọc yêu cầu từ user
                message = dis.readUTF();
                // Yêu cầu đăng xuất từ user
                if (message.equals("Log out")) {
                    ServerFrame.upDateUserOnline(2);
                    ServerFrame.txtMessage.append(username + " leaved chat app\n");
                    // Thông báo cho user có thể đăng xuất
                    dos.writeUTF("Safe to leave");
                    dos.flush();

                    // Đóng socket và chuyển trạng thái thành offline
                    closeSocket();
                    this.isLoggedIn = false;

                    // Thông báo cho các user khác cập nhật danh sách người dùng trực tuyến
                    Server.updateOnlineUsers();
                    break;
                }

                // Yêu cầu gửi tin nhắn dạng văn bản
                else if (message.equals("Text")) {
                    String receiver = dis.readUTF();
                    String content = dis.readUTF();
                    Lock lock = new ReentrantLock();
                    for (Handler client : Server.clients) {
                        if (client.getUsername().equals(receiver)) {
                            lock.lock();
                            try {
                                client.getDos().writeUTF("Text");
                                client.getDos().writeUTF(this.username);
                                client.getDos().writeUTF(content);
                                client.getDos().flush();
                                break;
                            } finally {
                                lock.unlock();
                            }
                        }
                    }
                }

                // Yêu cầu gửi File
                else if (message.equals("File")) {

                    // Đọc các header của tin nhắn gửi file
                    String receiver = dis.readUTF();
                    String filename = dis.readUTF();
                    int size = Integer.parseInt(dis.readUTF());
                    int bufferSize = 2048;
                    byte[] buffer = new byte[bufferSize];
                    Lock lock = new ReentrantLock();
                    for (Handler client : Server.clients) {
                        if (client.getUsername().equals(receiver)) {
                            lock.lock();
                            try{
                                client.getDos().writeUTF("File");
                                client.getDos().writeUTF(this.username);
                                client.getDos().writeUTF(filename);
                                client.getDos().writeUTF(String.valueOf(size));
                                while (size > 0) {
                                    // Gửi lần lượt từng buffer cho người nhận cho đến khi hết file
                                    dis.read(buffer, 0, Math.min(size, bufferSize));
                                    client.getDos().write(buffer, 0, Math.min(size, bufferSize));
                                    size -= bufferSize;
                                }
                                client.getDos().flush();
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