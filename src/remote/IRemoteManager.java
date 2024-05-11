package remote;

import Whiteboard.ClientWhiteBoardGUI;
import Whiteboard.ManagerWhiteBoardGUI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteManager extends Remote {

    void updateBoard() throws RemoteException;

    void updateUsers(String[] usernames) throws RemoteException;

    String getUsername() throws RemoteException;
}
