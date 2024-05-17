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
    private static int portTCP;
    private static IRemoteWhiteboard remoteWhiteboard;

    public static void main(String[] args) {
        try {
            // read server address and port number from the command line
            ip = args[0];
            port = Integer.parseInt(args[1]);
            portTCP = Integer.parseInt(args[2]);
            username = args[3];

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please provide server address, port number for both RMI and TCP sockets as well as and a username!", "Server Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            LocateRegistry.getRegistry(ip);
            remoteWhiteboard = (IRemoteWhiteboard) registry.lookup("whiteboard");

            // check if there exists a manager for the whiteboard
            if (! remoteWhiteboard.hasManager()) {
                JOptionPane.showMessageDialog(null, "Whiteboard not exist. Please exit the application.", "Server Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            // check if the username is unique
            if (! remoteWhiteboard.isUniqueUsername(username)) {
                JOptionPane.showMessageDialog(null, "Username exists! Please enter a new username.", "Input Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            // wait for manager's approval before joining the whiteboard
            try {
                Socket socket = new Socket(ip, portTCP);
                // get the input/output streams for reading/writing data from/to the socket
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                // send message to server
                output.writeUTF(username);
                output.flush();
                System.out.println("Message sent");
                JOptionPane.showMessageDialog(null, "Waiting for the manager to approve.", "Message", JOptionPane.INFORMATION_MESSAGE);

                // receive response from server
                String response = input.readUTF();
                System.out.println("Response: " + response);

                // if the manager refuses the client to join the whiteboard, exit
                if (response.equals("no")) {
                    JOptionPane.showMessageDialog(null, "The manager refuses the connection.", "Request Declined",  JOptionPane.OK_OPTION);
                    System.exit(0);
                }
                // approval received from the manager
                else if (response.equals("yes")) {
                    System.out.println("Connect Successfully.");
                    // initialise a client
                    ClientWhiteBoardGUI clientGUI = new ClientWhiteBoardGUI(remoteWhiteboard, username);
                    Client client = new Client(username, clientGUI, remoteWhiteboard);
                    // register on the remoteWhiteboard
                    remoteWhiteboard.registerClient(client);
                }
                output.close();
                input.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                // graceful exit
                int reply = JOptionPane.showConfirmDialog(null, "The server is currently down. Do you want to exit the application?", "Server Error",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your server.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NotBoundException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your port and server address.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
