package remote;

import Whiteboard.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public interface IRemoteWhiteboard extends Remote {

    ConcurrentHashMap<Integer, Shape> getShapes() throws RemoteException;

    void addShape(Shape shape) throws RemoteException;


    void updateWhiteboard() throws RemoteException;

}
