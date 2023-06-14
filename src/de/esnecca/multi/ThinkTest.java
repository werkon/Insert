package de.esnecca.multi;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class ThinkTest {

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
    public void testThink() {
        history.insert(3);
        history.insert(3);
        history.insert(3);
        history.insert(3);
        history.insert(3);
        history.insert(3);
        history.insert(0);
        history.insert(4);
        history.insert(4);
        history.insert(4);
        history.insert(4);
        history.insert(4);
        history.insert(4);
        history.insert(1);
        history.insert(2);
        history.insert(2);
        history.insert(2);
        history.insert(2);
        history.insert(2);
        history.insert(2);
        history.insert(5);
        history.insert(5);
        history.insert(5);

        // history.insert(5);
        // history.insert(5);
        // history.insert(5);

        // history.insert(6);
        // history.insert(6);
        // history.insert(6);
        // history.insert(6);
        // history.insert(6);
        // history.insert(6);

        // history.insert(0);
        // history.insert(0);
        // history.insert(0);
        // history.insert(0);
        // history.insert(0);

        // history.insert(1);
        // history.insert(1);
        // history.insert(1);
        // history.insert(1);

        Think think1 = new Think(history);
        Think think2 = new Think(history);

        int a = think1.think(5);
        int b = think2.think2(5);

        assertEquals(a, b);
    }

    @Test
    public void testThink2() {
        Random rn = new Random();
        for (int i = 0; i < 100; ++i) {
            history.reset();
            System.out.println(i);

            for (int j = 0; j < 26; ++j) {
                while (true) {
                    int x = rn.nextInt(width);
                    if (!history.isFull(x)) {
                        if (!history.test(x)) {
                            history.insert(x);
                            break;
                        }
                    }
                }
            }

            Think think1 = new Think(history);
            Think think2 = new Think(history);

            while (true) {
                int x = rn.nextInt(width);
                if (!history.isFull(x)) {
                    int a = think1.think(x);
                    int b = think2.think2(x);
                    if( a != b){
                        history.print();
                        System.out.println("x: " + x);
                    }
                    assertEquals(a, b);
                    break;
                }
            }
        }

    }
}
