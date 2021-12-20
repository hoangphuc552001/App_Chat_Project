package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
/**
 * Created by Lê Hoàng Phúc - 19127059
 */
public class MenuFrame extends JFrame implements ActionListener{
    JButton btnLogin,btnRegister;
    MenuFrame(){
        stylepanel spn=new stylepanel();
        Container container1 = this.getContentPane();
        Container container=new Container();
        Dimension size = new Dimension(160, 25);
        //Top Pannel
        JPanel topPanel=new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel();
        titleLabel.setText("MENU");
        titleLabel.setForeground(new Color(222, 52, 49));
        titleLabel.setFont(new Font("Monaco", Font.PLAIN, 30));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setPreferredSize(new Dimension(300,100));
        topPanel.add(titleLabel);
        JPanel jPaneltitle=new JPanel();
        jPaneltitle.setSize((new Dimension(50, 50)));
        jPaneltitle.setLayout(new BoxLayout(jPaneltitle, BoxLayout.LINE_AXIS));
        jPaneltitle.add(titleLabel);
        jPaneltitle.setBackground(new Color(247, 245, 245));
        jPaneltitle.setAlignmentX(CENTER_ALIGNMENT);
        //
        JPanel jpEnd=new JPanel();
        jpEnd.setLayout(new BoxLayout(jpEnd, BoxLayout.PAGE_AXIS));
        jpEnd.add(jPaneltitle);
        jpEnd.add(Box.createRigidArea(new Dimension(0, 5)));
        jpEnd.setBackground(new Color(147, 176, 194));
        jpEnd.setAlignmentX(CENTER_ALIGNMENT);
        topPanel.add(jpEnd);
        topPanel.setAlignmentX(CENTER_ALIGNMENT);
        //topPanel.add(jlb);
        //mid pannel
        btnLogin=new JButton("LOGIN");
        btnLogin.setFont(new Font("Monaco", Font.BOLD, 18));
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.setFocusable(false);
        btnLogin.setBackground(new Color(245, 66, 87));
        btnLogin.setForeground(Color.white);
        btnLogin.setUI(new stylebutton());
        btnLogin.setPreferredSize(new Dimension(300,50));
        btnLogin.setMaximumSize(size);
        //
        btnRegister=new JButton("REGISTER");
        btnRegister.setAlignmentX(CENTER_ALIGNMENT);
        btnRegister.setFont(new Font("Monaco", Font.BOLD, 18));
        btnRegister.setFocusable(false);
        btnRegister.setBackground(new Color(245, 66, 87));
        btnRegister.setForeground(Color.white);
        btnRegister.setUI(new stylebutton());
        btnRegister.setPreferredSize(new Dimension(300,50));
        btnRegister.setMaximumSize(size);

        // Add to container
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(topPanel);
        container.add((Box.createRigidArea(new Dimension(0,50))));
        container.add(btnLogin);
        container.add((Box.createRigidArea(new Dimension(0,10))));
        container.add(btnRegister);
        container.add((Box.createRigidArea(new Dimension(0,30))));
        //
        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);
        //
        spn.setLayout(new BorderLayout());
        spn.add(container,BorderLayout.CENTER);
        container1.add(spn);
        // Setting Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Menu");
        this.setVisible(true);
        this.setSize(350, 250);
        this.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }
    public static void GUI(){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MenuFrame frame = new MenuFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Action Event
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnLogin)){
            this.dispose();
            LoginFrame.GUI();
        }
        else if (e.getSource().equals(btnRegister)){
            this.dispose();
            RegisterFrame.GUI();
        }
    }
    public static void main(String[] args) {
        MenuFrame.GUI();
    }
}