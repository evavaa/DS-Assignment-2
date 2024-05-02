package manager;

import Whiteboard.ManagerWhiteBoardGUI;

public class Manager {
    public static String username;
    private static String ip;
    private static int port;


    public static void main(String[] args) {
        try {
            // read server address and port number from the command line
            ip = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];

            // launch the application
            ManagerWhiteBoardGUI managerGUI = new ManagerWhiteBoardGUI();
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Please provide server address and port number!", "Server Error", JOptionPane.ERROR_MESSAGE);
            ip = "localhost";
            port = 2000;
            ManagerWhiteBoardGUI managerGUI = new ManagerWhiteBoardGUI();
        }
    }
}
