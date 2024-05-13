package server;

import client.Client;
import org.json.JSONObject;
import remote.IRemoteWhiteboard;
import remote.RemoteWhiteboard;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class WhiteboardServer {
    private static String ip;
    private static int port;
    private ArrayList<Client> clients = new ArrayList<>();

    public static void main(String[] args) {

        try {
            // read server address and port number from the command line
            ip = args[0];
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Please provide server address and port number!", "Server Error", JOptionPane.ERROR_MESSAGE);
            ip = "localhost";
            port = 2000;
        }

        try{
            IRemoteWhiteboard remoteWhiteboard = new RemoteWhiteboard();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("whiteboard", remoteWhiteboard);
            System.out.println("Whiteboard server ready. Waiting for connection...");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}
