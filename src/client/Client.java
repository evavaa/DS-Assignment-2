package client;

import Whiteboard.ClientWhiteBoardGUI;
import remote.IRemoteWhiteboard;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static String username;
    private static String ip;
    private static int port;

    public void connect() {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            LocateRegistry.getRegistry("localhost");
            IRemoteWhiteboard remoteMath = (IRemoteWhiteboard) registry.lookup("Compute");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // read server address and port number from the command line
            ip = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];

            // launch the application
            ClientWhiteBoardGUI clientGUI = new ClientWhiteBoardGUI();
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Please provide server address and port number!", "Server Error", JOptionPane.ERROR_MESSAGE);
            ip = "localhost";
            port = 2000;
            ClientWhiteBoardGUI clientGUI = new ClientWhiteBoardGUI();
        }
    }


}
