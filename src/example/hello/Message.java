package example.hello;

public class Message implements java.io.Serializable{

    public Timestamp timestamp;
    public Buffer buffer;
    public String content;

    public Message(String messageContent, Buffer messageBuffer, Timestamp messageTimestamp){
        timestamp=messageTimestamp;
        buffer=messageBuffer;
        content=messageContent;
    }

}
