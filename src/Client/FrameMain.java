package Client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

/**
 * Created by Lê Hoàng Phúc - 19127059
 */
public class FrameMain extends JFrame {

    private JButton btnFile;
    private JButton btnSend;
    private JScrollPane chatPanel;
    private JLabel lbReceiver = new JLabel(" ");
    private JPanel contentPane;
    private JTextField txtMessage;
    private JTextPane chatWindow;
    private String temp = "";
    JComboBox<String> onlineUsers = new JComboBox<String>();
    JList<String> onUser_ = new JList<String>();
    DefaultListModel<String> l1 = new DefaultListModel<>();
    private String username;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String currentcharUser = "Main Window";
    private HashMap<String, JTextPane> chatWindowofUsers = new HashMap<String, JTextPane>();
    Thread receiver;

    /**
     * Insert a file into chat pane.
     */
    private void performFileContent(String username, String filename, byte[] file, Boolean yourMessage) {

        StyledDocument doc;
        String window = null;
        if (username.equals(this.username)) {
            window = currentcharUser;
        } else {
            window = username;
        }
        doc = chatWindowofUsers.get(window).getStyledDocument();

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, new Color(84, 160, 229));
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_LEFT);
        } else {
            StyleConstants.setForeground(userStyle, Color.orange);
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
        }

        try {
            doc.insertString(doc.getLength(), username + ": ", userStyle);
            doc.setParagraphAttributes(doc.getLength(), 1, userStyle, false);

        } catch (BadLocationException e) {
        }

        Style linkStyle = doc.getStyle("Link style");
        if (linkStyle == null) {
            linkStyle = doc.addStyle("Link style", null);
            StyleConstants.setForeground(linkStyle, Color.PINK);
            StyleConstants.setUnderline(linkStyle, true);
            StyleConstants.setBold(linkStyle, true);
            linkStyle.addAttribute("link", new HyberlinkListener(filename, file));
        }

        if (chatWindowofUsers.get(window).getMouseListeners() != null) {
            // Mouse listener click to download
            chatWindowofUsers.get(window).addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Element ele = doc.getCharacterElement(chatWindow.viewToModel(e.getPoint()));
                    AttributeSet as = ele.getAttributes();
                    HyberlinkListener listener = (HyberlinkListener) as.getAttribute("link");
                    if (listener != null) {
                        listener.execute();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                }
            });
        }
        // Print File Link
        try {
            doc.insertString(doc.getLength(), "{{" + filename + "}}", linkStyle);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        // new Line
        try {
            doc.insertString(doc.getLength(), "\n", userStyle);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Menu Page
     */
    private void performMenu(JTextPane jtp) {

        StyledDocument doc = jtp.getStyledDocument();
        Style style = jtp.addStyle("I'm a Style", null);
        ;
        StyleConstants.setForeground(style, new Color(176, 176, 176));
        StyleConstants.setBold(style, true);
        StyleConstants.setFontSize(style, 30);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
        // Print sender
        try {
            doc.insertString(doc.getLength(), "                 Hello\nWelcome to my app", style);
            doc.setParagraphAttributes(doc.getLength(), 1, style, false);
        } catch (BadLocationException e) {
        }

    }

    /**
     * Insert a new message into chat pane.
     */
    private void performMessage(String username, String message, Boolean yourMessage) {

        StyledDocument doc;
        if (username.equals(this.username)) {
            doc = chatWindowofUsers.get(currentcharUser).getStyledDocument();
        } else {
            doc = chatWindowofUsers.get(username).getStyledDocument();
        }
        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }
        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, new Color(84, 160, 229));
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_LEFT);
        } else {
            StyleConstants.setForeground(userStyle, Color.orange);
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
        }
        // Print sender
        try {
            doc.insertString(doc.getLength(), username + ": ", userStyle);
            doc.setParagraphAttributes(doc.getLength(), 1, userStyle, false);
        } catch (BadLocationException e) {
        }

        Style messageStyle = doc.getStyle("Message style");
        if (messageStyle == null) {
            messageStyle = doc.addStyle("Message style", null);
            StyleConstants.setForeground(messageStyle, Color.WHITE);
            StyleConstants.setBold(messageStyle, false);
        }

        // Print Content
        try {
            doc.insertString(doc.getLength(), message + "\n", messageStyle);
        } catch (BadLocationException e) {
        }

    }

    /**
     * Create the frame.
     */
    public FrameMain(String username, DataInputStream dis, DataOutputStream dos) {
        this.username = username;
        this.dis = dis;
        this.dos = dos;
        receiver = new Thread(new Receiver(dis));
        receiver.start();
        //Frame
        //
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(230, 240, 247));
        setContentPane(contentPane);
//        Container con = this.getContentPane();
        // Setting con
        contentPane.setLayout(new BorderLayout());
        //top pannel
        //Top Pannel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel();
        titleLabel.setText("Chat App");
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
        jpEnd.add(Box.createRigidArea(new Dimension(0, 15)));
        jpEnd.add(jPaneltitle);
        jpEnd.add(Box.createRigidArea(new Dimension(0, 5)));
        JSeparator s = new JSeparator();
        s.setOrientation(SwingConstants.HORIZONTAL);
        topPanel.add(jpEnd);
        topPanel.add(s);
        //mid
        chatPanel = new JScrollPane();
        chatPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);

        JLabel headerContent = new JLabel("Chat App");
        headerContent.setFont(new Font("Monaco", Font.BOLD, 24));
        JPanel header = new JPanel();
        header.setBackground(new Color(255, 230, 230));
        header.add(headerContent);

        txtMessage = new JTextField();
        txtMessage.setEnabled(false);
        txtMessage.setColumns(10);
        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(255, 153, 153));
        chatPanel.setColumnHeaderView(usernamePanel);
        lbReceiver.setText(this.username);
        lbReceiver.setFont(new Font("Monaco", Font.BOLD, 16));
        usernamePanel.add(lbReceiver);

        chatWindowofUsers.put("Main", new JTextPane());
        chatWindow = chatWindowofUsers.get("Main");
        performMenu(chatWindow);
        chatWindow.setFont(new Font("Monaco", Font.PLAIN, 14));
        chatWindow.setEditable(false);
        chatWindow.setBackground(Color.BLACK);
        chatPanel.setViewportView(chatWindow);
        JPanel chatChatPanel = new JPanel();
        chatChatPanel.setLayout(new BoxLayout(chatChatPanel, BoxLayout.LINE_AXIS));
        chatChatPanel.add(Box.createRigidArea(new Dimension(105, 30)));
        chatChatPanel.add(txtMessage);
        chatChatPanel.add(Box.createRigidArea(new Dimension(90, 30)));
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.PAGE_AXIS));
        midPanel.add(chatPanel);
        midPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        //
        JPanel endPanel = new JPanel();
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.PAGE_AXIS));
        endPanel.add(chatChatPanel);
        //
        Dimension size = new Dimension(160, 35);
        btnSend = new JButton("Send");
        btnSend.setFont(new Font("Monaco", Font.BOLD, 18));
        btnSend.setAlignmentX(CENTER_ALIGNMENT);
        btnSend.setFocusable(false);
        btnSend.setBackground(new Color(255, 153, 153));
        btnSend.setForeground(Color.white);
        btnSend.setUI(new stylebutton());
        btnSend.setMaximumSize(size);
        btnSend.setEnabled(false);
        //
        btnFile = new JButton("File");
        btnFile.setFont(new Font("Monaco", Font.BOLD, 18));
        btnFile.setAlignmentX(CENTER_ALIGNMENT);
        btnFile.setFocusable(false);
        btnFile.setBackground(new Color(255, 153, 153));
        btnFile.setForeground(Color.white);
        btnFile.setUI(new stylebutton());
        btnFile.setMaximumSize(size);
        btnFile.setEnabled(false);
        //
        JPanel jbtn = new JPanel();
        jbtn.setLayout(new BoxLayout(jbtn, BoxLayout.LINE_AXIS));
        jbtn.add(btnSend);
        jbtn.add(Box.createRigidArea(new Dimension(30, 0)));
        jbtn.add(btnFile);
        //
        endPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        endPanel.add(jbtn);
        endPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton logOut = new JButton("Log out");
        logOut.setAlignmentX(CENTER_ALIGNMENT);
        JFrame logTemp = this;
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("#logout");
                    dos.flush();

                    try {
                        receiver.join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    if (dos != null) {
                        dos.close();
                    }
                    if (dis != null) {
                        dis.close();
                    }
                    logTemp.dispose();
                    LoginFrame.GUI();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        endPanel.add(logOut);
        endPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        //check box
        JPanel checkboxJJ = new JPanel();
        checkboxJJ.setLayout(new BoxLayout(checkboxJJ, BoxLayout.LINE_AXIS));
        JPanel checkboxJ = new JPanel();
        checkboxJ.setLayout(new BoxLayout(checkboxJ, BoxLayout.PAGE_AXIS));
        checkboxJ.setPreferredSize(new Dimension(105, 0));
        JLabel onU = new JLabel("Online Users");
        onU.setForeground(new Color(128, 51, 51));
        onU.setFont(new Font("Monaco", Font.BOLD, 13));
        onU.setAlignmentX(CENTER_ALIGNMENT);
        checkboxJ.add(onU);
        checkboxJ.add(onlineUsers);
        checkboxJ.add(Box.createRigidArea(new Dimension(0, 250)));
        checkboxJJ.add(checkboxJ);
        //list
        JPanel listJJ = new JPanel();
        listJJ.setLayout(new BoxLayout(listJJ, BoxLayout.LINE_AXIS));
        JPanel listJ = new JPanel();
        listJ.setLayout(new BoxLayout(listJ, BoxLayout.PAGE_AXIS));
        listJ.setPreferredSize(new Dimension(90, 0));
        JLabel onU1 = new JLabel("Online Users");
        onU1.setForeground(new Color(128, 51, 51));
        onU1.setFont(new Font("Monaco", Font.BOLD, 13));
        onU1.setAlignmentX(CENTER_ALIGNMENT);
        listJ.add(onU1);
        listJ.add(onUser_);
        listJ.add(Box.createRigidArea(new Dimension(0, 250)));
        listJJ.add(listJ);
        //container
        contentPane.add(topPanel, BorderLayout.PAGE_START);
        contentPane.add(checkboxJJ, BorderLayout.LINE_START);
        contentPane.add(listJJ, BorderLayout.LINE_END);
        contentPane.add(midPanel, BorderLayout.CENTER);
//        con.add(Box.createRigidArea(new Dimension(90,0)),BorderLayout.LINE_END);
        contentPane.add(endPanel, BorderLayout.PAGE_END);
        // Setting JFrame
        this.setSize(new Dimension(585, 451));
        this.setTitle("Chat App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        //
        btnFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform dialog file chooser
                JFileChooser fileChooser = new JFileChooser();
                int rVal = fileChooser.showOpenDialog(contentPane.getParent());
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    byte[] selectedFile = new byte[(int) fileChooser.getSelectedFile().length()];
                    BufferedInputStream bis;
                    try {
                        bis = new BufferedInputStream(new FileInputStream(fileChooser.getSelectedFile()));
                        // read content to selectedFile
                        bis.read(selectedFile, 0, selectedFile.length);

                        dos.writeUTF("#msgfile");
                        dos.writeUTF(currentcharUser);
                        dos.writeUTF(fileChooser.getSelectedFile().getName());
                        dos.writeUTF(String.valueOf(selectedFile.length));

                        int size = selectedFile.length;
                        int bufferSize = 2048;
                        int offset = 0;
                        // Send all buffer to end file
                        while (size > 0) {
                            dos.write(selectedFile, offset, Math.min(size, bufferSize));
                            offset += Math.min(size, bufferSize);
                            size -= bufferSize;
                        }
                        dos.flush();
                        bis.close();
                        JOptionPane.showMessageDialog(null, "Send file successfully");
                        //Print
                        performFileContent(username, fileChooser.getSelectedFile().getName(), selectedFile, true);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        onlineUsers.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    currentcharUser = (String) onlineUsers.getSelectedItem();
                    if (chatWindow != chatWindowofUsers.get(currentcharUser)) {
                        try {
                            if (!currentcharUser.equals("Main Window")) {
                                if (!temp.equals(currentcharUser)) {
                                    dos.writeUTF("@123" + username + "@" + currentcharUser);
                                    temp = currentcharUser;
                                }
                            }
                            if (currentcharUser.isBlank() || currentcharUser.equals("Main Window")) {
                                JTextPane jtpp = new JTextPane();
                                performMenu(jtpp);
                                chatWindowofUsers.put(currentcharUser,jtpp);
                            }
                            txtMessage.setText("");
                            chatWindow = chatWindowofUsers.get(currentcharUser);
                            chatWindow.setBackground(Color.BLACK);
                            chatPanel.setViewportView(chatWindow);
                            chatPanel.validate();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (currentcharUser.equals("Main Window")) {
                        btnSend.setEnabled(false);
                        btnFile.setEnabled(false);
                        txtMessage.setEnabled(false);
                    } else {
                        btnSend.setEnabled(true);
                        btnFile.setEnabled(true);
                        txtMessage.setEnabled(true);
                    }
                }

            }
        });
        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (txtMessage.getText().isBlank() || currentcharUser.isBlank()) {
                    btnSend.setEnabled(false);
                } else {
                    btnSend.setEnabled(true);
                }
            }
        });

        // Set action perform to send button.
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("#msgtext");
                    dos.writeUTF(currentcharUser);
                    dos.writeUTF(txtMessage.getText());
                    dos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    performMessage("ERROR", "Network error!", true);
                }

                // PrintMsg
                performMessage(username, txtMessage.getText(), true);
                txtMessage.setText("");
            }
        });

        this.getRootPane().setDefaultButton(btnSend);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                try {
                    dos.writeUTF("#logout");
                    dos.flush();

                    try {
                        receiver.join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    if (dos != null) {
                        dos.close();
                    }
                    if (dis != null) {
                        dis.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    /**
     * Receiver Thread
     */
    class Receiver implements Runnable {

        private DataInputStream dis;

        public Receiver(DataInputStream dis) {
            this.dis = dis;
        }

        @Override
        public void run() {
            try {

                while (true) {
                    // Response from server
                    String method = dis.readUTF();
                    //Text
                    if (method.equals("#msgtext")) {
                        String sender = dis.readUTF();
                        String message = dis.readUTF();
                        performMessage(sender, message, false);
                    } else if (method.equals("#msgfile")) {
                        // Nhận một file
                        String sender = dis.readUTF();
                        String filename = dis.readUTF();
                        int size = Integer.parseInt(dis.readUTF());
                        int bufferSize = 2048;
                        byte[] buffer = new byte[bufferSize];
                        ByteArrayOutputStream file = new ByteArrayOutputStream();

                        while (size > 0) {
                            dis.read(buffer, 0, Math.min(bufferSize, size));
                            file.write(buffer, 0, Math.min(bufferSize, size));
                            size -= bufferSize;
                        }

                        // print file
                        performFileContent(sender, filename, file.toByteArray(), false);

                    } else if (method.contains("#confirmchat")) {
                        String[] getUsr = method.split("@");
                        String sender = getUsr[1];
                        String receiver = getUsr[2];
                        int a = JOptionPane.showConfirmDialog(null,
                                "Are you sure to chat with " + sender + " ?");
                        if (a == JOptionPane.YES_OPTION) {
                            onlineUsers.setSelectedItem(sender);
                        }
                    }
                    //online user
                    else if (method.equals("#onlineusers")) {
                        String[] users = dis.readUTF().split(",");
                        onlineUsers.removeAllItems();
                        l1.removeAllElements();
                        String chatting = currentcharUser;
                        boolean isChattingOnline = false;
                        for (String user : users) {
                            if (user.equals(username) == false) {
                                //update online user
                                onlineUsers.addItem(user);
                                if (!user.equals("Main Window")) l1.addElement(user);
                                onUser_ = new JList<>(l1);
                                if (chatWindowofUsers.get(user) == null) {
                                    JTextPane temp = new JTextPane();
                                    temp.setFont(new Font("Arial", Font.PLAIN, 14));
                                    temp.setEditable(false);
                                    chatWindowofUsers.put(user, temp);
                                }
                            }
                            if (chatting.equals(user)) {
                                isChattingOnline = true;
                            }
                        }
                        if (isChattingOnline == false) {
                            //go back default window
                            onlineUsers.setSelectedItem("Main Window");
                            JOptionPane.showMessageDialog(null, chatting + " is offline!\nGo back to main window");
                        } else {
                            onlineUsers.setSelectedItem(chatting);
                        }
                        onlineUsers.validate();
                    } else if (method.equals("#leaving")) {
                        //leave the chat
                        break;
                    }

                }

            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                try {
                    if (dis != null) {
                        dis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Click to download File
     */
    class HyberlinkListener extends AbstractAction {
        String filename;
        byte[] file;

        public HyberlinkListener(String filename, byte[] file) {
            this.filename = filename;
            this.file = Arrays.copyOf(file, file.length);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            execute();
        }

        public void execute() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(filename));
            int rVal = fileChooser.showSaveDialog(contentPane.getParent());
            if (rVal == JFileChooser.APPROVE_OPTION) {

                // Open file to store
                File saveFile = fileChooser.getSelectedFile();
                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(saveFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Successfully download! File is in " + saveFile.getAbsolutePath());
                int nextAction = JOptionPane.showConfirmDialog(null, "Do you want to open this file?", "Successful", JOptionPane.YES_NO_OPTION);
                if (nextAction == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().open(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bos != null) {
                    try {
                        bos.write(this.file);
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}