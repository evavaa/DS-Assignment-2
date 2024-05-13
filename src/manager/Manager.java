package manager;

import Whiteboard.DrawBoard;
import Whiteboard.ManagerWhiteBoardGUI;
import remote.IRemoteManager;
import remote.IRemoteWhiteboard;

import javax.swing.*;
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

    public String getUsername() {
        return username;
    }

    @Override
    public void updateUsers(String[] usernames) {
        managerGUI.updateUserList(usernames);
    }

    @Override
    public void updateBoard() {
        drawBoard.repaint();
    }

    @Override
    public boolean getApproval(String username) {
        String message = "Allow " + username  + " to share your whiteboard?";
        int reply = JOptionPane.showConfirmDialog(null, message, "Message",  JOptionPane.YES_NO_OPTION);
        return (reply == JOptionPane.YES_OPTION);
    }

}
