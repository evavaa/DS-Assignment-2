package client;

import javax.swing.*;

public class LoginGUI extends JFrame {
    private JLabel title;
    private JLabel username;
    private JTextField usernameInput;
    private JTextField IPAddressInput;
    private JTextField portNumberInput;
    private JLabel IP;
    private JLabel portNumber;
    private JPanel Login;
    private JButton newButton;
    private JButton joinButton;

    public LoginGUI() {
        setContentPane(Login);
        setTitle("Whiteboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
