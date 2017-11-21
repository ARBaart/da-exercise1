package example.hello;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

//This class delays messages for a random amount of time to test the SES algorithm
public class Delay implements Runnable {

    private SES_interface destinationHost;
    private Message messageToSend;

    public Delay(SES_interface destHost, Message message){
        destinationHost=destHost;
        messageToSend=message;
    }

    public void run(){

        int randomDelay = ThreadLocalRandom.current().nextInt(0, 5000);

        try {
            Thread.sleep(randomDelay);
        } catch (Exception e){
            System.err.println("Delay exception: " + e.toString());
            e.printStackTrace();
        }


        try {
            destinationHost.receiveMessage(messageToSend);
            System.out.println("Sent " + messageToSend.content);
        } catch (Exception e){
            System.err.println("Delay exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
