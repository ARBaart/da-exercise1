package example.hello;

import java.sql.Time;
import java.util.HashMap;

public class Timestamp extends HashMap<Integer,Integer> {
    
    public Timestamp(){
        for(int i =0; i<=2;i++){
            this.put(i,0);
        }
    }

    //Copy constructor
    public Timestamp(Timestamp original){
        this.putAll(original);
    }

    //Greater or equal than function for Timestamps
    public boolean geq (Timestamp otherTimestamp) {
        boolean result = true;

        for (Entry<Integer, Integer> entry : otherTimestamp.entrySet()) {
            Integer ID = entry.getKey();

            if (!this.containsKey(ID) || this.get(ID) < entry.getValue())
                result = false;
        }

        return result;
    }

    //Merges other timestamps into this one
    public void merge(Timestamp otherTimestamp) {
        otherTimestamp.forEach((ID,time) -> {
            if (!this.containsKey(ID) || otherTimestamp.get(ID) > this.get(ID))
                this.put(ID,time);
        });
    }
}
