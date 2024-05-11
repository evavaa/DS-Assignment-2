package remote;

import Whiteboard.ClientWhiteBoardGUI;
import Whiteboard.ManagerWhiteBoardGUI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteManager extends Remote {
    ManagerWhiteBoardGUI getGUI() throws RemoteException;
    void updateBoard() throws RemoteException;
}
