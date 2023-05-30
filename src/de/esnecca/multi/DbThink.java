package de.esnecca.multi;

import java.math.BigInteger;
import java.sql.SQLException;

import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.db.DbEntry;
import de.esnecca.multi.hash.HashEntry;
import de.esnecca.multi.hash.HashTable;

public class DbThink extends Thread {

    private Game game;
    private int x;
    private HashTable hashTable;
    private DbConnection dbConnection;
    private DbThinkLimits limits;
    private boolean show;

    private int gameid;
    private long written;

    public DbThink(Game game, int x, HashTable hashTable, DbConnection dbConnection, DbThinkLimits limits, boolean show) throws SQLException {
        this.game = new Game(game);
        this.x = x;
        this.hashTable = hashTable;
        this.dbConnection = dbConnection;
        this.limits = limits;
        this.show = show;

        gameid = dbConnection.getGameId(game);

        // hashLimit = game.getSize() - 9;
        // dbLimit = 24; // game.getSize() / 2;
        // printLimit = 16; // game.getSize() / 2;
        written = 0;
    }

    public static class DbThinkLimits{

        private int hashLimit;
        private int dbLimit;
        private int printLimit;

        public DbThinkLimits(int hashLimit, int dbLimit, int printLimit) {
            this.hashLimit = hashLimit;
            this.dbLimit = dbLimit;
            this.printLimit = printLimit;
        }

        public int getHashLimit() {
            return hashLimit;
        }

        public int getDbLimit() {
            return dbLimit;
        }

        public int getPrintLimit() {
            return printLimit;
        }
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
        if (game.getInserted() < limits.getHashLimit()) {
            bi = game.getSmallestBigInteger();
            HashEntry hashEntry = hashTable.get(bi);
            if (hashEntry != null && hashEntry.getValue().equals(bi)) {
                game.remove(x);
                return hashEntry.getResult();
            }
            if (game.getInserted() < limits.getDbLimit()) {
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
                if (game.getInserted() < limits.getDbLimit()) {
                    DbEntry dbEntry = new DbEntry(bi, gameid, ret, game.getInserted() + 1);
                    dbConnection.createEntry(dbEntry);
                    ++written;
                    if (game.getInserted() < limits.getPrintLimit()) {
                        Game game2 = new Game(game);
                        game2.insert(x);
                        print(game2, ret);
                    }
                }
            }
            return ret;
        }
        if (unentschieden) {
            int ret = 0;
            if (bi != null) {
                HashEntry hashEntry = new HashEntry(bi, ret);
                hashTable.set(hashEntry);
                if (game.getInserted() < limits.getDbLimit()) {
                    DbEntry dbEntry = new DbEntry(bi, gameid, ret, game.getInserted() + 1);
                    dbConnection.createEntry(dbEntry);
                    ++written;
                    if (game.getInserted() < limits.getPrintLimit()) {
                        Game game2 = new Game(game);
                        game2.insert(x);
                        print(game2, ret);
                    }
                }
            }
            return ret;
        }

        int ret = (groesstesNegativesR * (-1)) + 1;
        if (bi != null) {
            HashEntry hashEntry = new HashEntry(bi, ret);
            hashTable.set(hashEntry);
            if (game.getInserted() < limits.getDbLimit()) {
                DbEntry dbEntry = new DbEntry(bi, gameid, ret, game.getInserted() + 1);
                dbConnection.createEntry(dbEntry);
                ++written;
                if (game.getInserted() < limits.getPrintLimit()) {
                    Game game2 = new Game(game);
                    game2.insert(x);
                    print(game2, ret);
                }
        }
        }
        return ret;
    }

    public long getWritten() {
        return written;
    }

    @Override
    public void run() {
        try {
            int ret = think(x);
            dbConnection.close();
            if (show) {
                System.out.println("x: " + (x + 1) + " result: " + ret);
            }
        } catch (SQLException sex) {
            System.out.println(sex);
        }
    }

    synchronized public void print(Game game, int ret){
        game.print();
        System.out.println("   result: " + ret);
        System.out.println("");
        System.out.flush();
    }
}
