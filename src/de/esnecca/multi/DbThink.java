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

    private int gameid;
    private long written;
    private long collisions;

    public DbThink(Game game, int x, HashTable hashTable, DbConnection dbConnection, DbThinkLimits limits)
            throws SQLException {
        this.game = new Game(game);
        this.x = x;
        this.hashTable = hashTable;
        this.dbConnection = dbConnection;
        this.limits = limits;

        gameid = dbConnection.getGameId(game);

        // hashLimit = game.getSize() - 9;
        // dbLimit = 24; // game.getSize() / 2;
        // printLimit = 16; // game.getSize() / 2;
        written = 0;
        collisions = 0;
    }

    public static class DbThinkLimits {

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
        int biInserted = game.getInserted();

        if (game.getInserted() <= limits.getHashLimit()) {
            bi = game.getSmallestBigInteger();
            HashEntry hashEntry = hashTable.get(bi);
            if (hashEntry != null && hashEntry.getValue().equals(bi)) {
                game.remove(x);
                return hashEntry.getResult();
            }
            if (biInserted <= limits.getDbLimit()) { // 23 nun 24 / Limit: 24
                System.out.format("READ biInserted: %d, dbLimit: %d%n", biInserted, limits.getDbLimit());
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
                if (biInserted <= limits.getDbLimit()) { // vorher 23 < 24 nun 24 <= 24 Limit: 24
                    System.out.format("WRITE biInserted: %d, dbLimit: %d%n", biInserted, limits.getDbLimit());
                    DbEntry dbEntry = new DbEntry(bi, gameid, ret, biInserted);
                    if (dbConnection.createEntry(dbEntry)) {
                        ++written;
                    } else {
                        ++collisions;
                    }
                    if (game.getInserted() < limits.getPrintLimit()) {
                        game.insert(x);
                        print(game, ret);
                        game.remove(x);
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
                if (biInserted <= limits.getDbLimit()) {
                    DbEntry dbEntry = new DbEntry(bi, gameid, ret, biInserted);
                    System.out.format("WRITE biInserted: %d, dbLimit: %d%n", biInserted, limits.getDbLimit());
                    if (dbConnection.createEntry(dbEntry)) {
                        ++written;
                    } else {
                        ++collisions;
                    }
                    if (game.getInserted() < limits.getPrintLimit()) {
                        game.insert(x);
                        print(game, ret);
                        game.remove(x);
                    }
                }
            }
            return ret;
        }

        int ret = (groesstesNegativesR * (-1)) + 1;
        if (bi != null) {
            HashEntry hashEntry = new HashEntry(bi, ret);
            hashTable.set(hashEntry);
            if (biInserted <= limits.getDbLimit()) {
                DbEntry dbEntry = new DbEntry(bi, gameid, ret, biInserted);
                System.out.format("WRITE biInserted: %d, dbLimit: %d%n", biInserted, limits.getDbLimit());
                if (dbConnection.createEntry(dbEntry)) {
                    ++written;
                } else {
                    ++collisions;
                }
                if (game.getInserted() < limits.getPrintLimit()) {
                    game.insert(x);
                    print(game, ret);
                    game.remove(x);
                }
            }
        }
        return ret;
    }

    public long getWritten() {
        return written;
    }

    public long getCollisions() {
        return collisions;
    }

    @Override
    public void run() {
        try {
            think(x);
            dbConnection.close();
        } catch (SQLException sex) {
            System.out.println(sex);
        }
    }

    synchronized public void print(Game game, int ret) {
        game.print();
        System.out.format("     result: %d", ret);
        System.out.println("");
        System.out.flush();
    }
}
