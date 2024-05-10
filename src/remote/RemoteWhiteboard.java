package remote;

import Whiteboard.DrawBoard;
import Whiteboard.Shape;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard {
    // store shapes and texts on whiteboard as a list of shapes
    public ConcurrentHashMap<Integer, Shape> shapes = new ConcurrentHashMap<>();

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
    }

    @Override
    public void updateWhiteboard() throws RemoteException {

    }


}
