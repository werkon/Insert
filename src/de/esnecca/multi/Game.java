package de.esnecca.multi;

import java.math.BigInteger;

public class Game extends Field {

    private int colors;
    private int wins;
    private int inserted;
    private int currentColor;
    private int[] insertedRow;

    public Game(int width, int height, int colors, int wins) {
        super(width, height);
        this.colors = colors;
        this.wins = wins;
        insertedRow = new int[width];
        reset();
    }

    public Game(Game g) {
        super(g);
        colors = g.colors;
        wins = g.wins;
        inserted = g.inserted;
        currentColor = g.currentColor;
        insertedRow = new int[getWidth()];
        for (int x = 0; x < getWidth(); ++x) {
            insertedRow[x] = g.insertedRow[x];
        }
    }

    public void reset() {
        super.reset();
        for (int x = 0; x < getWidth(); ++x) {
            insertedRow[x] = 0;
        }
        inserted = 0;
        currentColor = 1;
    }

    public void insert(int x) {
        set(x, insertedRow[x]++, currentColor);
        ++currentColor;
        if (currentColor > colors) {
            currentColor = 1;
        }
        ++inserted;
    }

    public void remove(int x) {
        set(x, --insertedRow[x], 0);
        --currentColor;
        if (currentColor <= 0) {
            currentColor = colors;
        }
        --inserted;
    }

    public int getColors() {
        return colors;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public int getInserted(int x) {
        return insertedRow[x];
    }

    public int setInserted(int x, int inserted) {
        return insertedRow[x] = inserted;
    }

    public int getInserted() {
        return inserted;
    }

    public boolean isFull() {
        return getInserted() >= getSize();
    }

    public boolean isFull(int x) {
        return getInserted(x) >= getHeight();
    }

    public int getWins() {
        return wins;
    }

    public boolean test() {

        // Horizontal

        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x <= getWidth() - wins; ++x) {
                int color = get(x, y);
                if (color > 0) {
                    boolean eq = true;
                    for (int w = 1; w < wins; ++w) {
                        if (get(x + w, y) != color) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        return true;
                    }
                }
            }
        }

        // Vertikal

        for (int x = 0; x < getWidth(); ++x) {
            for (int y = 0; y <= getHeight() - wins; ++y) {
                int color = get(x, y);
                if (color > 0) {
                    boolean eq = true;
                    for (int w = 1; w < wins; ++w) {
                        if (get(x, y + w) != color) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        return true;
                    }
                }
            }
        }

        // Diagonal /

        for (int x = 0; x <= getWidth() - wins; ++x) { // 0 1 2 3
            for (int y = 0; y <= getHeight() - wins; ++y) { // 0 1 2
                int color = get(x, y);
                if (color > 0) {
                    boolean eq = true;
                    for (int w = 1; w < wins; ++w) { // 1 2 3
                        if (get(x + w, y + w) != color) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        return true;
                    }
                }
            }
        }

        // Diagonal \

        for (int x = 0; x <= getWidth() - wins; ++x) { // 0 1 2 3
            for (int y = 0; y <= getHeight() - wins; ++y) { // 0 1 2
                int color = get(x, y + wins - 1);
                if (color > 0) {
                    boolean eq = true;
                    for (int w = 1; w < wins; ++w) { // 1 2 3
                        if (get(x + w, y + wins - w - 1) != color) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean test(int x) {

        // Horizontal
        int count = 1;
        int y = getInserted(x);
        for (int i = 1; x + i < getWidth(); ++i) {
            int color = get(x + i, y);
            if (color == currentColor) {
                ++count;
            } else {
                break;
            }
        }
        for (int i = 1; x - i >= 0; ++i) {
            int color = get(x - i, y);
            if (color == currentColor) {
                ++count;
            } else {
                break;
            }
        }
        if (count >= wins) {
            return true;
        }

        // Vertikal
        count = 1;
        y = getInserted(x);
        for (int i = 1; y - i >= 0; ++i) {
            int color = get(x, y - i);
            if (color == currentColor) {
                ++count;
            } else {
                break;
            }
        }
        if (count >= wins) {
            return true;
        }

        // Diagonal /

        count = 1;
        y = getInserted(x);
        for (int i = 1; x + i < getWidth() && y + i < getHeight(); ++i) {
            int color = get(x + i, y + i);
            if (color == currentColor) {
                ++count;
            } else {
                break;
            }
        }
        for (int i = 1; x - i >= 0 && y - i >= 0; ++i) {
            int color = get(x - i, y - i);
            if (color == currentColor) {
                ++count;
            } else {
                break;
            }
        }
        if (count >= wins) {
            return true;
        }

        // Diagonal \

        count = 1;
        y = getInserted(x);
        for (int i = 1; x + i < getWidth() && y - i >= 0; ++i) {
            int color = get(x + i, y - i);
            if (color == currentColor) {
                ++count;
            } else {
                break;
            }
        }
        for (int i = 1; x - i >= 0 && y + i < getHeight(); ++i) {
            int color = get(x - i, y + i);
            if (color == currentColor) {
                ++count;
            } else {
                break;
            }
        }
        if (count >= wins) {
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        Game g = (Game) obj;

        if (g.colors != colors) {
            return false;
        }
        if (g.wins != wins) {
            return false;
        }
        if (g.inserted != inserted) {
            return false;
        }
        if (g.currentColor != currentColor) {
            return false;
        }

        for (int x = 0; x < getWidth(); ++x) {
            if (g.insertedRow[x] != insertedRow[x]) {
                return false;
            }
        }
        return true;
    }

    public void print() {
        System.out.print("   ");
        for (int x = 0; x < getWidth(); ++x) {
            System.out.format("%2d ", (x + 1));
        }
        System.out.println("");
        System.out.print("   ");
        for (int x = 0; x < getWidth(); ++x) {
            System.out.print("   |");
        }
        System.out.println("");
        System.out.println("");
        
        super.print();
        System.out.print("   ");
        for (int x = 0; x < getWidth(); ++x) {
            System.out.print("   |");
        }
        System.out.println("");
        System.out.print("   ");
        for (int x = 0; x < getWidth(); ++x) {
            System.out.format("%2d ",getInserted(x));
        }
        System.out.println(" - " + getInserted());
    }

    public BigInteger getBigIntegerRevert() {
        BigInteger i = BigInteger.valueOf(0);

        BigInteger m = BigInteger.valueOf(getColors());
        for (int x = getWidth() - 1; x >= 0; --x) {
            for (int y = 0; y < getInserted(x); ++y) {
                i = i.multiply(m);
                i = i.add(BigInteger.valueOf(get(x, y) - 1));
            }
        }
        m = BigInteger.valueOf(getHeight() + 1);
        for (int x = getWidth() - 1; x >= 0; --x) {
            i = i.multiply(m);
            i = i.add(BigInteger.valueOf(getInserted(x)));
        }
        return i;
    }

    public BigInteger getBigInteger() {
        BigInteger i = BigInteger.valueOf(0);

        BigInteger m = BigInteger.valueOf(getColors());
        for (int x = 0; x < getWidth(); ++x) {
            for (int y = 0; y < getInserted(x); ++y) {
                i = i.multiply(m);
                i = i.add(BigInteger.valueOf(get(x, y) - 1));
            }
        }
        m = BigInteger.valueOf(getHeight() + 1);
        for (int x = 0; x < getWidth(); ++x) {
            i = i.multiply(m);
            i = i.add(BigInteger.valueOf(getInserted(x)));
        }
        return i;
    }

    public BigInteger getSmallestBigInteger() {
        BigInteger b1 = getBigInteger();
        BigInteger b2 = getBigIntegerRevert();

        if (b1.compareTo(b2) < 0) {
            return b1;
        } else {
            return b2;
        }
    }

    public void fromBigInteger(BigInteger bi) {
        reset();
        BigInteger d = BigInteger.valueOf(getHeight() + 1);
        for (int x = getWidth() - 1; x >= 0; --x) {
            BigInteger[] dr = bi.divideAndRemainder(d);
            int i = dr[1].intValueExact();
            insertedRow[x] = i;
            inserted += i;
            bi = dr[0];
        }

        d = BigInteger.valueOf(getColors());
        for (int x = getWidth() - 1; x >= 0; --x) {
            for (int y = getInserted(x) - 1; y >= 0; --y) {
                BigInteger[] dr = bi.divideAndRemainder(d);
                int i = dr[1].intValueExact() + 1;
                set(x, y, i);
                bi = dr[0];
            }
        }
        currentColor = inserted % colors + 1;
    }
}
