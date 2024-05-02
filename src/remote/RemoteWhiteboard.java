package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard {

    public RemoteWhiteboard() throws RemoteException {
    }

    /**
     * Managers can create a whiteboard
     * @throws RemoteException
     */
    @Override
    public void createWhiteboard() throws RemoteException {
        System.out.println("A new whiteboard created.");
    }
}
