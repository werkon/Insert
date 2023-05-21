package de.esnecca.multi;

import static org.junit.Assert.assertEquals;

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
    public void testGetCurrentColor() {
        assertEquals(1, game.getCurrentColor());
        game.insert(width - 1);
        assertEquals(2, game.getCurrentColor());
        game.insert(width - 1);
        assertEquals(1, game.getCurrentColor());
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
        assertEquals(0, game.getInserted());
        assertEquals(1, game.getCurrentColor());
        assertEquals(0, game.get(width - 1, game.getInserted(width - 1)));
        game.insert(width - 1);
        assertEquals(1, game.getInserted());
        assertEquals(1, game.getInserted(width - 1));
        assertEquals(2, game.getCurrentColor());
        assertEquals(1, game.get(width - 1, game.getInserted(width - 1) - 1));
        game.insert(width - 1);
        assertEquals(2, game.getInserted());
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(1, game.getCurrentColor());
        assertEquals(2, game.get(width - 1, game.getInserted(width - 1) - 1));
        game.insert(0);
        assertEquals(3, game.getInserted());
        assertEquals(1, game.getInserted(0));
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(2, game.getCurrentColor());
        assertEquals(1, game.get(0, game.getInserted(0) - 1));
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
        game.insert(width - 1);
        game.insert(width - 1);
        game.insert(0);

        //        2
        //  1     1

        assertEquals(3, game.getInserted());
        assertEquals(1, game.getInserted(0));
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(2, game.getCurrentColor());
        assertEquals(1, game.get(0, 0));
        assertEquals(2, game.get(width - 1, game.getInserted(width - 1) - 1));

        game.remove(0);

        //        2
        //        1

        assertEquals(2, game.getInserted());
        assertEquals(0, game.getInserted(0));
        assertEquals(2, game.getInserted(width - 1));
        assertEquals(1, game.getCurrentColor());
        assertEquals(0, game.get(0, 0));
        assertEquals(2, game.get(width - 1, game.getInserted(width - 1) - 1));

        game.remove(width - 1);

        //        
        //        1

        assertEquals(1, game.getInserted());
        assertEquals(0, game.getInserted(0));
        assertEquals(1, game.getInserted(width - 1));
        assertEquals(2, game.getCurrentColor());
        assertEquals(0, game.get(0, 0));
        assertEquals(1, game.get(width - 1, game.getInserted(width - 1) - 1));

        game.remove(width - 1);

        //        
        //        

        assertEquals(0, game.getInserted());
        assertEquals(0, game.getInserted(0));
        assertEquals(0, game.getInserted(width - 1));
        assertEquals(1, game.getCurrentColor());
        assertEquals(0, game.get(0, 0));
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
            assertEquals(false,game.isFull(x));
            assertEquals(0, game.getInserted(x));
        }

    }
}
