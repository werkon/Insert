package de.esnecca.multi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private final int width = 7;
    private final int height = 6;
    private final int colors = 2;
    private final int wins = 4;

    private Game game;

    @Before
    public void init() {
        game = new Game(width, height, colors, wins);
    }

    @Test
    public void testCopyConstructor() {
        game.insert(0);
        Game g = new Game(game);

        assertTrue(game.equals(g));
        assertTrue(g.equals(game));

        g.insert(0);

        assertEquals(1, game.getInserted());
        assertEquals(2, g.getInserted());
        assertEquals(1, game.getInserted(0));
        assertEquals(2, g.getInserted(0));

        assertEquals(1, game.get(0, 0));

        if (g.getColors() >= 2) {
            assertEquals(2, g.get(0, 1));
        }

        if (g.getColors() >= 2) {
            assertEquals(2, game.getCurrentColor());
        }
        assertEquals(1, g.getCurrentColor());

        assertEquals(g.getWidth(), game.getWidth());
        assertEquals(g.getHeight(), game.getHeight());
        assertEquals(g.getSize(), game.getSize());
        assertEquals(g.getWidth(), game.getWidth());
        assertEquals(g.getColors(), game.getColors());

    }

    @Test
    public void testEquals() {
        game.insert(0);
        Game g = new Game(game);

        assertTrue(game.equals(g));
        assertTrue(g.equals(game));

        game.remove(0);

        assertFalse(game.equals(g));
        assertFalse(g.equals(game));

        g.remove(0);

        assertTrue(game.equals(g));
        assertTrue(g.equals(game));
    }

    @Test
    public void testGetCurrentColor() {
        int cc = game.getCurrentColor();
        assertEquals(1, game.getCurrentColor());
        game.insert(width - 1);
        if (++cc > colors)
            cc = 1;
        assertEquals(cc, game.getCurrentColor());
        game.insert(width - 1);
        if (++cc > colors)
            cc = 1;
        assertEquals(cc, game.getCurrentColor());
    }

    @Test
    public void testGetInserted() {
        assertEquals(0, game.getInserted());
        game.insert(width - 1);
        assertEquals(1, game.getInserted());
        game.insert(width - 1);
        assertEquals(2, game.getInserted());
    }

    @Test
    public void testGetInserted2() {
        assertEquals(0, game.getInserted(width - 1));
        game.insert(width - 1);
        assertEquals(1, game.getInserted(width - 1));
        game.insert(width - 1);
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(0, game.getInserted(0));

    }

    @Test
    public void testGetWins() {
        assertEquals(wins, game.getWins());
    }

    @Test
    public void testInsert() {
        int cc = game.getCurrentColor();
        int cco = cc;
        assertEquals(0, game.getInserted());
        assertEquals(cc, game.getCurrentColor());
        assertEquals(0, game.get(width - 1, game.getInserted(width - 1)));
        game.insert(width - 1);
        cco = cc;
        if (++cc > colors)
            cc = 1;
        assertEquals(1, game.getInserted());
        assertEquals(1, game.getInserted(width - 1));
        assertEquals(cc, game.getCurrentColor());
        assertEquals(cco, game.get(width - 1, game.getInserted(width - 1) - 1));
        game.insert(width - 1);
        cco = cc;
        if (++cc > colors)
            cc = 1;
        assertEquals(2, game.getInserted());
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(cc, game.getCurrentColor());
        assertEquals(cco, game.get(width - 1, game.getInserted(width - 1) - 1));
        game.insert(0);
        cco = cc;
        if (++cc > colors)
            cc = 1;
        assertEquals(3, game.getInserted());
        assertEquals(1, game.getInserted(0));
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(cc, game.getCurrentColor());
        assertEquals(cco, game.get(0, game.getInserted(0) - 1));
    }

    @Test
    public void testIsFull() {
        assertEquals(false, game.isFull());
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                assertEquals(false, game.isFull());
                game.insert(x);
            }
        }
        assertEquals(true, game.isFull());
    }

    @Test
    public void testIsFull2() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                assertEquals(false, game.isFull(x));
                game.insert(x);
            }
            assertEquals(true, game.isFull(x));
        }
        for (int x = 0; x < width; ++x) {
            assertEquals(true, game.isFull(x));
        }
    }

    @Test
    public void testRemove() {
        int cc = game.getCurrentColor();

        game.insert(width - 1);
        int c0 = cc;
        if (++cc > colors)
            cc = 1;
        game.insert(width - 1);
        int c1 = cc;
        if (++cc > colors)
            cc = 1;
        game.insert(0);
        int c2 = cc;
        if (++cc > colors)
            cc = 1;

        // 2
        // 1 1

        assertEquals(3, game.getInserted());
        assertEquals(1, game.getInserted(0));
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(cc, game.getCurrentColor());
        assertEquals(c2, game.get(0, 0));
        assertEquals(c1, game.get(width - 1, game.getInserted(width - 1) - 1));
        assertEquals(c0, game.get(width - 1, game.getInserted(width - 1) - 2));

        game.remove(0);
        if (--cc < 1)
            cc = colors;

        // 2
        // 1

        assertEquals(2, game.getInserted());
        assertEquals(0, game.getInserted(0));
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(cc, game.getCurrentColor());
        assertEquals(c1, game.get(width - 1, game.getInserted(width - 1) - 1));
        assertEquals(c0, game.get(width - 1, game.getInserted(width - 1) - 2));
        assertEquals(0, game.get(0, 0));

        game.remove(width - 1);
        if (--cc < 1)
            cc = colors;

        //
        // 1

        assertEquals(1, game.getInserted());
        assertEquals(0, game.getInserted(0));
        assertEquals(1, game.getInserted(width - 1));
        assertEquals(cc, game.getCurrentColor());
        assertEquals(0, game.get(width - 1, game.getInserted(width - 1)));
        assertEquals(c0, game.get(width - 1, game.getInserted(width - 1) - 1));

        game.remove(width - 1);
        if (--cc < 1)
            cc = colors;

        //
        //

        assertEquals(0, game.getInserted());
        assertEquals(0, game.getInserted(0));
        assertEquals(0, game.getInserted(width - 1));
        assertEquals(cc, game.getCurrentColor());
        assertEquals(0, game.get(width - 1, 0));
    }

    @Test
    public void testReset() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                game.insert(x);
            }
        }
        game.reset();
        assertEquals(false, game.isFull());
        assertEquals(0, game.getInserted());
        for (int x = 0; x < width; ++x) {
            assertEquals(false, game.isFull(x));
            assertEquals(0, game.getInserted(x));
        }
    }

    @Test
    public void testTest() {

        if (width == 7 && height == 6 && wins == 4 && colors == 2) {

            // Horizontal

            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(2));
            game.insert(2);
            assertEquals(false, game.test(2));
            game.insert(2);
            assertEquals(true, game.test(3));
            assertEquals(false, game.test());
            game.insert(3);
            assertEquals(true, game.test());
            game.reset();

            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(true, game.test(width - 4));
            assertEquals(false, game.test());
            game.insert(width - 4);
            assertEquals(true, game.test());
            game.reset();

            // Vertikal

            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(true, game.test(0));
            assertEquals(false, game.test());
            game.insert(0);
            assertEquals(true, game.test());
            game.reset();

            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(true, game.test(0));
            assertEquals(false, game.test());
            game.insert(0);
            assertEquals(6, game.getInserted(0));
            assertEquals(true, game.test());
            game.reset();

            // Diagonal /

            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(2));
            game.insert(2);
            assertEquals(false, game.test(2));
            game.insert(2);
            assertEquals(false, game.test(3));
            game.insert(3);
            assertEquals(false, game.test(2));
            game.insert(2);
            assertEquals(false, game.test(3));
            game.insert(3);
            assertEquals(false, game.test(3));
            game.insert(3);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(true, game.test(3));
            assertEquals(false, game.test());
            game.insert(3);
            assertEquals(4, game.getInserted(3));
            assertEquals(true, game.test());
            game.reset();

            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 5));
            game.insert(width - 5);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);

            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(true, game.test(width - 1));
            assertEquals(false, game.test());
            game.insert(width - 1);
            assertEquals(6, game.getInserted(width - 1));
            assertEquals(true, game.test());
            game.reset();

            assertEquals(false, game.test(width - 5));
            game.insert(width - 6);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 5));
            game.insert(width - 5);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);

            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(true, game.test(width - 1));
            assertEquals(false, game.test());
            game.insert(width - 1);
            assertEquals(6, game.getInserted(width - 1));
            assertEquals(true, game.test());

            game.reset();

            // Diagonal \

            assertEquals(false, game.test(3));
            game.insert(3);
            assertEquals(false, game.test(2));
            game.insert(2);
            assertEquals(false, game.test(2));
            game.insert(2);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(1));
            game.insert(1);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(0));
            game.insert(0);
            assertEquals(false, game.test(3));
            game.insert(3);
            assertEquals(true, game.test(0));
            assertEquals(false, game.test());
            game.insert(0);
            assertEquals(4, game.getInserted(0));
            assertEquals(true, game.test());

            game.reset();

            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 5));
            game.insert(width - 5);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(true, game.test(width - 4));
            assertEquals(false, game.test());
            game.insert(width - 4);
            assertEquals(6, game.getInserted(width - 4));
            assertEquals(true, game.test());

            game.reset();

            assertEquals(false, game.test(width - 5));
            game.insert(width - 6);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 5));
            game.insert(width - 5);
            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);

            assertEquals(false, game.test(width - 1));
            game.insert(width - 1);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 2));
            game.insert(width - 2);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(false, game.test(width - 3));
            game.insert(width - 3);
            assertEquals(false, game.test(width - 4));
            game.insert(width - 4);
            assertEquals(true, game.test(width - 4));
            assertEquals(false, game.test());
            game.insert(width - 4);
            assertEquals(6, game.getInserted(width - 4));
            assertEquals(true, game.test());

            game.reset();

        }

        // -----------------

        // Horizontal

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x <= width - wins; ++x) {
                for (int w = 0; w < wins; ++w) {
                    game.reset();
                    for (int t = 0; t < wins; ++t) {
                        if (w == t) {
                            game.setInserted(x + t, y);
                        } else {
                            game.set(x + t, y, 1);
                            game.setInserted(x + t, y + 1);
                        }
                        for (int s = y - 1; s >= 0; --s) {
                            game.set(x + t, s, 2);
                        }
                    }
                    assertEquals(true, game.test(x + w));
                }
            }
        }

        // Vertikal

        for (int y = 0; y <= height - wins; ++y) { // 6 - 4 = 2 -> 0 1 2
            for (int x = 0; x < width; ++x) {
                game.reset();
                for (int t = 0; t < wins - 1; ++t) { // 4 - 1 = 3
                    game.set(x, y + t, 1);
                }
                for (int t = 0; t < y; ++t) {
                    game.set(x, t, 2);
                }
                game.setInserted(x, y + wins - 1);
                assertEquals(true, game.test(x));
            }
        }

        // Diagonal /
        for (int y = 0; y <= height - wins; ++y) { // 6 - 4 = 2 -> 0 1 2
            for (int x = 0; x <= width - wins; ++x) {
                game.reset();
                for (int w = 0; w < wins; ++w) {
                    game.reset();
                    for (int t = 0; t < wins; ++t) {
                        if (w == t) {
                            game.setInserted(x + t, y + t);
                        } else {
                            game.set(x + t, y + t, 1);
                            game.setInserted(x + t, y + t + 1);
                        }
                        for (int s = y + t - 1; s >= 0; --s) {
                            game.set(x + t, s, 2);
                        }
                    }
                    assertEquals(true, game.test(x + w));
                }
            }
        }

        // Diagonal \
        for (int y = 0; y <= height - wins; ++y) {
            for (int x = 0; x <= width - wins; ++x) {
                game.reset();
                for (int w = 0; w < wins; ++w) {
                    game.reset();
                    for (int t = 0; t < wins; ++t) {
                        if (w == t) {
                            game.setInserted(x + t, y + wins - t - 1);
                        } else {
                            game.set(x + t, y + wins - t - 1, 1);
                            game.setInserted(x + t, y + wins - t);
                        }
                        for (int s = y + wins - t - 2; s >= 0; --s) {
                            game.set(x + t, s, 2);
                        }
                    }
                    assertEquals(true, game.test(x + w));
                }
            }
        }

        Random rn = new Random();
        for (int i = 0; i < 1000; ++i) {
            game.reset();
            boolean stop = false;
            while (!stop) {
                if (game.isFull()) {
                    stop = true;
                }
                int x = rn.nextInt(width);
                if (!game.isFull(x)) {
                    if (game.test(x)) {
                        assertEquals(false, game.test());
                        game.insert(x);
                        assertEquals(true, game.test());
                        stop = true;
                    } else {
                        assertEquals(false, game.test());
                        game.insert(x);
                        assertEquals(false, game.test());
                    }
                }
            }
        }
    }

    @Test
    public void testGetBigInteger() {

        Random rn = new Random();
        for (int i = 0; i < 1000; ++i) {
            game.reset();
            boolean stop = false;
            while (!stop) {
                if (game.isFull()) {
                    stop = true;
                }
                int x = rn.nextInt(width);
                if (!game.isFull(x)) {
                    if (game.test(x)) {
                        game.insert(x);
                        stop = true;
                    } else {
                        game.insert(x);
                    }
                    BigInteger bi = game.getBigInteger();
                    Game g = new Game(width, height, colors, wins);
                    g.fromBigInteger(bi);
    
                    assertTrue(g.equals(game));
                    assertTrue(game.equals(g));
                }
            }
        }
    }

    @Test
    public void testGetBigIntegerRevert() {
        game.insert(0);

        BigInteger bi0 = game.getBigInteger();
        BigInteger bi1 = game.getBigIntegerRevert();

        System.out.println(bi0.toString(10));
        System.out.println(bi1.toString(10));

        assertFalse(bi0.equals(bi1));

        Game g2 = new Game(width, height, colors, wins);
        g2.fromBigInteger(bi1);

        BigInteger bi2 = g2.getBigIntegerRevert();

        assertTrue(bi0.equals(bi2));

        Game g3 = new Game(width, height, colors, wins);
        g3.fromBigInteger(bi2);

        assertTrue(game.equals(g3));

        Random rn = new Random();
        for (int i = 0; i < 1000; ++i) {
            game.reset();
            boolean stop = false;
            while (!stop) {
                if (game.isFull()) {
                    stop = true;
                }
                int x = rn.nextInt(width);
                if (!game.isFull(x)) {
                    if (game.test(x)) {
                        game.insert(x);
                        stop = true;
                    } else {
                        game.insert(x);
                    }

                    bi0 = game.getBigInteger();
                    bi1 = game.getBigIntegerRevert();

                    g2 = new Game(width, height, colors, wins);
                    g2.fromBigInteger(bi1);

                    bi2 = g2.getBigIntegerRevert();

                    assertTrue(bi0.equals(bi2));

                    g3 = new Game(width, height, colors, wins);
                    g3.fromBigInteger(bi2);
            
                    assertTrue(game.equals(g3));
                }
            }
        }
    }
}
