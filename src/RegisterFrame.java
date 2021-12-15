import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class RegisterFrame extends JFrame implements ActionListener {
    Container container = getContentPane();
    JLabel register = new JLabel("REGISTER");
    JLabel name = new JLabel("Name");
    JLabel username = new JLabel("Username");
    JLabel password = new JLabel("Password");
    JLabel cfpassword = new JLabel("Password Again");
    JTextField usernameTextField = new JTextField();
    JTextField nameTextField = new JTextField();
    JPasswordField passwordTextField = new JPasswordField();
    JPasswordField cfpasswordField = new JPasswordField();
    JButton registerButton = new JButton("REGISTER");
    JButton backButton = new JButton("BACK");
    JCheckBox showPassword = new JCheckBox("Show Password");
    RegisterFrame() {
        container.setLayout(null);
        //
        register.setFont(new Font("Courier", Font.BOLD, 25));
        container.add(register);
        container.add(name);
        container.add(username);
        container.add(nameTextField);
        container.add(usernameTextField);
        container.add(password);
        container.add(cfpassword);
        container.add(passwordTextField);
        container.add(cfpasswordField);
        container.add(showPassword);
        container.add(registerButton);
        container.add(backButton);
        register.setBounds(160, 40, 150, 30);
        name.setBounds(50, 100, 100, 30);
        nameTextField.setBounds(150, 100, 150, 30);
        username.setBounds(50, 140, 100, 30);
        usernameTextField.setBounds(150, 140, 150, 30);
        password.setBounds(50, 180, 100, 30);
        passwordTextField.setBounds(150, 180, 150, 30);
        cfpassword.setBounds(50, 220, 100, 30);
        cfpasswordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 260, 150, 30);
        registerButton.setBounds(210, 300, 100, 30);
        registerButton.addActionListener(this);
        backButton.setBounds(100, 300, 100, 30);
        backButton.addActionListener(this);
        showPassword.addActionListener(this);

        // Setting JFrame
        this.setTitle("Register");
        this.setVisible(true);
        this.setBounds(10, 10, 370, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(400, 400));
        this.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public static void GUI() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RegisterFrame frame = new RegisterFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(backButton)){
            this.dispose();
            MenuFrame.GUI();
        }
        else if (e.getSource().equals(showPassword)) {
            if (showPassword.isSelected()) {
                passwordTextField.setEchoChar((char) 0);
                cfpasswordField.setEchoChar((char)0);
            } else {
                passwordTextField.setEchoChar('*');
                cfpasswordField.setEchoChar('*');
            }
        }
        else if (e.getSource().equals(registerButton)){
            if (usernameTextField.getText().toString().isBlank()
            ||nameTextField.getText().toString().isBlank()
            ||passwordTextField.getText().toString().isBlank()
            ||cfpasswordField.getText().toString().isBlank()){
                JOptionPane.showMessageDialog(null,"Blank Input.","Alert",JOptionPane.WARNING_MESSAGE);
            }
            else if (!passwordTextField.getText().toString().equals(cfpasswordField.getText().toString())){
                JOptionPane.showMessageDialog(null,"Password is not fit.","Alert",JOptionPane.WARNING_MESSAGE);
            }else if(AccountUser.checkExistUser(usernameTextField.getText().toString())==false){
                JOptionPane.showMessageDialog(null,"Account Existed.","Alert",JOptionPane.WARNING_MESSAGE);
            }
            else{
                BufferedWriter buffer = null;
                try {
                    buffer = new BufferedWriter(new FileWriter("useraccount.txt",true));
                    String config = nameTextField.getText().toString()
                            + "@" + usernameTextField.getText().toString()
                            + "@" + passwordTextField.getText().toString()
                            +"\n";
                    buffer.write(config);
                    buffer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null,"Registered Successfully.");
                nameTextField.setText("");
                usernameTextField.setText("");
                passwordTextField.setText("");
                cfpasswordField.setText("");
            }
        }
    }

}
