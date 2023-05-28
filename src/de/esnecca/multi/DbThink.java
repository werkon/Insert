package de.esnecca.multi;

import java.math.BigInteger;
import java.sql.SQLException;

import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.db.DbEntry;
import de.esnecca.multi.hash.HashEntry;
import de.esnecca.multi.hash.HashTable;

public class DbThink implements Runnable {

    private Game game;
    private int x;
    private HashTable hashTable;
    private DbConnection dbConnection;
    private boolean show;
    private int gameid;

    public DbThink(Game game, int x, HashTable hashTable, DbConnection dbConnection, boolean show) throws SQLException {
        this.game = new Game(game);
        this.x = x;
        this.hashTable = hashTable;
        this.dbConnection = dbConnection;
        this.show = show;
        gameid = dbConnection.getGameId(game);
    }

    public int think(int x) throws SQLException {
        if (game.test(x)) {
            return 1;
        }
        if (game.getInserted() >= game.getSize() - 1) {
            return 0;
        }

        game.insert(x);

        BigInteger bi = null;
        if (game.getInserted() < game.getSize() - 8) {
            bi = game.getSmallestBigInteger();
            HashEntry hashEntry = hashTable.get(bi);
            if (hashEntry != null && hashEntry.getValue().equals(bi)) {
                game.remove(x);
                return hashEntry.getResult();
            }
            if (game.getInserted() < game.getSize() - 16) {
                DbEntry dbEntry = dbConnection.getDbEntry(bi, gameid);
                if (dbEntry != null) {
                    game.remove(x);
                    return dbEntry.getResult();
                }
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
                if (game.getInserted() < game.getSize() - 16) {
                    DbEntry dbEntry = new DbEntry(bi, gameid, ret, game.getInserted() + 1);
                    dbConnection.createEntry(dbEntry);
                }
            }
            return ret;
        }
        if (unentschieden) {
            int ret = 0;
            if (bi != null) {
                HashEntry hashEntry = new HashEntry(bi, ret);
                hashTable.set(hashEntry);
                if (game.getInserted() < game.getSize() - 16) {
                    DbEntry dbEntry = new DbEntry(bi, gameid, ret, game.getInserted() + 1);
                    dbConnection.createEntry(dbEntry);
                }
            }
            return ret;
        }

        int ret = (groesstesNegativesR * (-1)) + 1;
        if (bi != null) {
            HashEntry hashEntry = new HashEntry(bi, ret);
            hashTable.set(hashEntry);
            if (game.getInserted() < game.getSize() - 16) {
                DbEntry dbEntry = new DbEntry(bi, gameid, ret, game.getInserted() + 1);
                dbConnection.createEntry(dbEntry);
            }
        }
        return ret;
    }

    @Override
    public void run() {
        try {
            int ret = think(x);
            dbConnection.close();
            if (show) {
                System.out.println("x: " + (x + 1) + " r: " + ret);
            }
        } catch (SQLException sex) {
            System.out.println(sex);
        }
    }

}
