package client;

import Whiteboard.ClientWhiteBoardGUI;
import Whiteboard.DrawBoard;
import remote.IRemoteClient;
import remote.IRemoteWhiteboard;
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

    @Override
    public void updateBoard() {
        drawBoard.repaint();
    }

    @Override
    public void updateUsers(String[] usernames) {
        clientGUI.updateUserList(usernames);
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }
}
