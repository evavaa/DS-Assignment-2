package remote;

import Whiteboard.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard {
    // store shapes and texts on whiteboard as a list of shapes
    public ConcurrentHashMap<Integer, Shape> shapes = new ConcurrentHashMap<>();

    // store a list of clients
    public ConcurrentHashMap<String, IRemoteClient> clients = new ConcurrentHashMap<>();
    public static IRemoteManager manager;

    public RemoteWhiteboard() throws RemoteException {
    }

    @Override
    public boolean isUniqueUsername(String key) throws RemoteException {
        if (clients.get(key) != null) {
            return false;
        } else if (key == manager.getUsername()) {
            return false;
        } else {
            return true;
        }
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
    public void setManager(IRemoteManager manager) throws RemoteException {
        this.manager = manager;
        List<String> usernames = new ArrayList<>();
        usernames.add(manager.getUsername());
        manager.updateUsers(usernames.toArray(new String[0]));
    }

    @Override
    public void registerClient(IRemoteClient client) throws RemoteException {
        clients.put(client.getUsername(), client);
        updateUserList();
    }

    @Override
    public void unregisterClient(String username) throws RemoteException {
        clients.remove(username);
        updateUserList();
    }

    private void updateUserList() throws RemoteException {
        // notify all clients and manager to update user list
        List<String> usernames = new ArrayList<>(clients.keySet());
        usernames.add(manager.getUsername());

        for (IRemoteClient c : clients.values()) {
            c.updateUsers(usernames.toArray(new String[0]));
        }
        manager.updateUsers(usernames.toArray(new String[0]));
    }

    @Override
    public void notifyAppTerminate() throws RemoteException {
        // notify all clients that the manager has closed the application
        for (IRemoteClient c : clients.values()) {
            c.disconnect();
        }
    }

    @Override
    public boolean hasManager() throws RemoteException {
        return (manager != null);
    }

    @Override
    public void clear() throws RemoteException {
        shapes.clear();
    }

}
