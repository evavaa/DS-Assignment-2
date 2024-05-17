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

    /**
     * Check if the provided username is unique.
     * @param key input username
     * @return whether the username is unique
     * @throws RemoteException
     */
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

    /**
     * Set the drawings on the whiteboard and notify all users to update their canvas.
     * @param shapes a list of drawings
     * @throws RemoteException
     */
    @Override
    public void setShapes(ConcurrentHashMap<Integer, Shape> shapes) throws RemoteException {
        this.shapes = shapes;
        // notify all clients to repaint
        for (IRemoteClient c : clients.values()) {
            c.updateBoard();
        }

        // notify manager to repaint
        manager.updateBoard();
    }

    /**
     * Add a piece of drawing to the whiteboard and notify all users to update their canvas.
     * @param shape a list of drawings
     * @throws RemoteException
     */
    @Override
    public void addShape(Shape shape) throws RemoteException {
        shapes.put(shapes.size(), shape);

        // notify all clients to repaint
        for (IRemoteClient c : clients.values()) {
            c.updateBoard();
        }

        // notify manager to repaint
        manager.updateBoard();
    }

    /**
     * Notify all users to update their chat window when a new message comes in.
     * @param username username of the peer who sends the message
     * @param message content of the message
     * @throws RemoteException
     */
    @Override
    public void updateChat(String username, String message) throws RemoteException {
        for (IRemoteClient c : clients.values()) {
            c.updateChatHistory(username, message);
        }
        manager.updateChatHistory(username, message);
    }

    /**
     * Set the whiteboard manager to a particular user.
     * @param manager
     * @throws RemoteException
     */
    @Override
    public void setManager(IRemoteManager manager) throws RemoteException {
        this.manager = manager;
        if (manager != null) {
            updateUserList();
        }

    }

    /**
     * Register the provided client for future updates.
     * @param client user to be added on remote server
     * @throws RemoteException
     */
    @Override
    public void registerClient(IRemoteClient client) throws RemoteException {
        clients.put(client.getUsername(), client);
        updateUserList();
    }

    /**
     * Remove the client from the remote server.
     * @param username username of the peer to be removed
     * @throws RemoteException
     */
    @Override
    public void unregisterClient(String username) throws RemoteException {
        clients.remove(username);
        updateUserList();
    }

    /**
     * Notify all users to update the user list displayed on their GUI
     * @throws RemoteException
     */
    private void updateUserList() throws RemoteException {
        // notify all clients and manager to update user list
        List<String> usernames = new ArrayList<>(clients.keySet());
        usernames.add(manager.getUsername());

        for (IRemoteClient c : clients.values()) {
            c.updateUsers(usernames.toArray(new String[0]));
        }
        manager.updateUsers(usernames.toArray(new String[0]));
    }

    /**
     * Notify all users that the manager has closed the application and ask them to exit.
     * @param message warning message that describes the reason of whiteboard termination
     * @throws RemoteException
     */
    @Override
    public void notifyAppTerminate(String message) throws RemoteException {
        // notify all clients that the manager has closed the application
        for (IRemoteClient c : clients.values()) {
            System.out.println("disconnect client: " + c.getUsername());
            c.disconnect(message);
        }
        clients.clear();
    }

    /**
     * Manager kicks out a particular user in the whiteboard.
     * @param username username of the user to be kicked out
     * @throws RemoteException
     */
    @Override
    public void kickOut(String username) throws RemoteException {
        IRemoteClient client = clients.get(username);
        System.out.println("kick out client: " + username);
        client.beKickedOut();
        unregisterClient(username);
    }

    /**
     * Check if the whiteboard has been created by finding out whether a manager exists.
     * @return whether there exists a whiteboard
     * @throws RemoteException
     */
    @Override
    public boolean hasManager() throws RemoteException {
        return (manager != null);
    }

    /**
     * Clear all drawings on the canvas and remove all users from the user list.
     * @throws RemoteException
     */
    @Override
    public void clear() throws RemoteException {
        shapes.clear();
        manager.updateBoard();
        // update user list
        List<String> usernames = new ArrayList<>();
        usernames.add(manager.getUsername());
        manager.updateUsers(usernames.toArray(new String[0]));
    }

}
