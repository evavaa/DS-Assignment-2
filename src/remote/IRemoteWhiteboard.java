package remote;

import Whiteboard.Shape;
import client.Client;
import manager.Manager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public interface IRemoteWhiteboard extends Remote {

    ConcurrentHashMap<Integer, Shape> getShapes() throws RemoteException;

    void addShape(Shape shape) throws RemoteException;

    void setManager(IRemoteManager manager) throws RemoteException;

    void registerClient(IRemoteClient client) throws RemoteException;

    boolean isUniqueUsername(String key) throws RemoteException;

    void unregisterClient(IRemoteClient client) throws RemoteException;

    boolean hasManager() throws RemoteException;

}
