package example.hello;

import java.rmi.RMISecurityManager;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class SESmessaging implements SES_interface {

    //Constructor adds own ID to vector timestamp
    public SESmessaging() {
        addIDToTimestamp(ID);
    }

    //ID of process
    private int ID = 0;

    public Registry LocalRegistry;

    //Local vector timestamp
    private static Timestamp currentTimestamp = new Timestamp();

    //Timestampbuffer for purpose of SES algorithm
    private static Buffer currentBuffer = new Buffer();

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
    private void incrementTimestamp(){
        currentTimestamp.put(ID, currentTimestamp.get(ID)+1);
    }

    //When a new process connects it's id can be added to the vector timestamp
    public static void addIDToTimestamp(int newID){
        currentTimestamp.put(newID,0);

    }

    //After delivery of message this function merges the buffer of the delivered message with the currentBuffer.
    private void mergeBuffer(Buffer sourceBuffer){
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
    public void receiveMessage(Message message) throws RemoteException {
        System.out.println("Receiving: "+ message.content + " with buffer " + message.buffer.get(ID) + " with timestamp "+message.timestamp);
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
    public void deliverMessage(Message message){
        System.out.println("Delivering: "+ message.content);
        incrementTimestamp();
        mergeBuffer(message.buffer);
        currentTimestamp.merge(message.timestamp);
    }

    //Copies timestamp and buffer, creates a message and sends it to the delay class.
    protected void sendMessage(int destID, SES_interface destination, String content){

        incrementTimestamp();
        Timestamp timestampCopy = new Timestamp(currentTimestamp);
        Buffer bufferCopy = new Buffer(currentBuffer);

        Message message = new Message(content, bufferCopy, timestampCopy);

        Delay delay = new Delay(destination, message);

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
            System.err.println("Server ready. Our ID is: "+ ID);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }




}
