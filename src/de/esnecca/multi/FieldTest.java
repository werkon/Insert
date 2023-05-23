package de.esnecca.multi;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FieldTest {

    private final int width = 7;
    private final int height = 6;

    private Field field;

    @Before
    public void init() {
        field = new Field(width, height);
    }

    @Test
    public void testCopyConstructor() {
        field.set(0, 0, 1);
        Field f = new Field(field);
        assertEquals(1, f.get(0, 0));
        field.set(0, 0, 2);
        assertEquals(2, field.get(0, 0));
        assertEquals(1, f.get(0, 0));

        assertEquals(f.getWidth(), field.getWidth());
        assertEquals(f.getHeight(), field.getHeight());
    }

    @Test
    public void testGetHeight() {
        assertEquals(height, field.getHeight());
    }

    @Test
    public void testGetSize() {
        assertEquals(width * height, field.getSize());
    }

    @Test
    public void testGetWidth() {
        assertEquals(width, field.getWidth());
    }

    @Test
    public void testReset() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                field.set(x, y, x * height + y);
            }
        }
        field.reset();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                assertEquals(0, field.get(x, y));
            }
        }
    }

    @Test
    public void testSet() {
        field.set(0, 0, 1);
        field.set(width - 1, height - 1, 2);
        assertEquals(1, field.get(0, 0));
        assertEquals(2, field.get(width - 1, height - 1));

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                field.set(x, y, x * height + y);
            }
        }
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                assertEquals(x * height + y, field.get(x, y));
            }
        }
    }

}
