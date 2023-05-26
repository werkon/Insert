package de.esnecca.multi.tools;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class PrimeTest {

    Prime prime1;
    Prime prime2;

    @Before
    public void init() {
        prime1 = new Prime(10);
        prime2 = new Prime(1000 * 1000);
    }

    @Test
    public void testGetBigPrime() {
        BigInteger bi1 = BigInteger.valueOf(7);
        assertEquals(prime1.getBigPrime(), bi1);
        BigInteger bi2 = BigInteger.valueOf(999983);
        assertEquals(prime2.getBigPrime(), bi2);
    }

    @Test
    public void testGetPrime() {
        assertEquals(7, prime1.getPrime());
        assertEquals(999983, prime2.getPrime());
    }
}
