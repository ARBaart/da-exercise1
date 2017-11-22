package example.hello;

import java.util.HashMap;

public class Buffer extends HashMap<Integer,Timestamp> {


    public Buffer() {}

    //copy constructor
    public Buffer(Buffer original){
        original.forEach((id,timestamp)->{
            this.put(id, new Timestamp(original.get(id)));

        });}

}
