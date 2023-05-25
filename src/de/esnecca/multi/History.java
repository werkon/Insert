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
        if(!super.equals(obj)){
            return false;
        }
        History h = (History) obj;

        for(int i = 0; i < getInserted(); ++i){
            if(h.history[i] != history[i]){
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

    public void remove() {
        remove(history[getInserted() - 1]);
    }

}
