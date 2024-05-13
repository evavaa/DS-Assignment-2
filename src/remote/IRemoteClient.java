package remote;

import Whiteboard.ClientWhiteBoardGUI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteClient extends Remote {
    String getUsername() throws RemoteException;

    void updateBoard() throws RemoteException;

    void updateUsers(String[] usernames) throws RemoteException;

    void disconnect() throws RemoteException;

    void beKickedOut() throws RemoteException;

    void updateChatHistory(String send_username, String message) throws RemoteException;
}
