package example.hello;

import java.rmi.RMISecurityManager;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.ArrayList;

public class SESmessaging implements SES_interface {

    public SESmessaging() {}

    private int ID = 0;

    public Registry LocalRegistry;

    private ArrayList<Message> messageBuffer = new ArrayList<Message>();

    public void sayHello(){
        System.out.println("Connection established");
    }

    public int getID(){
        return ID;
    }

    public void communicate(Message message) throws RemoteException {
        System.out.println(message.content);
    }

    //Function that starts server locally
    public void startServer( int port){

        try {
            System.setSecurityManager(new RMISecurityManager());
            LocateRegistry.createRegistry(port);
            SES_interface stub = (SES_interface) UnicastRemoteObject.exportObject(this, 0);

            // Bind the remote object's stub in the registry
            LocalRegistry = LocateRegistry.getRegistry(port);
            LocalRegistry.bind("SES_interface", stub);
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }




}
