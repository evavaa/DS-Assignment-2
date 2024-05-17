package client;

import Whiteboard.ClientWhiteBoardGUI;
import Whiteboard.DrawBoard;
import remote.IRemoteWhiteboard;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IRemoteClient {

    public String username;
    private transient ClientWhiteBoardGUI clientGUI;
    private IRemoteWhiteboard remoteWhiteboard;
    private DrawBoard drawBoard;

    public Client(String username, ClientWhiteBoardGUI clientGUI, IRemoteWhiteboard remoteWhiteboard) throws RemoteException {
        super();
        this.username = username;
        this.clientGUI = clientGUI;
        this.remoteWhiteboard = remoteWhiteboard;
        this.drawBoard = clientGUI.getDrawBoard();
    }

    /**
     * Prompt the canvas to paint all drawings again.
     */
    @Override
    public void updateBoard() {
        drawBoard.repaint();
    }

    /**
     * Prompt the GUI to update the chat window with the new message.
     * @param sendUsername user who sends the new message
     * @param message content of the message
     */
    @Override
    public void updateChatHistory(String sendUsername, String message) {
        String newMessage = sendUsername + ": " + message + "\n";
        clientGUI.updateChatHistory(newMessage);
    }

    /**
     * Prompt the GUI to update user list displayed on the interface.
     * @param usernames a list of usernames to be displayed
     */
    @Override
    public void updateUsers(String[] usernames) {
        clientGUI.updateUserList(usernames);
    }

    /**
     * Get the username of the peer.
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Disconnect the client from the server.
     * @param message warning message to be displayed on GUI
     */
    @Override
    public void disconnect(String message) {
        JOptionPane.showMessageDialog(null, message, "Warning Message",  JOptionPane.OK_OPTION);
        clientGUI.dispose();
    }

    /**
     * Disconnect the client from the server when the client is kicked out by the manager.
     */
    @Override
    public void beKickedOut() {
        JOptionPane.showMessageDialog(null, "Sorry, you have been kicked out by the manager.", "Warning Message",  JOptionPane.OK_OPTION);
        clientGUI.dispose();
    }
}
