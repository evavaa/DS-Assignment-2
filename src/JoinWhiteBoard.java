import Whiteboard.ChatClient;
import Whiteboard.ClientWhiteBoardGUI;
import client.Client;
import remote.IRemoteWhiteboard;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteBoard {
    public static String username;
    private static String ip;
    private static int port;
    private static IRemoteWhiteboard remoteWhiteboard;

    public static void main(String[] args) {
        try {
            // read server address and port number from the command line
            ip = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];

        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Please provide server address, port number and username!", "Server Error", JOptionPane.ERROR_MESSAGE);
            ip = "localhost";
            port = 2000;
            username = "user1";
        }

        try {
            Registry registry = LocateRegistry.getRegistry(ip, 1099);
            LocateRegistry.getRegistry(ip);
            remoteWhiteboard = (IRemoteWhiteboard) registry.lookup("whiteboard");

            // check if there exists a manager for the whiteboard
            if (! remoteWhiteboard.hasManager()) {
                JOptionPane.showMessageDialog(null, "There is no manager in this whiteboard.", "Server Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            // check if the username is unique
            if (! remoteWhiteboard.isUniqueUsername(username)) {
                JOptionPane.showMessageDialog(null, "Username exists! Please enter a new username.", "Input Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            // wait for manager's approval before joining the whiteboard
            JOptionPane.showMessageDialog(null, "Waiting for the manager to approve.");
            if (! remoteWhiteboard.getApproval(username)) {
                JOptionPane.showMessageDialog(null, "The manager refuses the connection.", "Request Declined",  JOptionPane.OK_OPTION);
                System.exit(0);
            }

            // initialise a client
            ClientWhiteBoardGUI clientGUI = new ClientWhiteBoardGUI(remoteWhiteboard, username);
            Client client = new Client(username, clientGUI, remoteWhiteboard);

            // register on the remoteWhiteboard
            remoteWhiteboard.registerClient(client);

            startConnection(ip, port);

        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your server.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NotBoundException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your input.", "Error", JOptionPane.WARNING_MESSAGE);
        }

    }

    public static void startConnection(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
