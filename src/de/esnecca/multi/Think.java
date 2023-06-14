package de.esnecca.multi;

import java.time.temporal.IsoFields;

public class Think {

    private History history;

    public Think(History history) {
        this.history = new History(history);
    }

    public int think(int x) {

        if (history.test(x)) {
            return 1;
        }

        history.insert(x);

        for (int i = 0; i < history.getWidth(); ++i) {
            if (!history.isFull(i)) {
                if (history.test(i)) {
                    history.remove(x);
                    return -2;
                }
            }
        }

        history.remove(x);

        return _think(x);
    }

    private int _think(int x) {

        if (history.getInserted() >= history.getSize() - 1) {
            return 0;
        }

        // max 40

        history.insert(x);

        // max 41

        for (int i = 0; i < history.getWidth(); ++i) {
            if (!history.isFull(i)) {
                if (history.test(i)) {
                    history.remove(x);
                    return -2;
                }
            }
        }

        int kleinstesPositivesR = 0;
        int groesstesNegativesR = 0;
        boolean unentschieden = false;
        for (int i = 0; i < history.getWidth(); ++i) {
            if (!history.isFull(i)) {
                int r = _think(i);
                if (r > 0) {
                    if (kleinstesPositivesR == 0 || r < kleinstesPositivesR) {
                        kleinstesPositivesR = r;
                    }
                } else {
                    if (r < 0) {
                        if (r < groesstesNegativesR) {
                            groesstesNegativesR = r;
                        }
                    } else {
                        unentschieden = true;
                    }
                }
            }
        }

        history.remove(x);

        if (kleinstesPositivesR > 0) {

            int ret = (kleinstesPositivesR + 1) * (-1);
            return ret;
        }
        if (unentschieden) {
            int ret = 0;
            return ret;
        }

        int ret = (groesstesNegativesR * (-1)) + 1;
        return ret;
    }

    public int think2(int x) {

        if (history.test(x)) {
            return 1;
        }

        history.insert(x);

        if(history.isFull()){
            history.remove(x);
            return 0;
        }

        int kleinstesPositivesR = 0;
        int groesstesNegativesR = 0;
        boolean unentschieden = false;
        for (int i = 0; i < history.getWidth(); ++i) {
            if (!history.isFull(i)) {
                int r = think2(i);
                if (r > 0) {
                    if (kleinstesPositivesR == 0 || r < kleinstesPositivesR) {
                        kleinstesPositivesR = r;
                    }
                } else {
                    if (r < 0) {
                        if (r < groesstesNegativesR) {
                            groesstesNegativesR = r;
                        }
                    } else {
                        unentschieden = true;
                    }
                }
            }
        }

        history.remove(x);

        if (kleinstesPositivesR > 0) {

            int ret = (kleinstesPositivesR + 1) * (-1);
            return ret;
        }
        if (unentschieden) {
            int ret = 0;
            return ret;
        }

        int ret = (groesstesNegativesR * (-1)) + 1;
        return ret;
    }

}
