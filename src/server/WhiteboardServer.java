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

//        try {
//            // create a server socket for client communication
//            ServerSocket server = new ServerSocket(port);
//            System.out.println("Waiting for client connection-");
//
//            // wait for connections
//            while (true) {
//                Socket client = server.accept();
//                System.out.println("New client applying for connection!");
//
//                // start a new thread for a connection
//                Thread t = new Thread(() -> serveClient(client));
//                t.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void serveClient(Socket client) {
        try(Socket clientSocket = client) {
            // Input stream & Output Stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            // receive message from the client
            String request = input.readUTF();
            System.out.println("CLIENT: " + request);

            JSONObject requestJSON = new JSONObject(request);
            String user = requestJSON.getString("user");
            String message = requestJSON.getString("message");

            String response = null;
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
