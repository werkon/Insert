package de.esnecca.multi.reserve;

import java.math.BigInteger;
import java.util.HashMap;

public class Reserve {
    private HashMap<BigInteger,Integer> map;

    public Reserve(){
        map = new HashMap<BigInteger,Integer>();
    }

    synchronized public boolean reserve(BigInteger bi, int inserted){
        Integer i = map.get(bi);
        if( i == null ){
            map.put(bi, Integer.valueOf(inserted));
            return true;
        }else{
            return false;
        }
    }

    synchronized public Integer free(BigInteger bi){
        return map.remove(bi);
    }

}
