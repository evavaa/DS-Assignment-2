package Whiteboard;

import remote.IRemoteWhiteboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

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
    private JFileChooser fileChooser;
    private File file;

    private transient DrawBoard drawBoard = new DrawBoard();

    public ManagerWhiteBoardGUI(IRemoteWhiteboard remoteWhiteboard, String username) {
        this.remoteWhiteboard = remoteWhiteboard;
        this.username = username;
        setContentPane(WhiteBoard);
        setTitle("Manager Whiteboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        chatArea.setEditable(false);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // when the manager chooses to quit, close the application and notify all clients
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(JOptionPane.showConfirmDialog(null, "Do you want to close the whiteboard? All drawings on the whiteboard will be cleared.", "Message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    // close the GUI
                    setVisible(false);
                    try {
                        String message = "The manager has closed the whiteboard. The application will terminate soon.";
                        remoteWhiteboard.notifyAppTerminate(message);
                        // clear all drawings
                        remoteWhiteboard.clear();
                        remoteWhiteboard.setManager(null);
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
                try {
                    remoteWhiteboard.updateChat(username, message);
                    chatInput.setText("");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // new file
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // remind the user to manually save current whiteboard
                int reply = JOptionPane.showConfirmDialog(null, "The changes on current whiteboard will not be saved automatically. \n" +
                        "Please save the whiteboard manually before opening a new whiteboard.\n" +
                        "Are you sure that you want to create a new whiteboar? \n", "Warning Message", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    // create a blank whiteboard (clear drawings on current whiteboard)
                    try {
                        // notify clients the closure of current whiteboard
                        // clients on current whiteboard do not have access to the new whiteboard
                        String message = "The manager has created a new whiteboard. The current whiteboard will terminate soon.";
                        remoteWhiteboard.notifyAppTerminate(message);
                        remoteWhiteboard.clear();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // open file
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser openFileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
                openFileChooser.setFileFilter(filter);

                int reply = openFileChooser.showOpenDialog(openFileChooser);
                if (reply == JFileChooser.APPROVE_OPTION) {
                    // check if the selected file is a txt file
                    file = openFileChooser.getSelectedFile();
                    try {
                        FileInputStream fileInput = new FileInputStream(file);
                        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
                        ConcurrentHashMap<Integer, Shape> shapes = (ConcurrentHashMap<Integer, Shape>)objectInput.readObject();
                        System.out.println("Open an existing whiteboard.");

                        // upload shapes to remote whiteboard
                        remoteWhiteboard.setShapes(shapes);
                        System.out.println("shapes reset");
                        objectInput.close();
                        fileInput.close();

                        try {
                            // notify clients the closure of current whiteboard
                            // clients on current whiteboard do not have access to the new whiteboard
                            String message = "The manager has opened another whiteboard. The current whiteboard will terminate soon.";
                            remoteWhiteboard.notifyAppTerminate(message);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid Whiteboard file. Please try another file.", "Error", JOptionPane.OK_OPTION);
                        System.out.println("Invalid Whiteboard file.");
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // save file
        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save the whiteboard for the first time, then invoke the saveAs method
                if (file == null) {
                    saveAs();
                } else {
                    String fileName = file.getAbsolutePath().toLowerCase();
                    // if the file saved last time is a txt file, then automatically save to the same file again
                    if (fileName.endsWith(".txt")) {
                        System.out.println("Whiteboard saved to an existing txt file.");
                        try {
                            FileOutputStream fileOutput = new FileOutputStream(file);
                            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
                            objectOutput.writeObject(remoteWhiteboard.getShapes());
                            objectOutput.flush();
                            objectOutput.close();
                            objectOutput.close();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    // if the file saved last time is not a txt file, then invoke the saveAs method again
                    else {
                        saveAs();
                    }
                }
            }
        });

        // save file as image or a txt file that can be loaded later
        saveAsFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });

        // close application
        closeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JOptionPane.showConfirmDialog(null, "Do you want to close the whiteboard? All drawings on the whiteboard will be cleared.", "Message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    // close the GUI
                    setVisible(false);
                    try {
                        String message = "The manager has closed the whiteboard. The application will terminate soon.";
                        remoteWhiteboard.notifyAppTerminate(message);
                        // clear all drawings
                        remoteWhiteboard.clear();
                        remoteWhiteboard.setManager(null);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
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

    /**
     * Create a file chooser with three different formats.
     * @return
     */
    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            FileNameExtensionFilter png = new FileNameExtensionFilter("PNG", "png");
            FileNameExtensionFilter jpg = new FileNameExtensionFilter("JPG", "jpg");
            FileNameExtensionFilter txt = new FileNameExtensionFilter("TXT", "txt");
            fileChooser.addChoosableFileFilter(png);
            fileChooser.addChoosableFileFilter(jpg);
            fileChooser.addChoosableFileFilter(txt);
            fileChooser.setFileFilter(txt);
            fileChooser.setAcceptAllFileFilterUsed(false);
        }
        return fileChooser;
    }

    /**
     * Manager can save the whiteboard into three different formats.
     */
    private void saveAs() {
        // let the user choose the file format
        fileChooser = getFileChooser();
        int reply = fileChooser.showSaveDialog(fileChooser);
        if (reply == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            String extension = fileChooser.getFileFilter().getDescription().toLowerCase();
            String fileName = file.getAbsolutePath().toLowerCase();

            // check if the user enters the file extension
            if (!(fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".txt"))) {
                String filePath = file.getAbsolutePath() + "." + extension;
                file = new File(filePath);
                fileName = file.getAbsolutePath().toLowerCase();
            }
            // save to specified file
            try {
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                    System.out.println("Whiteboard saved to an image file.");
                    BufferedImage image = new BufferedImage(drawBoard.getWidth(), drawBoard.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics g = image.getGraphics();
                    drawBoard.printAll(g);
                    ImageIO.write(image, extension, file);
                } else if (fileName.endsWith(".txt")) {
                    System.out.println("Whiteboard saved to a txt file.");
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
                    objectOutput.writeObject(remoteWhiteboard.getShapes());
                    objectOutput.flush();
                    objectOutput.close();
                    objectOutput.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public DrawBoard getDrawBoard() {
        return drawBoard;
    }
}
