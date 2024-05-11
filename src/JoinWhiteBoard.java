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
            // JOptionPane.showMessageDialog(null, "Please provide server address and port number!", "Server Error", JOptionPane.ERROR_MESSAGE);
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
            try {
                Socket socket = new Socket(ip, port);

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
                    int reply = JOptionPane.showConfirmDialog(null, "The manager refuses the connection. Do you want to exit the application?", "Request Declined",  JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    System.exit(0);
                } else if (response.equals("yes")) {
                    System.out.println("Connect Successfully.");
                    // initialise a client
                    ClientWhiteBoardGUI clientGUI = new ClientWhiteBoardGUI(remoteWhiteboard);
                    Client client = new Client(username, clientGUI, remoteWhiteboard);

                    // register on the remoteWhiteboard
                    remoteWhiteboard.registerClient(client);
                }
                output.close();
                input.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your server.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NotBoundException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your input.", "Error", JOptionPane.WARNING_MESSAGE);
        }

    }
}
