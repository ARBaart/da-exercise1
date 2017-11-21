package example.hello;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = "192.168.1.140";
        try {
            System.setSecurityManager(new RMISecurityManager());

            Registry registry = LocateRegistry.getRegistry(host);

            Hello stub = (Hello) registry.lookup("Hello");

            String response = stub.sayHello("Test");
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}