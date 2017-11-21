package example.hello;

import java.util.HashMap;

public class Buffer extends HashMap<Integer,Timestamp> {

    public Buffer(){};

    //copy constructor
    public Buffer(Buffer original){
        original.forEach((k,v)->{
            this.put(k, new Timestamp(original.get(k)));

        });}

}
