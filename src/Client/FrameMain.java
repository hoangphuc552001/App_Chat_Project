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
    private JButton btnFile, btnSend, btnDown;
    private JScrollPane chatPanel;
    private JLabel lbReceiver = new JLabel(" ");
    private JPanel contentPane;
    private JTextField txtMessage;
    private JTextPane chatWindow;
    JComboBox<String> onlineUsers = new JComboBox<String>();
    private HashMap<String, byte[]> fileContain = new HashMap<String, byte[]>();
    private String username;
    private DataInputStream dis;
    private DataOutputStream dos;
    JList<String> onUser_ = new JList<String>();
    DefaultListModel<String> l1 = new DefaultListModel<>();
    private HashMap<String, JTextPane> chatWindowofUsers = new HashMap<String, JTextPane>();
    String tempVal = "";
    Thread receiver;

    /**
     * Menu Page
     */
    private void performMenu(JTextPane jtp) {
        StyledDocument doc = jtp.getStyledDocument();
        Style style = jtp.addStyle("I'm a Style", null);
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
            doc = chatWindowofUsers.get(lbReceiver.getText()).getStyledDocument();
        } else {
            doc = chatWindowofUsers.get(username).getStyledDocument();
        }

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, new Color(159, 146, 7));
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_LEFT);
        } else {
            StyleConstants.setForeground(userStyle, new Color(154, 24, 95));
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
        }
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
        try {
            doc.insertString(doc.getLength(), message + "\n", messageStyle);
        } catch (BadLocationException e) {
        }


    }
    private void performEmoij(String username, String emoji, Boolean yourMessage) {

        StyledDocument doc;
        if (username.equals(this.username)) {
            doc = chatWindowofUsers.get(lbReceiver.getText()).getStyledDocument();
        } else {
            doc = chatWindowofUsers.get(username).getStyledDocument();
        }

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, new Color(154, 24, 95));
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
        } else {
            StyleConstants.setForeground(userStyle, new Color(159, 146, 7));
            StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_LEFT);
        }
        try { doc.insertString(doc.getLength(), username + ": ", userStyle); }
        catch (BadLocationException e){}

        Style iconStyle = doc.getStyle("Icon style");
        if (iconStyle == null) {
            iconStyle = doc.addStyle("Icon style", null);
        }
        StyleConstants.setIcon(iconStyle, new ImageIcon(emoji));
        try { doc.insertString(doc.getLength(), "invisible text", iconStyle); }
        catch (BadLocationException e){}
        try { doc.insertString(doc.getLength(), "\n", userStyle); }
        catch (BadLocationException e){}

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
        //
        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(255, 153, 153));
        //
        JPanel bottomusername = new JPanel();
        bottomusername.setBackground(new Color(255, 153, 153));
        //
        chatPanel.setColumnHeaderView(usernamePanel);
        lbReceiver.setText(" ");
        lbReceiver.setFont(new Font("Monaco", Font.BOLD, 20));
        lbReceiver.setForeground(new Color(154, 24, 95));
        usernamePanel.add(lbReceiver);
        JTextPane inittp = new JTextPane();
        performMenu(inittp);
        chatWindowofUsers.put(" ", inittp);
        chatWindow = chatWindowofUsers.get(" ");
        chatWindow.setFont(new Font("Monaco", Font.PLAIN, 14));
        chatWindow.setEditable(false);
        chatWindow.setBackground(Color.BLACK);
        chatWindow.setPreferredSize(new Dimension(0, 180));
        chatPanel.setViewportView(chatWindow);
        JPanel chatChatPanel = new JPanel();
        chatChatPanel.setLayout(new BoxLayout(chatChatPanel, BoxLayout.LINE_AXIS));
        chatChatPanel.add(Box.createRigidArea(new Dimension(105, 30)));
        chatChatPanel.add(txtMessage);
        chatChatPanel.add(Box.createRigidArea(new Dimension(90, 30)));
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.PAGE_AXIS));
        midPanel.add(chatPanel);
        JPanel footerpannel = new JPanel();
        footerpannel.setBackground((new Color(255, 153, 153)));
        footerpannel.setPreferredSize(new Dimension(0, 5));
        JLabel footerusername = new JLabel(username);
        footerusername.setFont(new Font("Monaco", Font.BOLD, 20));
        footerusername.setForeground(new Color(159, 146, 7));
        footerpannel.add(footerusername);
        midPanel.add(footerpannel);
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
        btnFile = new JButton("Upload");
        btnFile.setFont(new Font("Monaco", Font.BOLD, 18));
        btnFile.setAlignmentX(CENTER_ALIGNMENT);
        btnFile.setFocusable(false);
        btnFile.setBackground(new Color(255, 153, 153));
        btnFile.setForeground(Color.white);
        btnFile.setUI(new stylebutton());
        btnFile.setMaximumSize(size);
        btnFile.setEnabled(false);
        //
        btnDown = new JButton("Download");
        btnDown.setFont(new Font("Monaco", Font.BOLD, 20));
        btnDown.setAlignmentX(CENTER_ALIGNMENT);
        btnDown.setFocusable(false);
        btnDown.setBackground(new Color(255, 153, 153));
        btnDown.setForeground(Color.white);
        btnDown.setUI(new stylebutton());
        btnDown.setMaximumSize(size);
        btnDown.setEnabled(false);
        //
        JPanel jbtn = new JPanel();
        jbtn.setLayout(new BoxLayout(jbtn, BoxLayout.LINE_AXIS));
        jbtn.add(btnSend);
        jbtn.add(Box.createRigidArea(new Dimension(30, 0)));
        jbtn.add(btnFile);
        jbtn.add(Box.createRigidArea(new Dimension(30, 0)));
        jbtn.add(btnDown);
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
        this.setSize(new Dimension(585, 540));
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
                fileChooser.setMultiSelectionEnabled(true);
                String fileNames = "";
                File[] files = null;
                int rVal = fileChooser.showOpenDialog(contentPane.getParent());
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    files = fileChooser.getSelectedFiles();
                    for (File file : files) {
                        fileNames += file.getName() + ",";
                    }
                }
                try {
                    dos.writeUTF("#msgfile");
                    dos.writeUTF(lbReceiver.getText());
                    dos.writeUTF(fileNames);
                    String size_file = "";
                    ArrayList<byte[]> byte_get = new ArrayList<>();
                    byte[] selectedFile = null;
                    BufferedInputStream bis = null;
                    for (int i = 0; i < files.length; i++) {
                        selectedFile = new byte[(int) files[i].length()];
                        bis = new BufferedInputStream(new FileInputStream(files[i].getAbsolutePath()));
                        bis.read(selectedFile, 0, selectedFile.length);
                        String size = String.valueOf(selectedFile.length);
                        size_file += size;
                        size_file += ",";
                        byte_get.add(selectedFile);
                    }
                    dos.writeUTF(size_file);
                    String[] size_file_split = size_file.split(",");
                    for (int j = 0; j < size_file_split.length; j++) {
                        int size_ = Integer.parseInt(size_file_split[j]);
                        int bufferSize = 2048;
                        int offset = 0;
                        while (size_ > 0) {
                            dos.write(byte_get.get(j), offset, Math.min(size_, bufferSize));
                            offset += Math.min(size_, bufferSize);
                            size_ -= bufferSize;
                        }
                        dos.flush();
                        bis.close();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        onlineUsers.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    lbReceiver.setText((String) onlineUsers.getSelectedItem());
                    if (chatWindow != chatWindowofUsers.get(lbReceiver.getText())) {
                        txtMessage.setText("");
                        chatWindow = chatWindowofUsers.get(lbReceiver.getText());
                        chatWindow.setBackground(Color.BLACK);
                        chatWindow.setPreferredSize(new Dimension(0, 180));
                        chatPanel.setViewportView(chatWindow);
                        chatPanel.validate();
                        if (!lbReceiver.getText().equals(" ")) {
                            if (!tempVal.equals(lbReceiver.getText()))
                                try {
                                    dos.writeUTF("@123" + username + "@" + lbReceiver.getText());
                                    tempVal = lbReceiver.getText();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                        }
                    }
                    if (lbReceiver.getText().isBlank()) {
                        btnSend.setEnabled(false);
                        btnFile.setEnabled(false);
                        txtMessage.setEnabled(false);
                        btnDown.setEnabled(false);
//                        JTextPane jtpp = new JTextPane();
//                        performMenu(jtpp);
//                        chatWindowofUsers.put(" ",jtpp);
                    } else {
                        btnSend.setEnabled(true);
                        btnFile.setEnabled(true);
                        txtMessage.setEnabled(true);
                        btnDown.setEnabled(true);
                    }
                }

            }
        });
        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (txtMessage.getText().isBlank() || lbReceiver.getText().isBlank()) {
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
                    dos.writeUTF(lbReceiver.getText());
                    dos.writeUTF(txtMessage.getText());
                    dos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    performMessage("ERROR", "Network error!", true);
                }
                JLabel smileIcon = new JLabel(new ImageIcon("emoji\\smile.png"));
                JLabel sadIcon = new JLabel(new ImageIcon("emoji\\sad.png"));
                JLabel heartIcon = new JLabel(new ImageIcon("emoji\\heart.png"));
                JLabel confusedIcon = new JLabel(new ImageIcon("emoji\\confused.png"));
                if (txtMessage.getText().toString().equals(":)")){
                    performEmoij(username,smileIcon.getIcon().toString(),false);
                }
                else if (txtMessage.getText().toString().equals(":(")){
                    performEmoij(username,sadIcon.getIcon().toString(),false);
                }
                else if (txtMessage.getText().toString().equals("<3")){
                    performEmoij(username,heartIcon.getIcon().toString(),false);
                }
                else if (txtMessage.getText().toString().equals("-_-")){
                    performEmoij(username,confusedIcon.getIcon().toString(),false);
                }
                else{
                    // PrintMsg
                    performMessage(username, txtMessage.getText(), true);
                }
                txtMessage.setText("");
            }
        });
        //
        btnDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String f = "";
                String[] listStr = new String[fileContain.entrySet().size()];
                int i = 0;
                for (Map.Entry mapElement : fileContain.entrySet()) {
                    f += (String) mapElement.getKey() + ",";
                    listStr[i++] = (String) mapElement.getKey();
                }
                String input = (String) JOptionPane.showInputDialog(null, "Choose now...",
                        "The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null, // Use
                        // default
                        // icon
                        listStr, // Array of choices
                        listStr[1]); // Initial choice
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(input));
                fileChooser.setDialogTitle("Specify a file to save");
                int userSelection = fileChooser.showSaveDialog(contentPane);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    for (Map.Entry mapElement1 : fileContain.entrySet()) {
                        if (input.equals(mapElement1.getKey())) {
                            byte[] content = fileContain.get(mapElement1.getKey());
                            BufferedOutputStream bos = null;
                            try {
                                bos = new BufferedOutputStream(new FileOutputStream(fileToSave));
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            JOptionPane.showMessageDialog(null, "Successfully download! File is in " + fileToSave.getAbsolutePath());
                            int nextAction = JOptionPane.showConfirmDialog(null, "Do you want to open this file?", "Successful", JOptionPane.YES_NO_OPTION);
                            if (nextAction == JOptionPane.YES_OPTION) {
                                try {
                                    Desktop.getDesktop().open(fileToSave);
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            if (bos != null) {
                                try {
                                    bos.write(content);
                                    bos.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            }
                        }
                    }
                }
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
                        if (message.equals("<3")){
                            JLabel smileIcon = new JLabel(new ImageIcon("emoji\\heart.png"));
                            performEmoij(sender,smileIcon.getIcon().toString(),true);
                        }
                        else if (message.equals(":)")){
                            JLabel smileIcon = new JLabel(new ImageIcon("emoji\\smile.png"));
                            performEmoij(sender,smileIcon.getIcon().toString(),true);
                        }
                        else if (message.equals(":(")){
                            JLabel smileIcon = new JLabel(new ImageIcon("emoji\\sad.png"));
                            performEmoij(sender,smileIcon.getIcon().toString(),true);
                        }
                        else if (message.equals("-_-")){
                            JLabel smileIcon = new JLabel(new ImageIcon("emoji\\confused.png"));
                            performEmoij(sender,smileIcon.getIcon().toString(),true);
                        }
                        else{
                            performMessage(sender, message, false);
                        }
                    } else if (method.equals("#msgfile")) {
                        String sender = dis.readUTF();
                        String filename = dis.readUTF();
                        String[] filesSplit_ = filename.split(",");
                        String filesSize = dis.readUTF();
                        String[] filesSizeSplit_ = filesSize.split(",");
//                        int size = Integer.parseInt(dis.readUTF());
                        int bufferSize = 2048;
                        byte[] buffer = new byte[bufferSize];
                        for (int i = 0; i < filesSizeSplit_.length; i++) {
                            int size = Integer.parseInt(filesSizeSplit_[i]);
                            ByteArrayOutputStream file = new ByteArrayOutputStream();
                            while (size > 0) {
                                dis.read(buffer, 0, Math.min(bufferSize, size));
                                file.write(buffer, 0, Math.min(bufferSize, size));
                                size -= bufferSize;
                            }
                            fileContain.put(filesSplit_[i], file.toByteArray());
                        }

                        JOptionPane.showMessageDialog(null, "Successfully download!");

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
                        String chatting = lbReceiver.getText();

                        boolean isChattingOnline = false;

                        for (String user : users) {
                            if (user.equals(username) == false) {
                                onlineUsers.addItem(user);
                                if (!user.equals(" ")) l1.addElement(user);
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
                            onlineUsers.setSelectedItem(" ");
                            JOptionPane.showMessageDialog(null, chatting + " is offline\nGo back to home page");
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


}