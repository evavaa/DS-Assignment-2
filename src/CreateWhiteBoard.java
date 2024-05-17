import Whiteboard.ManagerWhiteBoardGUI;
import manager.Manager;
import remote.IRemoteWhiteboard;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteBoard {
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
            JOptionPane.showMessageDialog(null, "Please provide server address, port number for both RMI and TCP sockets as well as a username!", "Server Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            // connect to the rmi registry that is running on given ip address
            Registry registry = LocateRegistry.getRegistry(ip, port);
            LocateRegistry.getRegistry(ip);
            remoteWhiteboard = (IRemoteWhiteboard) registry.lookup("whiteboard");

            // check if there exists a manager for the whiteboard
            if (remoteWhiteboard.hasManager()) {
                JOptionPane.showMessageDialog(null, "There already exists a manager in this whiteboard.", "Server Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            // initialise a manager
            ManagerWhiteBoardGUI managerGUI = new ManagerWhiteBoardGUI(remoteWhiteboard, username);
            Manager manager = new Manager(username, managerGUI, remoteWhiteboard);

            // register on the remoteWhiteboard
            remoteWhiteboard.setManager(manager);
            System.out.println("Whiteboard created");

            // create socket
            try {
                ServerSocket socket = new ServerSocket(portTCP);
                System.out.println("Waiting for client join request-");
                // wait for join request
                while(true)
                {
                    Socket client = socket.accept();
                    System.out.println("Request: Someone wants to join the whiteboard!");
                    // start a new thread for a connection
                    Thread t = new Thread(() -> serveClient(client));
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showConfirmDialog(null, "Unable to create a server socket. Please check your port number.", "Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to remote server. Please check your server.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NotBoundException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to remote server. Please check your port and server address.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Manager approves or denies the join requests sent by other peers.
     * @param client other peers
     */
    private static void serveClient(Socket client) {
        try(Socket clientSocket = client) {
            // Input stream & Output Stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            // receive message from the client
            String clientUsername = input.readUTF();
            System.out.println("CLIENT: " + clientUsername);
            String message = "Allow " + clientUsername  + " to share your whiteboard?";
            int reply = JOptionPane.showConfirmDialog(null, message, "Message",  JOptionPane.YES_NO_OPTION);
            String response;
            if (reply == JOptionPane.YES_OPTION) {
                response = "yes";
            } else {
                response = "no";
            }
            // send response back to the client
            System.out.println("Response sent: " + response);
            output.writeUTF(response);
            output.flush();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot connect to the client.", "Client Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
