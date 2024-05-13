package Whiteboard;

import remote.IRemoteWhiteboard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.stream.StreamSupport;


public class ManagerWhiteBoardGUI extends JFrame {
    private JPanel toolBar;
    private JButton freeDrawButton;
    private JPanel drawPanel;
    private JPanel userListPanel;
    private JPanel chatBox;
    private JLabel userListLabel;
    private JLabel chatLabel;
    private JTextArea chatArea;
    private JTextField chatInput;
    private JButton sendButton;
    private JPanel WhiteBoard;
    private JButton textButton;
    private JButton eraserButton;
    private JComboBox shapes;
    private JButton colorButton;
    private JSpinner eraserSize;
    private JList userList;
    private JButton kickoutButton;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem newFile;
    private JMenuItem openFile;
    private JMenuItem saveFile;
    private JMenuItem saveAsFile;
    private JMenuItem closeFile;
    private IRemoteWhiteboard remoteWhiteboard;
    private String username;
    private ChatClient chatClient;

    private transient DrawBoard drawBoard = new DrawBoard();

    public ManagerWhiteBoardGUI(IRemoteWhiteboard remoteWhiteboard, String username) {
        this.remoteWhiteboard = remoteWhiteboard;
        this.username = username;
        //this.chatClient = chatClient;
        setContentPane(WhiteBoard);
        setTitle("Manager Whiteboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // when the manager chooses to quit, close the application and notify all clients
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(JOptionPane.showConfirmDialog(null, "Do you want to close the whiteboard? All drawings on the whiteboard will be cleared.", "Message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    // close the GUI
                    setVisible(false);
                    try {
                        remoteWhiteboard.notifyAppTerminate();
                        // clear all drawings
                        remoteWhiteboard.clear();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });

        // setup drawing board
        drawBoard.setRemoteWhiteboard(remoteWhiteboard);
        drawPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        drawPanel.add(drawBoard);
        drawPanel.revalidate();

        // setup toolbar
        toolBar.setBorder(new TitledBorder(null, "Tools"));

        // setup menu bar
        menuBar = new JMenuBar();
        menu = new JMenu("File");

        newFile = new JMenuItem("New");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        saveAsFile = new JMenuItem("Save As");
        closeFile = new JMenuItem("Close");
        menu.add(newFile);
        menu.add(openFile);
        menu.add(saveFile);
        menu.add(saveAsFile);
        menu.add(closeFile);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        createUIComponents();
        update();

        setVisible(true);
    }

    public DrawBoard getDrawBoard() {
        return drawBoard;
    }

    public void updateUserList(String[] usernames) {
        userList.setListData(usernames);
    }

    public void update() {
        // free draw
        freeDrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setCurrentTool("free draw");
            }
        });

        // select shape
        shapes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setCurrentTool(shapes.getSelectedItem().toString().toLowerCase());
            }
        });

        // text input
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setCurrentTool("text");
            }
        });

        // eraser
        eraserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setCurrentTool("eraser");
            }
        });

        // select colour
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setColor(JColorChooser.showDialog(null, "Select a Colour", Color.black));
            }
        });

        // kick out selected user
        kickoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get selected username
                String selected = userList.getSelectedValue().toString();
                // cannot select manager him/herself
                if (! selected.equals(username)) {
                    // let the manager confirm whether he/she wants to kick out the selected user
                    String message = "Do you want to kick out this user: " + selected + "?";
                    int reply = JOptionPane.showConfirmDialog(null, message, "Warning Message", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        // remove the selected user
                        try {
                            remoteWhiteboard.kickOut(selected);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // send chat
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatInput.getText();
                chatClient.sendRequest(message);
            }
        });

        // new file
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("not yet");

            }
        });

        // open file
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("not yet");
                JFileChooser fileChooser = new JFileChooser();
                int i = fileChooser.showOpenDialog(null);

            }
        });

        // save file
        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("not yet");

            }
        });

        // save file as...
        saveAsFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("not yet");

            }
        });

        // close application
        // new file
        closeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("not yet");

            }
        });

    }

    private void createUIComponents() {
        // setup eraser sizes
        eraserSize = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        eraserSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner eraserSize = (JSpinner) e.getSource();
                drawBoard.setEraserSize((Integer) eraserSize.getValue());
            }
        });
    }
}
