package example.hello;

import java.util.HashMap;

public class Timestamp extends HashMap<Integer,Integer> {

    public Timestamp(){}

    //Copy constructor
    public Timestamp(Timestamp original){
        this.putAll(original);
    }

    //Greater or equal than function for Timestamps
    public boolean geq (Timestamp otherTimestamp){
        boolean result = true;

        for(Timestamp.Entry<Integer,Integer> entry1: this.entrySet()){
            Integer value1 = entry1.getValue();
            for(Timestamp.Entry<Integer,Integer> entry2: otherTimestamp.entrySet()){
                Integer value2 = entry2.getValue();
                if(value1<value2){
                    result = false;
                }
            }
        }
        return result;
    }

    //Merges other timestamps into this one
    public void merge(Timestamp otherTimestamp){
        otherTimestamp.forEach((k,v)->{
            if (this.containsKey(k)){
                if(otherTimestamp.get(k)>=this.get(k))
                {
                    this.put(k,v);
                }
            }
            else{
                this.put(k,v);
            }
        });
    }
}


