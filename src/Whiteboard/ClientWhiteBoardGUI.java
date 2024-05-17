package Whiteboard;

import remote.IRemoteWhiteboard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;


public class ClientWhiteBoardGUI extends JFrame {
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

    private IRemoteWhiteboard remoteWhiteboard;
    private String username;

    private transient DrawBoard drawBoard = new DrawBoard();

    public ClientWhiteBoardGUI(IRemoteWhiteboard remoteWhiteboard, String username) {
        this.remoteWhiteboard = remoteWhiteboard;
        this.username = username;
        setContentPane(WhiteBoard);
        setTitle("Client Whiteboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        chatArea.setEditable(false);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // when the client chooses to quit, unregister the client from the remote server
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(JOptionPane.showConfirmDialog(null, "Do you want to leave the whiteboard?", "Message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    try {
                        remoteWhiteboard.unregisterClient(username);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                    System.exit(0);
            }
        });

        // setup drawing board
        drawBoard.setRemoteWhiteboard(remoteWhiteboard);
        drawPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        drawPanel.add(drawBoard);
        drawPanel.revalidate();

        // setup toolbar
        toolBar.setBorder(new TitledBorder(null, "Tools"));

        createUIComponents();
        update();
        setVisible(true);
    }

    /**
     * Update the content of user list on GUI.
     * @param usernames a list of usernames of all users
     */
    public void updateUserList(String[] usernames) {
        userList.setListData(usernames);
    }

    /**
     * Update the content of chat window on GUI.
     * @param message new message to be displayed in the chat box
     */
    public void updateChatHistory(String message) {
        chatArea.append(message);
    }

    /**
     * Set up button listeners.
     */
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

        // send chat
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatInput.getText();
                try {
                    remoteWhiteboard.updateChat(username, message);
                    chatInput.setText("");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Set up eraser with adjustable sizes.
     */
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

    public DrawBoard getDrawBoard() {
        return drawBoard;
    }


}
