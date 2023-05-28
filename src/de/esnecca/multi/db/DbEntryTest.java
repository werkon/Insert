package de.esnecca.multi.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class DbEntryTest {
    @Test
    public void testEquals() {
        BigInteger bi1 = BigInteger.valueOf(123);
        BigInteger bi2 = BigInteger.valueOf(123);
        BigInteger bi3 = BigInteger.valueOf(124);

        DbEntry dbe1 = new DbEntry(bi1, 1, 2, 3);
        DbEntry dbe2 = new DbEntry(bi2, 1, 2, 3);
        DbEntry dbe3 = new DbEntry(bi3, 1, 2, 3);
        DbEntry dbe4 = new DbEntry(bi3, 1, 2, 4);

        assertTrue(bi1.equals(bi2));
        assertTrue(dbe1.equals(dbe1));
        assertTrue(dbe1.equals(dbe2));
        assertFalse(dbe1.equals(dbe3));
        assertFalse(dbe3.equals(dbe4));

    }

    @Test
    public void testGetBi() {
        BigInteger bi1 = BigInteger.valueOf(123);
        DbEntry dbe1 = new DbEntry(bi1, 1, 2, 3);
        assertEquals(dbe1.getBi(), bi1);
    }

    @Test
    public void testGetGameid() {
        BigInteger bi1 = BigInteger.valueOf(123);
        DbEntry dbe1 = new DbEntry(bi1, 1, 2, 3);
        assertEquals(dbe1.getGameid(), 1);
    }


    @Test
    public void testGetResult() {
        BigInteger bi1 = BigInteger.valueOf(123);
        DbEntry dbe1 = new DbEntry(bi1, 1, 2, 3);
        assertEquals(dbe1.getResult(), 2);
    }

    @Test
    public void testGetInserted() {
        BigInteger bi1 = BigInteger.valueOf(123);
        DbEntry dbe1 = new DbEntry(bi1, 1, 2, 3);
        assertEquals(dbe1.getInserted(), 3);
    }

}
