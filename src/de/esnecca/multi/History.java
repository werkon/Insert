package de.esnecca.multi;

public class History extends Game {

    private int[] history;

    public History(int width, int height, int colors, int wins) {
        super(width, height, colors, wins);

        history = new int[getSize()];
    }

    public History(History h) {
        super(h);
        history = new int[getSize()];
        for (int x = 0; x < getSize(); ++x) {
            history[x] = h.history[x];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        History h = (History) obj;

        for (int i = 0; i < getInserted(); ++i) {
            if (h.history[i] != history[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void insert(int x) {
        history[getInserted()] = x;
        super.insert(x);
    }

    public int getHistory(int i) {
        if (getInserted() > i) {
            return history[i];
        } else {
            return -1;
        }
    }

    public void print(Integer[] result) {
        print();
        System.out.print("    ");
        for (int x = 0; x < result.length; ++x) {
            System.out.format("  |");
            if (x == getHistory(0)) {
                if (x == getHistory(1)) {
                    System.out.format("³");
                } else {
                    System.out.format("¹");
                }
            } else {
                if (x == getHistory(1)) {
                    System.out.format("²");
                } else {
                    System.out.format(" ");
                }
            }
        }
        System.out.println("");
        System.out.format("    ");
        for (int x = 0; x < result.length; ++x) {
            if (isFull(x)) {
                System.out.format("  x ");
            } else {
                if (result[x] == null) {
                    System.out.format("    ");
                } else {
                    System.out.format("%3d ", result[x].intValue());
                }
            }
        }
        System.out.println("");
    }

    public void remove() {
        remove(history[getInserted() - 1]);
    }

}
