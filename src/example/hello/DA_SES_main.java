package example.hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class DA_SES_main {

    protected static Map<Integer, SESmessaging> LocalHosts = new HashMap();

    protected static Map<Integer, SES_interface> RemoteHosts = new HashMap();

    //Function returns object for calling functions on remote host
    private static int connectHost(String IP, int port){
        try {
            Registry registry = LocateRegistry.getRegistry(IP, port);
            SES_interface stub = (SES_interface) registry.lookup("SES_interface");
            int ID = stub.getID();
            RemoteHosts.put(ID,stub);
            return ID;
        } catch (Exception e) {
            System.err.println("connectHost exception: " + e.toString());
            e.printStackTrace();
            return 0;
        }

    }

    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        Logger logger = new Logger();

        LocalHosts.put(0,new SESmessaging(0,1099, logger));
        LocalHosts.put(1,new SESmessaging(1,1100, logger));
        LocalHosts.put(2,new SESmessaging(2,1101, logger));

        System.out.println("Press Enter to continue");
        sc.nextLine();

        /*int ID = connectHost("172.20.10.2",1099);

        try {
            RemoteHosts.get(ID).sayHello();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        System.out.println("Connected to host with ID: " + ID);
        sc.nextLine();*/

        System.out.println("Starting communication");

        new Thread(LocalHosts.get(0)).start();
        new Thread(LocalHosts.get(1)).start();
        new Thread(LocalHosts.get(2)).start();
        try {
            Thread.sleep(20000);
        } catch (Exception e) { e.printStackTrace(); }
        logger.finish();
    }

}
