package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteWhiteboard extends Remote {

    void createWhiteboard() throws RemoteException;

}
