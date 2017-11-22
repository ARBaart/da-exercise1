package example.hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class MyApplication {

    private static Map<Integer, SES_interface> RemoteHosts = new HashMap();

    //Function returns object for calling functions on remote host
    private static int connectHost(String IP, int port){
        try {
            Registry registry = LocateRegistry.getRegistry(IP, port);
            SES_interface stub = (SES_interface) registry.lookup("SES_interface");
            int ID = stub.getID();
            RemoteHosts.put(ID,stub);
            SESmessaging.addIDToTimestamp(ID);
            return ID;
        } catch (Exception e) {
            System.err.println("connectHost exception: " + e.toString());
            e.printStackTrace();
            return 0;
        }

    }

    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        SESmessaging localInterface = new SESmessaging(0,1099);

        System.out.println("Press Enter to continue");
        sc.nextLine();

        int ID = connectHost("172.20.10.2",1099);

        try {
            RemoteHosts.get(ID).sayHello();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        System.out.println("Connected to host with ID: " + ID);
        sc.nextLine();

        try {
            localInterface.sendMessage(ID, RemoteHosts.get(ID), "Message 1");
            localInterface.sendMessage(ID, RemoteHosts.get(ID), "Message 2");
            localInterface.sendMessage(ID, RemoteHosts.get(ID), "Message 3");
            localInterface.sendMessage(ID, RemoteHosts.get(ID), "Message 4");
            localInterface.sendMessage(ID, RemoteHosts.get(ID), "Message 5");
            localInterface.sendMessage(ID, RemoteHosts.get(ID), "Message 6");
            localInterface.sendMessage(ID, RemoteHosts.get(ID), "Message 7");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }




    }

}
