package de.esnecca.multi.hash;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HashEntry other = (HashEntry) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        if (result != other.result)
            return false;
        return true;
    }    

    
    
}
