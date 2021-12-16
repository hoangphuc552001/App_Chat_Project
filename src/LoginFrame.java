import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class LoginFrame extends JFrame implements ActionListener {
    private String host = "localhost";
    private int port = 3200;
    private Socket socket;

    private DataInputStream dis;
    private DataOutputStream dos;
    Container container = getContentPane();
    JLabel loginLabel = new JLabel("LOGIN");
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton backButton = new JButton("BACK");
    JCheckBox showPassword = new JCheckBox("Show Password");

    LoginFrame() {
        container.setLayout(null);
        //
        loginLabel.setFont(new Font("Courier", Font.BOLD, 25));
        container.add(loginLabel);
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(backButton);
        loginLabel.setBounds(160, 40, 100, 30);
        userLabel.setBounds(50, 80, 100, 30);
        passwordLabel.setBounds(50, 120, 100, 30);
        userTextField.setBounds(150, 80, 150, 30);
        passwordField.setBounds(150, 120, 150, 30);
        showPassword.setBounds(150, 150, 150, 30);
        loginButton.setBounds(200, 200, 100, 30);
        loginButton.addActionListener(this);
        backButton.setBounds(90, 200, 100, 30);
        backButton.addActionListener(this);
        showPassword.addActionListener(this);
        // Setting JFrame
        this.setTitle("Login");
        this.setVisible(true);
        this.setBounds(10, 10, 370, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(400, 300));
        this.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public static void GUI() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginFrame frame = new LoginFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(backButton)) {
            this.dispose();
            MenuFrame.GUI();
        } else if (e.getSource().equals(showPassword)) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        }else if(e.getSource().equals(loginButton)){
            String response = Login(userTextField.getText().toString(), passwordField.getText().toString());
            System.out.println(response);
            // đăng nhập thành công thì server sẽ trả về  chuỗi "Log in successful"
            if (response.equals("Log in successful") ) {
                JOptionPane.showMessageDialog(null,"Login successful. Welcome to my App Chat");
//                EventQueue.invokeLater(new Runnable() {
//                    public void run() {
//                        try {
//                            ChatFrame frame = new ChatFrame(username, dis, dos);
//                            frame.setVisible(true);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
                dispose();
            }
        }
    }
    /**
     * Connect to Server
     */
    public void Connect() {
        try {
            if (socket != null) {
                socket.close();
            }
            socket = new Socket(host, port);
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Gửi yêu cầu đăng nhập đến server
     * Trả về kết quả phản hồi từ server
     */
    public String Login(String username, String password) {
        try {
            Connect();

            dos.writeUTF("Log in");
            dos.writeUTF(username);
            dos.writeUTF(password);
            dos.flush();

            String response = dis.readUTF();
            return response;

        } catch (IOException e) {
            e.printStackTrace();
            return "Network error: Log in fail";
        }
    }

}
