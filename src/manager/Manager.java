package manager;

import Whiteboard.DrawBoard;
import Whiteboard.ManagerWhiteBoardGUI;
import remote.IRemoteManager;
import remote.IRemoteWhiteboard;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Manager extends UnicastRemoteObject implements IRemoteManager {
    public String username;
    private transient ManagerWhiteBoardGUI managerGUI;
    private IRemoteWhiteboard remoteWhiteboard;
    private DrawBoard drawBoard;

    public Manager(String username, ManagerWhiteBoardGUI managerGUI, IRemoteWhiteboard remoteWhiteboard) throws RemoteException {
        super();
        this.username = username;
        this.managerGUI = managerGUI;
        this.remoteWhiteboard = remoteWhiteboard;
        this.drawBoard = managerGUI.getDrawBoard();
    }

    /**
     * Get the username of the manager.
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Prompt the GUI to update user list displayed on the interface.
     * @param usernames a list of usernames to be displayed
     */
    @Override
    public void updateUsers(String[] usernames) {
        managerGUI.updateUserList(usernames);
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
        managerGUI.updateChatHistory(newMessage);
    }
}
