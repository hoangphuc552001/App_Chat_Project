package Server;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerFrame extends JFrame implements ActionListener {
    JButton btnBack, btnStart, btnStop;
    public static int port = 3200;
    JTextField portnameTextfield, servernameTextfield;
    public static JLabel user_Count_, status_;
    public static JTextArea txtMessage;
    public static int totalAllUser=0;
    Thread t;
    /**
     * default constructor
     *
     * @throws Exception
     */
    public ServerFrame() throws Exception {
        Container container = this.getContentPane();
        //Top Pannel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel();
        titleLabel.setText("SERVER");
        titleLabel.setForeground(Color.white);
        titleLabel.setFont(new Font("Monaco", Font.PLAIN, 35));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        topPanel.add(titleLabel);

        //
        JPanel jPaneltitle = new JPanel();
        jPaneltitle.setSize((new Dimension(50, 50)));
        jPaneltitle.setLayout(new BoxLayout(jPaneltitle, BoxLayout.LINE_AXIS));
        jPaneltitle.setBackground(new Color(222, 52, 49));
        jPaneltitle.add(titleLabel);
        //
        JPanel jpEnd = new JPanel();
        jpEnd.setLayout(new BoxLayout(jpEnd, BoxLayout.PAGE_AXIS));
        jpEnd.add(Box.createRigidArea(new Dimension(0, 30)));
        jpEnd.add(jPaneltitle);
        jpEnd.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(jpEnd);
        //Mid Pannel
        //left
        JPanel leftMidPannel = new JPanel();
        leftMidPannel.setLayout(new BoxLayout(leftMidPannel, BoxLayout.PAGE_AXIS));
        JPanel serverMidPannel = new JPanel();
        serverMidPannel.setLayout(new BoxLayout(serverMidPannel, BoxLayout.LINE_AXIS));
        JLabel servername = new JLabel("Server:");
        servername.setFont(new Font("Monaco", Font.PLAIN, 17));
        servernameTextfield = new JTextField();
        servernameTextfield.setText("localhost");
        servernameTextfield.setEditable(false);
        servernameTextfield.setFont(new Font("Monaco", Font.PLAIN, 15));
        serverMidPannel.add(servername);
        serverMidPannel.add(servernameTextfield);
        serverMidPannel.add(Box.createRigidArea(new Dimension(500, 0)));
        JPanel portMidPannel = new JPanel();
        portMidPannel.setLayout(new BoxLayout(portMidPannel, BoxLayout.LINE_AXIS));
        JLabel portname = new JLabel("Port:");
        portname.setFont(new Font("Monaco", Font.PLAIN, 17));
        portnameTextfield = new JTextField();
        portnameTextfield.setText("3200");
        portnameTextfield.setFont(new Font("Monaco", Font.BOLD, 20));
        portnameTextfield.setEnabled(false);
        portMidPannel.add(portname);
        portMidPannel.add(Box.createRigidArea(new Dimension(19, 0)));
        portMidPannel.add(portnameTextfield);
        portMidPannel.add(Box.createRigidArea(new Dimension(499, 0)));
        leftMidPannel.add(serverMidPannel);
        leftMidPannel.add(portMidPannel);
        topPanel.add(leftMidPannel);
        //midpannelll
        JPanel midPP = new JPanel();
        midPP.setLayout(new BoxLayout(midPP, BoxLayout.PAGE_AXIS));
        JPanel statusPP = new JPanel();
        statusPP.setLayout(new BoxLayout(statusPP, BoxLayout.LINE_AXIS));
        JLabel status = new JLabel("Status: ");
        status.setFont(new Font("Monaco", Font.PLAIN, 15));
        status_ = new JLabel("STOP");
        status_.setForeground(new Color(211, 15, 15));
        status_.setFont(new Font("Monaco", Font.PLAIN, 16));
        statusPP.add(status);
        statusPP.add(status_);
        JPanel userCount = new JPanel();
        userCount.setLayout(new BoxLayout(userCount, BoxLayout.LINE_AXIS));
        JLabel user_Count = new JLabel("Online User: ");
        user_Count.setFont(new Font("Monaco", Font.PLAIN, 15));
        user_Count_ = new JLabel("0");
        user_Count_.setFont(new Font("Monaco", Font.PLAIN, 16));
        userCount.add(user_Count);
        userCount.add(user_Count_);
        midPP.add(statusPP);
        midPP.add(userCount);
        midPP.add(Box.createRigidArea(new Dimension(0, 15)));
        //
        JPanel panel_Chat = new JPanel();
        panel_Chat.setBorder(
                new TitledBorder(null, "List User", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        txtMessage = new JTextArea();
        txtMessage.setBackground(Color.BLACK);
        txtMessage.setForeground(Color.WHITE);
        txtMessage.setFont(new Font("Monaco", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(txtMessage);
        panel_Chat.add(scrollPane);
        panel_Chat.setBounds(31, 417, 758, 293);
        panel_Chat.setLayout(new GridLayout(0, 1, 0, 0));
        midPP.add(panel_Chat);
        //
        JPanel buttonPN = new JPanel();
        buttonPN.setLayout(new BoxLayout(buttonPN, BoxLayout.LINE_AXIS));
        btnStart = new JButton("START");
        btnStop = new JButton("STOP");
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        buttonPN.add(btnStart);
        buttonPN.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPN.add(btnStop);
        midPP.add(buttonPN);
        //Bottom Pannel
        JPanel bottomPanel = new JPanel();
        btnBack = new JButton("EXIT");
        btnBack.addActionListener(this);
        btnBack.setFocusable(false);
        btnBack.setAlignmentX(CENTER_ALIGNMENT);
        btnBack.setBackground(Color.white);
        btnBack.setForeground(new Color(222, 52, 49));
        bottomPanel.add(btnBack);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        // Add to container
        container.setLayout(new BorderLayout());
        container.add(topPanel, BorderLayout.PAGE_START);
        container.add(midPP, BorderLayout.CENTER);
        container.add(Box.createRigidArea(new Dimension(30, 0)), BorderLayout.LINE_START);
        container.add(Box.createRigidArea(new Dimension(30, 0)), BorderLayout.LINE_END);
        container.add(bottomPanel, BorderLayout.PAGE_END);
        // Setting JFrame
        this.setTitle("List Slang Words");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(670, 680);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    /**
     * Event dispatch thread
     */
    public static void GUI() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerFrame frame = new ServerFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * update user online
     */
    public static void upDateUserOnline(int type){
        if (type==1) user_Count_.setText(String.valueOf(++totalAllUser));
        else user_Count_.setText(String.valueOf(--totalAllUser));
    }
    /**
     * set action listener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == btnBack) {
            this.dispose();
        } else if (e.getSource().equals(btnStart)) {
            try {
                t = new Thread(){
                    public void run() {
                        try {
                            new Server();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                t.start();
                port = Integer.valueOf(portnameTextfield.getText()).intValue();
                status_.setText("RUNNING");
                status_.setForeground(new Color(14, 152, 70));
                txtMessage.append("Server is starting on port " + port+"\n");
                btnStop.setEnabled(true);
                btnStart.setEnabled(false);
            } catch (Exception e1) {
                txtMessage.append("ERROR");
                e1.printStackTrace();
            }
        } else if (e.getSource().equals(btnStop)) {
            user_Count_.setText("0");
            try {
                Server.serverSocket.close();
                txtMessage.append("Stop Server\n");
                status_.setText("OFF");
                status_.setForeground(new Color(211, 15, 15));
                btnStop.setEnabled(false);
                btnStart.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                txtMessage.append("Stop Server");
                status_.setText("OFF");
                status_.setForeground(new Color(211, 15, 15));
                btnStop.setEnabled(false);
                btnStart.setEnabled(true);
            }
        }
    }
    public static void updateNumberClient() {
        int number = Integer.parseInt(user_Count_.getText());
        user_Count_.setText(Integer.toString(number + 1));
    }

    public static void main(String[] args) {
        ServerFrame.GUI();
    }

}