package de.esnecca.multi.reserve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class ReserveTest {

    private Reserve reserve;

    @Before
    public void init() {
        reserve = new Reserve();
    }

    @Test
    public void testReserve() {
        BigInteger bi = BigInteger.valueOf(4711);

        assertTrue(reserve.reserve(bi, 12));
        assertFalse(reserve.reserve(bi, 12));

    }

    @Test
    public void testFree() {
        BigInteger bi = BigInteger.valueOf(4711);

        assertTrue(reserve.reserve(bi, 12));
        assertFalse(reserve.reserve(bi, 12));

        assertEquals(12, reserve.free(bi).intValue());
        assertNull(reserve.free(bi));
        assertNull(reserve.free(bi));

        assertTrue(reserve.reserve(bi, 12));
        assertFalse(reserve.reserve(bi, 12));

        assertEquals(12, reserve.free(bi).intValue());
        assertNull(reserve.free(bi));
        assertNull(reserve.free(bi));

    }
}
