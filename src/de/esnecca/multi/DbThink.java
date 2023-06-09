package de.esnecca.multi;

import java.math.BigInteger;
import java.sql.SQLException;

import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.db.DbEntry;
import de.esnecca.multi.hash.HashEntry;
import de.esnecca.multi.hash.HashTable;
import de.esnecca.multi.reserve.Reserve;

public class DbThink extends Thread {

    private History history;
    private int x;
    private HashTable hashTable;
    private DbConnection dbConnection;
    private DbThinkLimits limits;
    private Reserve reserve;

    private int gameid;
    private long written;
    private long collisions;

    public DbThink(History history, int x, HashTable hashTable, DbConnection dbConnection, DbThinkLimits limits,
            Reserve reserve)
            throws SQLException {
        this.history = new History(history);
        this.x = x;
        this.hashTable = hashTable;
        this.dbConnection = dbConnection;
        this.limits = limits;
        this.reserve = reserve;

        gameid = dbConnection.getGameId(history);

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
        // if (game.test(x)) {
        // return 1;
        // }
        if (history.getInserted() >= history.getSize() - 1) {
            return 0;
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

        BigInteger bi = null;
        int biInserted = history.getInserted();

        if (history.getInserted() <= limits.getHashLimit()) {
            bi = history.getSmallestBigInteger();
            HashEntry hashEntry = hashTable.get(bi);
            if (hashEntry != null && hashEntry.getValue().equals(bi)) {
                history.remove(x);
                return hashEntry.getResult();
            }
            if (biInserted <= limits.getDbLimit()) {
                DbEntry dbEntry = dbConnection.getDbEntry(bi, gameid);

                if (dbEntry == null) {
                    if (biInserted >= 4 && !reserve.reserve(bi, biInserted)) {
                        reserve.incSleeping();
                        while (true) {
                            try {
                                Thread.sleep(1000);
                                // System.out.println("wait " + biInserted);
                            } catch (InterruptedException e) {
                            }
                            dbEntry = dbConnection.getDbEntry(bi, gameid);
                            if (dbEntry != null) {
                                reserve.decSleeping();
                                history.remove(x);
                                return dbEntry.getResult();
                            }
                        }
                    }
                } else {
                    history.remove(x);
                    return dbEntry.getResult();
                }
            }
        }

        int kleinstesPositivesR = 0;
        int groesstesNegativesR = 0;
        boolean unentschieden = false;
        for (int i = 0; i < history.getWidth(); ++i) {
            if (!history.isFull(i)) {
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

        history.remove(x);

        if (kleinstesPositivesR > 0) {

            int ret = (kleinstesPositivesR + 1) * (-1);
            if (bi != null) {
                HashEntry hashEntry = new HashEntry(bi, ret);
                hashTable.set(hashEntry);
                if (biInserted <= limits.getDbLimit()) {
                    DbEntry dbEntry = new DbEntry(bi, gameid, ret, biInserted);
                    if (dbConnection.createEntry(dbEntry)) {
                        ++written;
                        if (history.getInserted() < limits.getPrintLimit()) {
                            history.insert(x);
                            print(history, ret);
                            System.out.println("");
                            history.remove(x);
                        }
                    } else {
                        ++collisions;
                    }
                    reserve.free(bi);
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
                    if (dbConnection.createEntry(dbEntry)) {
                        ++written;
                        if (history.getInserted() < limits.getPrintLimit()) {
                            history.insert(x);
                            print(history, ret);
                            System.out.println("");
                            history.remove(x);
                        }
                    } else {
                        ++collisions;
                    }
                    reserve.free(bi);
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
                if (dbConnection.createEntry(dbEntry)) {
                    ++written;
                    if (history.getInserted() < limits.getPrintLimit()) {
                        history.insert(x);
                        print(history, ret);
                        System.out.println("");
                        history.remove(x);
                    }
                } else {
                    ++collisions;
                }
                reserve.free(bi);
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

    static synchronized public void print(Game game, int ret) {
        game.print();
        System.out.format("      result: %d", ret);
        System.out.println("");
        System.out.println("");
        System.out.flush();
    }
}
