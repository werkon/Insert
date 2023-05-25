package de.esnecca.multi;

import java.math.BigInteger;

public class HashEntry {
    private BigInteger value;
    private int result;

    public HashEntry(BigInteger value, int result) {
        this.value = value;
        this.result = result;
    }

    public BigInteger getValue() {
        return value;
    }
    public int getResult() {
        return result;
    }    
    
}
