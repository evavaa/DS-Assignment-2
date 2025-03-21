package remote;

import Whiteboard.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public interface IRemoteWhiteboard extends Remote {

    ConcurrentHashMap<Integer, Shape> getShapes() throws RemoteException;

    void setShapes(ConcurrentHashMap<Integer, Shape> shapes) throws RemoteException;

    void addShape(Shape shape) throws RemoteException;

    void setManager(IRemoteManager manager) throws RemoteException;

    void registerClient(IRemoteClient client) throws RemoteException;

    boolean isUniqueUsername(String key) throws RemoteException;

    void unregisterClient(String username) throws RemoteException;

    boolean hasManager() throws RemoteException;

    void notifyAppTerminate(String message) throws RemoteException;

    void clear() throws RemoteException;

    void kickOut(String username) throws RemoteException;

    void updateChat(String username, String message) throws RemoteException;
}
