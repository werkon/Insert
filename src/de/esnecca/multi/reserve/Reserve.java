package de.esnecca.multi.reserve;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Reserve {
    private HashMap<BigInteger, Integer> map;
    private int sleeping;

    public Reserve() {
        map = new HashMap<BigInteger, Integer>();
    }

    synchronized public boolean reserve(BigInteger bi, int inserted) {
        Integer i = map.get(bi);
        if (i == null) {
            map.put(bi, Integer.valueOf(inserted));
            return true;
        } else {
            return false;
        }
    }

    synchronized public Integer free(BigInteger bi) {
        return map.remove(bi);
    }

    synchronized public int getSleeping() {
        return sleeping;
    }

    synchronized public void incSleeping() {
        ++sleeping;
    }

    synchronized public void decSleeping() {
        --sleeping;
    }

    synchronized public String toString() {
        String s = "[";
        for (Map.Entry<BigInteger, Integer> entry : map.entrySet()) {
            if (s.length() > 1) {
                s += ", ";
            }
            s += entry.getValue().toString();
        }
        s += "]";
        return s;
    }
}
