package client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WhiteBoardGUI extends JFrame {
    private JPanel toolBar;
    private JButton freeDrawButton;
    private JPanel drawBoard;
    private JPanel userList;
    private JPanel chatBox;
    private JLabel userListLabel;
    private JTextArea textArea1;
    private JLabel chatLabel;
    private JTextArea textArea2;
    private JTextField chatInput;
    private JButton sendButton;
    private JPanel WhiteBoard;
    private JButton textButton;
    private JButton eraserButton;
    private JComboBox shapes;
    private JButton colorButton;

    private String selectedShape;
    private Color color;


    public WhiteBoardGUI() {
        setContentPane(WhiteBoard);
        setTitle("Whiteboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);

        // setup drawing board
        drawBoard.setBackground(Color.white);
        drawBoard.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        // setup toolbar
        toolBar.setBorder(new TitledBorder(null, "Tools"));

        // select shape
        shapes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedShape = shapes.getSelectedItem().toString();
            }
        });

        // select colour
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = JColorChooser.showDialog(null, "Select a Colour", Color.black);
            }
        });
    }

    public static void main(String[] args) {
        new WhiteBoardGUI();
    }
}