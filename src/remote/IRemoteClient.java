package remote;

import Whiteboard.ClientWhiteBoardGUI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteClient extends Remote {
    String getUsername() throws RemoteException;

    ClientWhiteBoardGUI getGUI() throws RemoteException;

    void updateBoard() throws RemoteException;
}
