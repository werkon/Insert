package de.esnecca.multi.reserve;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Reserve {
    private HashMap<BigInteger, Integer> map;
    private HashMap<Integer, Integer> map2;

    public Reserve() {
        map = new HashMap<BigInteger, Integer>();
        map2 = new HashMap<Integer, Integer>();
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
        int sleeping = 0;
        for (Map.Entry<Integer, Integer> entry : map2.entrySet()) {
            sleeping += entry.getValue();
        }
        return sleeping;
    }

    synchronized public void incSleeping(int inserted) {
        Integer i = map2.get(inserted);
        if (i == null) {
            map2.put(inserted, 1);
        } else {
            map2.put(inserted, Integer.valueOf(i + 1));
        }
    }

    synchronized public void decSleeping(int inserted) {
        Integer i = map2.get(inserted);
        if (i <= 1) {
            map2.remove(inserted);
        } else {
            map2.put(inserted, Integer.valueOf(i - 1));
        }
    }

    synchronized public String toString() {
        String s = "[";
        for (Map.Entry<Integer, Integer> entry : map2.entrySet()) {
            if (s.length() > 1) {
                s += ", ";
            }
            s += "(" + entry.getKey().toString() + "->" + entry.getValue().toString() + ")";
        }
        s += "]";
        return s;
    }
}
