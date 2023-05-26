package de.esnecca.multi.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class HashTableTest {

    HashTable hashTable;

    @Before
    public void init() {
        hashTable = new HashTable(100);
    }


    @Test
    public void testGet() {
        BigInteger bi1 = BigInteger.valueOf(4711);
        HashEntry hashEntry1 = new HashEntry(bi1, 3);
        BigInteger bi2 = BigInteger.valueOf(4711);
        HashEntry hashEntry2 = new HashEntry(bi2, 3);
        hashTable.set(hashEntry1);

        assertEquals(4711, hashTable.get(bi1).getValue().intValueExact());
        assertEquals(3, hashTable.get(bi1).getResult());
        assertEquals(hashEntry2, hashTable.get(bi1));
    }

    @Test
    public void testReset() {
        BigInteger bi1 = BigInteger.valueOf(4711);
        HashEntry hashEntry1 = new HashEntry(bi1, 3);
        hashTable.set(hashEntry1);
        assertNotNull(hashTable.get(bi1));
        hashTable.reset();
        assertNull(hashTable.get(bi1));
    }

    @Test
    public void testSet() {
        BigInteger bi1 = BigInteger.valueOf(4711);
        HashEntry hashEntry1 = new HashEntry(bi1, 3);
        BigInteger bi2 = BigInteger.valueOf(4711);
        HashEntry hashEntry2 = new HashEntry(bi2, 3);

        hashTable.set(hashEntry1);

        assertEquals(hashEntry2, hashTable.get(bi1));
    }
}
