package de.esnecca.multi;

import java.math.BigInteger;
import de.esnecca.multi.hash.HashEntry;
import de.esnecca.multi.hash.HashTable;

public class HashThink extends Thread {

    private Game game;
    private int x;
    private HashTable hashTable;
    private int hashLimit;
    int result;

    public HashThink(Game game, int x, HashTable hashTable, int hashLimit) {
        this.game = new Game(game);
        this.x = x;
        this.hashTable = hashTable;
        this.hashLimit = hashLimit;
        this.result = 0;
    }


    public int think(int x) {
        // if (game.test(x)) {
        //     return 1;
        // }
        if (game.getInserted() >= game.getSize() - 1) {
            return 0;
        }

        game.insert(x);

        for (int i = 0; i < game.getWidth(); ++i) {
            if (!game.isFull(i)) {
                if(game.test(i)){
                    game.remove(x);
                    return -2;
                }
            }
        }

        BigInteger bi = null;
        if (game.getInserted() <= hashLimit) {
            bi = game.getSmallestBigInteger();
            HashEntry hashEntry = hashTable.get(bi);
            if (hashEntry != null && hashEntry.getValue().equals(bi)) {
                game.remove(x);
                return hashEntry.getResult();
            }
        }

        int kleinstesPositivesR = 0;
        int groesstesNegativesR = 0;
        boolean unentschieden = false;
        for (int i = 0; i < game.getWidth(); ++i) {
            if (!game.isFull(i)) {
                int r = think(i);
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

        game.remove(x);

        if (kleinstesPositivesR > 0) {

            int ret = (kleinstesPositivesR + 1) * (-1);
            if (bi != null) {
                HashEntry hashEntry = new HashEntry(bi, ret);
                hashTable.set(hashEntry);
            }
            return ret;
        }
        if (unentschieden) {
            int ret = 0;
            if (bi != null) {
                HashEntry hashEntry = new HashEntry(bi, ret);
                hashTable.set(hashEntry);
            }
            return ret;
        }

        int ret = (groesstesNegativesR * (-1)) + 1;
        if (bi != null) {
            HashEntry hashEntry = new HashEntry(bi, ret);
            hashTable.set(hashEntry);
        }
        return ret;
    }

    public int getResult() {
        return result;
    }

    @Override
    public void run() {
        result = think(x);
    }

}
