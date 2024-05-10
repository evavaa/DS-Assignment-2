package server;

import Whiteboard.ClientWhiteBoardGUI;
import remote.IRemoteWhiteboard;
import remote.RemoteWhiteboard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardServer {
    private static String ip;
    private static int port;

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

        try {
            // create a server socket for client connection
            ServerSocket server = new ServerSocket(port);
            System.out.println("Waiting for client connection-");

            // wait for connections
            while (true) {
                Socket client = server.accept();
                System.out.println("New client applying for connection!");

                // start a new thread for a connection
                Thread t = new Thread(() -> serveClient(client));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void serveClient(Socket client) {

    }
}
