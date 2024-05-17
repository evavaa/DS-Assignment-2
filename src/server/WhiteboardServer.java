package server;

import remote.IRemoteWhiteboard;
import remote.RemoteWhiteboard;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardServer {

    public static void main(String[] args) {
        try{
            int port = Integer.parseInt(args[0]);
            IRemoteWhiteboard remoteWhiteboard = new RemoteWhiteboard();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("whiteboard", remoteWhiteboard);
            System.out.println("Whiteboard server ready. Waiting for connection...");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}
