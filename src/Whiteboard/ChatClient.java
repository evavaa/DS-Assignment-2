package Whiteboard;

import org.json.JSONObject;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient extends Thread {
    private String ip;
    private int port;
    private String username;
    protected Socket socket;
    protected DataInputStream input;
    protected DataOutputStream output;

    public ChatClient(String ip, int port, String username, Socket socket) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.socket = socket;

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String sendRequest(String message) {
        try {
            // generate message to server
            JSONObject request = new JSONObject();
            request.put("user", username);
            request.put("message", message);

            // send message to server
            output.writeUTF(request.toString());
            output.flush();
            System.out.println("Message sent");

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

            }
            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
