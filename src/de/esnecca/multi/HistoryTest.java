package de.esnecca.multi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class HistoryTest {

    private final int width = 7;
    private final int height = 6;
    private final int colors = 2;
    private final int wins = 4;

    private History history;

    @Before
    public void init() {
        history = new History(width, height, colors, wins);
    }

    @Test
    public void testCopyConstructor() {
        history.insert(0);
        History h = new History(history);

        assertTrue(history.equals(h));
        assertTrue(h.equals(history));

        h.insert(0);

        assertEquals(1, history.getInserted());
        assertEquals(2, h.getInserted());
        assertEquals(1, history.getInserted(0));
        assertEquals(2, h.getInserted(0));

        h.remove();

        assertEquals(1, history.getInserted());
        assertEquals(1, h.getInserted());
        assertEquals(1, history.getInserted(0));
        assertEquals(1, h.getInserted(0));

        h.remove();

        assertEquals(1, history.getInserted());
        assertEquals(0, h.getInserted());
        assertEquals(1, history.getInserted(0));
        assertEquals(0, h.getInserted(0));

    }

    @Test
    public void testEquals() {
        history.insert(1);
        history.insert(1);
        History h = new History(history);

        assertTrue(history.equals(h));
        assertTrue(h.equals(history));

        history.remove();

        assertFalse(history.equals(h));
        assertFalse(h.equals(history));

        h.remove();

        assertTrue(history.equals(h));
        assertTrue(h.equals(history));
    }

    @Test
    public void testInsert() {
        history.insert(0);
        assertEquals(1, history.getInserted());
    }

    @Test
    public void testRemove() {
        history.insert(0);
        assertEquals(1, history.getInserted());
        assertEquals(1, history.getInserted(0));
        history.remove();
        assertEquals(0, history.getInserted());
        assertEquals(0, history.getInserted(0));

        int x = 0;
        for (int i = 0; i < history.getSize(); ++i) {
            if (history.isFull(x)) {
                ++x;
            }
            history.insert(x);
        }
        assertEquals(history.getSize(), history.getInserted());

        while (history.getInserted() > 0) {
            history.print();
            history.remove();
        }

        for (x = 0; x < history.getWidth(); ++x) {
            assertEquals(0, history.getInserted(x));
            for (int y = 0; y < history.getHeight(); ++y) {
                assertEquals(0, history.get(x, y));
            }
        }
        assertEquals(0, history.getInserted());
    }

    @Test
    public void testReset() {
        history.insert(0);
        history.reset();
        assertEquals(0, history.getInserted());
    }
}
