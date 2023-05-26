package de.esnecca.multi.hash;

import java.math.BigInteger;

import de.esnecca.multi.tools.Prime;

public class HashTable {

    private Prime size;
    private HashEntry hashEntries[];

    public HashTable(int size) {
        this.size = new Prime(size);
        hashEntries = new HashEntry[this.size.getPrime()];
    }

    public synchronized void reset() {
        for (int i = 0; i < size.getPrime(); ++i) {
            hashEntries[i] = null;
        }
    }

    public synchronized HashEntry get(BigInteger i) {
        return hashEntries[i.mod(size.getBigPrime()).intValue()];
    }

    public synchronized void set(HashEntry hashEntry) {
        hashEntries[hashEntry.getValue().mod(size.getBigPrime()).intValue()] = hashEntry;
    }


    public synchronized int filled() {
        int c = 0;
        for( int i = 0; i < size.getPrime(); ++i){
            if( hashEntries[i] != null ){
                ++c;
            }
        }
        return (int)(((long)c) * 100 / size.getPrime());

    }

}
