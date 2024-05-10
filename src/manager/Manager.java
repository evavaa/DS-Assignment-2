package manager;

import Whiteboard.ManagerWhiteBoardGUI;
import remote.IRemoteWhiteboard;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Manager {
    public static String username;
    private static String ip;
    private static int port;
    private static ManagerWhiteBoardGUI managerGUI;

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
        }

        try {
            // connect to the rmi registry that is running on given ip address
            Registry registry = LocateRegistry.getRegistry(ip, 1099);
            LocateRegistry.getRegistry(ip);
            IRemoteWhiteboard remoteWhiteboard = (IRemoteWhiteboard) registry.lookup("whiteboard");

            // launch the application
            managerGUI = new ManagerWhiteBoardGUI(remoteWhiteboard);


        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your server.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NotBoundException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your input.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }


}
