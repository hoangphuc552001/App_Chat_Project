import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class LoginFrame extends JFrame implements ActionListener {
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
            String username=userTextField.getText().toString();
            String password=passwordField.getText().toString();
            if (AccountUser.checkUserPW(username,password)){
                JOptionPane.showMessageDialog(null,"Login Successful");
            }else{
                JOptionPane.showMessageDialog(null,"Incorrect user or password","Alert",JOptionPane.WARNING_MESSAGE);
                userTextField.setText("");
                passwordField.setText("");
            }
        }
    }

}
