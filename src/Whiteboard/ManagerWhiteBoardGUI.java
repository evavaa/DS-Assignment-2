package Whiteboard;

import remote.IRemoteWhiteboard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;


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

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem newFile;
    private JMenuItem openFile;
    private JMenuItem saveFile;
    private JMenuItem saveAsFile;

    private transient DrawBoard drawBoard = new DrawBoard();

    public ManagerWhiteBoardGUI(IRemoteWhiteboard remoteWhiteboard) {
        setContentPane(WhiteBoard);
        setTitle("Manager Whiteboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);

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
        menu.add(newFile);
        menu.add(openFile);
        menu.add(saveFile);
        menu.add(saveAsFile);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        createUIComponents();
        update();
    }

    public DrawBoard getDrawBoard() {
        return drawBoard;
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

        // new file
        newFile.addActionListener(new ActionListener() {
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
