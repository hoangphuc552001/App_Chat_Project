package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    public static ServerSocket serverSocket;
    private Socket socket;
    static Vector<Handler> clients = new Vector<Handler>();
    private String dataAccountFile = "useraccount.txt";

    private void loadAccounts() {
        File f = new File(dataAccountFile);
        if (f.exists())
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataAccountFile), "utf8"));

                String info = br.readLine();
                while (info != null && !(info.isEmpty())) {
                    clients.add(new Handler(info.split(",")[0], info.split(",")[1],false));
                    info = br.readLine();
                }

                br.close();

            } catch (Exception e) {
                e.printStackTrace();

            }
    }

    /**
     * Lưu danh sách tài khoản xuống file
     */
    private void saveAccounts() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(dataAccountFile), "utf8");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        for (Handler client : clients) {
            pw.print(client.getUsername() + "," + client.getPassword() + "\n");
        }
        pw.println("");
        if (pw != null) {
            pw.close();
        }
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
        try {
            // Object dùng để synchronize cho việc giao tiếp với các người dùng

            // Đọc danh sách tài khoản đã đăng ký
            this.loadAccounts();
            // Socket dùng để xử lý các yêu cầu đăng nhập/đăng ký từ user
            serverSocket = new ServerSocket(3200);

            while (true) {
                // Đợi request đăng nhập/đăng xuất từ client
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                DataInputStream dis = new DataInputStream(is);
                DataOutputStream dos = new DataOutputStream(os);
                // Đọc yêu cầu đăng nhập/đăng xuất
                String request = dis.readUTF();
                if (request.equals("Sign up")) {
                    // Yêu cầu đăng ký từ user

                    String username = dis.readUTF();
                    String password = dis.readUTF();

                    // Kiểm tra tên đăng nhập đã tồn tại hay chưa
                    if (isExisted(username) == false) {
                        // Tạo một Handler để giải quyết các request từ user này
                        Handler newHandler = new Handler(socket, username, password, false);
                        clients.add(newHandler);
                        // Lưu danh sách tài khoản xuống file và gửi thông báo đăng nhập thành công cho user
                        this.saveAccounts();
                        dos.writeUTF("Sign up successful");
                        dos.flush();
                        // Gửi thông báo cho các client đang online cập nhật danh sách người dùng trực tuyến
                        //updateOnlineUsers();
                    } else {
                        // Thông báo đăng nhập thất bại
                        dos.writeUTF("This username is being used");
                        dos.flush();
                    }
                } else if (request.equals("Log in")) {
                    // Yêu cầu đăng nhập từ user
                    ServerFrame.upDateUserOnline(1);
                    String username = dis.readUTF();
                    String password = dis.readUTF();
                    ServerFrame.txtMessage.append(username + " joined chat app\n");
                    // Kiểm tra tên đăng nhập có tồn tại hay không
                    if (isExisted(username) == true) {
                        for (Handler client : clients) {

                            if (client.getUsername().equals(username)) {
                                // Kiểm tra mật khẩu có trùng khớp không
                                if (password.equals(client.getPassword())) {
                                    // Tạo Handler mới để giải quyết các request từ user này
                                    Handler newHandler = client;
                                    newHandler.setSocket(socket);
                                    newHandler.setIsLoggedIn(true);
                                    // Thông báo đăng nhập thành công cho người dùng
                                    dos.writeUTF("Log in successful");
                                    dos.flush();
                                    // Tạo một Thread để giao tiếp với user này
                                    Thread t = new Thread(newHandler);
                                    t.start();
                                    // Gửi thông báo cho các client đang online cập nhật danh sách người dùng trực tuyến
                                    updateOnlineUsers();
                                } else {
                                    dos.writeUTF("Password is not correct");
                                    dos.flush();
                                }
                                break;
                            }
                        }
                    } else {
                        dos.writeUTF("Password is not correct");
                        dos.flush();
                    }
                }
            }

        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    /**
     * Kiểm tra username đã tồn tại hay chưa
     */
    public boolean isExisted(String name) {
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
