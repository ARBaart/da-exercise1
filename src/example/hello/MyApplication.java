package example.hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class MyApplication {

    private static Map<Integer, SES_interface> RemoteHosts = new HashMap();

    private static Timestamp currentTimestamp = new Timestamp();

    private static Buffer currentBuffer = new Buffer();

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

    private static void sendMessage(int destID, String content){

        Message message = new Message(content, currentBuffer, currentTimestamp);

        Delay delay = new Delay(RemoteHosts.get(destID), message);

        new Thread(delay).start();

    }

    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        SESmessaging localInterface = new SESmessaging();
        localInterface.startServer(1098);

        System.out.println("Press Enter to continue");
        sc.nextLine();

        int ID = connectHost("localhost",1099);

        try {
            RemoteHosts.get(ID).sayHello();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        System.out.println("connectHost succeeded, press Enter");
        sc.nextLine();

        try {
            sendMessage(ID, "Hello, this is a complicated way of sending messages");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }




    }

}
