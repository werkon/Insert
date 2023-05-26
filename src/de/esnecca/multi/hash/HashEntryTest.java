package de.esnecca.multi.hash;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class HashEntryTest {


    @Test
    public void testEquals() {
        BigInteger bi1 = BigInteger.valueOf(123);
        BigInteger bi2 = BigInteger.valueOf(123);
        BigInteger bi3 = BigInteger.valueOf(124);

        HashEntry he1 = new HashEntry(bi1, 1);
        HashEntry he2 = new HashEntry(bi2, 1);
        HashEntry he3 = new HashEntry(bi3, 1);
        HashEntry he4 = new HashEntry(bi3, 2);

        assertTrue(bi1.equals(bi2));
        assertTrue(he1.equals(he1));
        assertTrue(he1.equals(he2));
        assertFalse(he1.equals(he3));
        assertFalse(he3.equals(he4));

    }

    @Test
    public void testGetResult() {
        BigInteger bi1 = BigInteger.valueOf(123);
        BigInteger bi2 = BigInteger.valueOf(123);
        HashEntry he1 = new HashEntry(bi1, 1);
        assertTrue(he1.getValue().equals(bi2));
    }


    @Test
    public void testGetValue() {
        BigInteger bi1 = BigInteger.valueOf(123);
        HashEntry he1 = new HashEntry(bi1, 1);
        assertTrue(he1.getResult() == 1);
    }
}
