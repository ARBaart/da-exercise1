package example.hello;

import java.rmi.RMISecurityManager;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class SESmessaging implements SES_interface, Runnable {

    //Constructor adds own ID to vector timestamp
    public SESmessaging(int newID, int port) {
        ID=newID;
        startServer(port);
    }

    //ID of process
    private int ID;

    public Registry LocalRegistry;

    //Local vector timestamp
    private Timestamp currentTimestamp = new Timestamp();

    //Timestampbuffer for purpose of SES algorithm
    private Buffer currentBuffer = new Buffer();

    //Buffer for messages that are received out of order
    private ArrayList<Message> messageBuffer = new ArrayList<Message>();

    //sayHello function prints connection established if another process connects to this host
    public void sayHello(){
        System.out.println("Connection established");
        incrementTimestamp();
    }

    //Supplies ID to remote processes
    public int getID(){
        return ID;
    }

    //Events can call this function to increment the timestamp
    private synchronized void incrementTimestamp(){
        currentTimestamp.put(ID, currentTimestamp.get(ID)+1);
    }

    //After delivery of message this function merges the buffer of the delivered message with the currentBuffer.
    private synchronized void mergeBuffer(Buffer sourceBuffer){
        sourceBuffer.forEach((k,v)->{
            if (currentBuffer.containsKey(k)){
                if(sourceBuffer.get(k).geq(currentBuffer.get(k)))
                {
                    currentBuffer.put(k,v);
                }
            }
            else{
                currentBuffer.put(k,v);
            }
        });
    }

    //This function can be called remotely and acts as the postman for this interface. Checks if a message can be delivered or needs to be added to the messageBuffer.
    public synchronized void receiveMessage(Message message) throws RemoteException {
        //System.out.println("Receiving: "+ message.content + " with buffer " + message.buffer.get(ID) + " with timestamp "+message.timestamp);
        System.out.println("Receiving: "+ message.content + " with buffer " + message.buffer.get(ID) + " while at "+currentTimestamp);
        if(!message.buffer.containsKey(ID)){
            deliverMessage(message);

            //Next code checks messageBuffer to see if one of the old messages can now be delivered
            int length = messageBuffer.size();
            int i=0;

            while(i<length){
                if(currentTimestamp.geq(messageBuffer.get(i).buffer.get(ID))){
                    deliverMessage(messageBuffer.get(i));
                    messageBuffer.remove(i);
                    length=messageBuffer.size();
                    i=0;
                }
                else{
                    i++;
                }
            }

        }
        else if(currentTimestamp.geq(message.buffer.get(ID))){
            deliverMessage(message);

            //Next code checks messageBuffer to see if one of the old messages can now be delivered
            int length = messageBuffer.size();
            int i=0;

            while(i<length){
                if(currentTimestamp.geq(messageBuffer.get(i).buffer.get(ID))){
                    deliverMessage(messageBuffer.get(i));
                    messageBuffer.remove(i);
                    length=messageBuffer.size();
                    i=0;
                }
                else{
                    i++;
                }
            }

        }
        else{
            messageBuffer.add(message);
        }
    }

    //Actual delivery of message with incrementingTimestamp, buffer merge, timestamp merge
    public synchronized void deliverMessage(Message message){
        System.out.println("Delivering: "+ message.content);
        incrementTimestamp();
        mergeBuffer(message.buffer);
        currentTimestamp.merge(message.timestamp);
    }

    //Copies timestamp and buffer, creates a message and sends it to the delay class.
    protected void sendMessage(int destID, String content){

        incrementTimestamp();
        Timestamp timestampCopy = new Timestamp(currentTimestamp);
        Buffer bufferCopy = new Buffer(currentBuffer);

        Message message = new Message(content, bufferCopy, timestampCopy);

        Delay delay = new Delay(DA_SES_main.RemoteHosts.get(destID), message);

        new Thread(delay).start();

        currentBuffer.put(destID, timestampCopy);

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

            DA_SES_main.RemoteHosts.put(ID,stub);

            System.err.println("Server ready. Our ID is: "+ ID);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void run(){
        sendMessage((ID+1)%3, ID + " Sends message 1 to " + (ID+1)%3);
        sendMessage((ID+2)%3, ID + " Sends message 1 to " + (ID+2)%3);
        sendMessage((ID+2)%3, ID + " Sends message 2 to " + (ID+2)%3);
        sendMessage((ID+1)%3, ID + " Sends message 2 to " + (ID+1)%3);
        sendMessage((ID+1)%3, ID + " Sends message 3 to " + (ID+1)%3);
        sendMessage((ID+2)%3, ID + " Sends message 3 to " + (ID+2)%3);
    }


}
