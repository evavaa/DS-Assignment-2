import Whiteboard.ClientWhiteBoardGUI;
import Whiteboard.ManagerWhiteBoardGUI;
import client.Client;
import manager.Manager;
import remote.IRemoteClient;
import remote.IRemoteManager;
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
import java.rmi.server.UnicastRemoteObject;

public class CreateWhiteBoard {
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
            //TODO: for testing purpose only
            ip = "localhost";
            port = 2000;
            username = "manager";
        }

        try {
            // connect to the rmi registry that is running on given ip address
            Registry registry = LocateRegistry.getRegistry(ip, 1099);
            LocateRegistry.getRegistry(ip);
            remoteWhiteboard = (IRemoteWhiteboard) registry.lookup("whiteboard");

            // initialise a manager
            ManagerWhiteBoardGUI managerGUI = new ManagerWhiteBoardGUI(remoteWhiteboard);
            Manager manager = new Manager(username, managerGUI, remoteWhiteboard);

            // register on the remoteWhiteboard
            remoteWhiteboard.setManager(manager);
            System.out.println("Whiteboard created");

            // create socket
            try {
                ServerSocket socket = new ServerSocket(port);
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
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your server.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NotBoundException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your input.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

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
