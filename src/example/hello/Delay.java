package example.hello;

import java.util.concurrent.ThreadLocalRandom;

public class Delay implements Runnable {

    private SES_interface destinationHost;
    private Message messageToSend;

    Delay(SES_interface destHost, Message message){
        destinationHost=destHost;
        messageToSend=message;
    }
    public void run(){

        int randomDelay = ThreadLocalRandom.current().nextInt(0, 1000);

        try {
            Thread.sleep(randomDelay);
        } catch (Exception e){
            System.err.println("Delay exception: " + e.toString());
            e.printStackTrace();
        }


        try {
            destinationHost.communicate(messageToSend);
        } catch (Exception e){
            System.err.println("Delay exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
