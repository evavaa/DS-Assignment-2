package remote;

import Whiteboard.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard {
    // store shapes and texts on whiteboard as a list of shapes
    public ConcurrentHashMap<Integer, Shape> shapes = new ConcurrentHashMap<>();

    // store a list of clients
    public ConcurrentHashMap<String, IRemoteClient> clients = new ConcurrentHashMap<>();
    public static IRemoteManager manager;

    public RemoteWhiteboard() throws RemoteException {
    }

    /**
     * Get all drawings on the whiteboard.
     * @throws RemoteException
     */
    @Override
    public ConcurrentHashMap<Integer, Shape> getShapes() throws RemoteException {
        return shapes;
    }

    @Override
    public void addShape(Shape shape) throws RemoteException {
        System.out.println("add new shapes on the whiteboard");
        shapes.put(shapes.size(), shape);

        // notify all clients to repaint
        for (IRemoteClient c : clients.values()) {
            c.updateBoard();
        }

        // notify manager to repaint
        manager.updateBoard();
    }

    @Override
    public void updateWhiteboard() throws RemoteException {

    }

    @Override
    public void setManager(IRemoteManager manager) {
        this.manager = manager;
    }

    @Override
    public void registerClient(IRemoteClient client) throws RemoteException {
        clients.put(client.getUsername(), client);
    }

    @Override
    public void unregisterClient(IRemoteClient client) throws RemoteException {
        clients.remove(client.getUsername());
    }

    @Override
    public void notifyClients() throws RemoteException {

    }


}
