package example.hello;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SES_interface extends Remote{

    void sayHello() throws RemoteException;

    int getID() throws RemoteException;

    void receiveMessage(Message message) throws RemoteException;

}
