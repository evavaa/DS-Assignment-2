import Whiteboard.ChatClient;
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
            // JOptionPane.showMessageDialog(null, "Please provide server address, port number and username!", "Server Error", JOptionPane.ERROR_MESSAGE);
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
            ManagerWhiteBoardGUI managerGUI = new ManagerWhiteBoardGUI(remoteWhiteboard, username);
            Manager manager = new Manager(username, managerGUI, remoteWhiteboard);

            // register on the remoteWhiteboard
            remoteWhiteboard.setManager(manager);
            System.out.println("Whiteboard created");
            //startConnection(ip, port);

        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your server.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NotBoundException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your input.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

//    public static void startConnection(String ip, int port) {
//        try {
//            Socket socket = new Socket(ip, port);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
