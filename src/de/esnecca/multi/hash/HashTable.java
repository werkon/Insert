package de.esnecca.multi.hash;

import java.math.BigInteger;

import de.esnecca.multi.tools.Prime;

public class HashTable {

    private Prime size;
    private HashEntry hashEntries[];

    public HashTable(int size) {
        this.size = new Prime(size);
        hashEntries = new HashEntry[this.size.getPrime()];
        reset();
    }

    public synchronized void reset() {
        for (int i = 0; i < size.getPrime(); ++i) {
            hashEntries[i] = null;
        }
    }

    private synchronized HashEntry _get(int i) {
        return hashEntries[i];
    }

    public HashEntry get(BigInteger bi) {
        int i = bi.mod(size.getBigPrime()).intValue();
        return _get(i);
    }

    private synchronized void _set(HashEntry hashEntry, int i) {
        hashEntries[i] = hashEntry;
    }


    public void set(HashEntry hashEntry) {
        int i = hashEntry.getValue().mod(size.getBigPrime()).intValue();
        _set(hashEntry, i);
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
