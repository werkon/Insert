package de.esnecca.multi;

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

    public void print() {
        System.out.print("   ");
        for (int x = 0; x < getWidth(); ++x) {
            System.out.print(" " + (x + 1));
        }
        System.out.println("");
        System.out.print("   ");
        for (int x = 0; x < getWidth(); ++x) {
            System.out.print(" |");
        }
        System.out.println("");
        super.print();
        System.out.print("   ");
        for (int x = 0; x < getWidth(); ++x) {
            System.out.print(" |");
        }
        System.out.println("");
        System.out.print("   ");
        for (int x = 0; x < getWidth(); ++x) {
            System.out.print(" " + getInserted(x));
        }
        System.out.println("");
        System.out.println("");
    }
}
