import de.esnecca.multi.History;
import de.esnecca.multi.db.Db;
import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.hash.HashTable;
import de.esnecca.multi.reserve.Reserve;
import de.esnecca.multi.DbThink;

public class Database {

    public static void main(String[] args) throws Exception {

        History history = new History(7, 6, 2, 4);
        DbThink.DbThinkLimits dbThinkLimits = new DbThink.DbThinkLimits(history.getSize() - 10, 20, 10);
        HashTable hashTable = new HashTable(1000 * 1000 * 250);
        Reserve reserve = new Reserve();
        Db db = new Db("insert", "insert", "jdbc:postgresql://localhost:5432/insert");
        DbConnection dbConnection = new DbConnection(db);
        if (dbConnection.getGameId(history) <= 0) {
            dbConnection.createGame(history);
        }
        dbConnection.close();

        long owritten = 0;
        long ocollisions = 0;
        long ohits = 0;
        long onohits = 0;
        long otested = 0;

        DbThink[] threads = new DbThink[history.getWidth() * history.getWidth()];
        for (int x = 0; x < history.getWidth(); ++x) {
            if (!history.isFull(x)) {
                history.insert(x);
                for (int x2 = 0; x2 < history.getWidth(); ++x2) {
                    if (!history.isFull(x2)) {
                        DbThink dbThink = new DbThink(history, x2, hashTable, new DbConnection(db), dbThinkLimits,
                                reserve);
                        threads[x * history.getWidth() + x2] = dbThink;
                        dbThink.start();
                    }
                }
                history.remove();
            }
        }

        boolean stop = false;
        while (!stop) {
            stop = true;
            long written = 0;
            long collisions = 0;
            long hits = 0;
            long nohits = 0;
            long tested = 0;
            for (int x = 0; x < history.getWidth() * history.getWidth(); ++x) {
                if (threads[x] != null) {
                    written += threads[x].getWritten();
                    collisions += threads[x].getCollisions();
                    hits += threads[x].getHits();
                    nohits += threads[x].getNohits();
                    tested += threads[x].getTested();
                    if (threads[x].isAlive()) {
                        stop = false;
                    }
                }
            }
            System.out.println(
                    "Tested: " + (tested - otested)
                            + " Written: " + (written - owritten) + " Collisions: " + (collisions - ocollisions)
                            + " Hits: " + (nohits - onohits) * 100 / ((hits - ohits) + (nohits - onohits))
                            + "% Cache: " + hashTable.filled() + "% Sleeping: " + reserve.getSleeping() + " "
                            + reserve.toString());
            owritten = written;
            ocollisions = collisions;
            ohits = hits;
            onohits = nohits;
            otested = tested;
            Thread.sleep(1000 * 60);
        }

        owritten = 0;
        ocollisions = 0;
        for (int x = 0; x < history.getWidth(); ++x) {
            if (!history.isFull(x)) {
                DbThink dbThink = new DbThink(history, x, hashTable, new DbConnection(db), dbThinkLimits, reserve);
                threads[x] = dbThink;
                dbThink.start();
            }
        }

        stop = false;
        while (!stop) {
            stop = true;
            long written = 0;
            long collisions = 0;
            for (int x = 0; x < history.getWidth(); ++x) {
                if (threads[x] != null) {
                    written += threads[x].getWritten();
                    collisions += threads[x].getCollisions();
                    if (threads[x].isAlive()) {
                        stop = false;
                    }
                }
            }
            System.out.println("Written: " + (written - owritten) + " Collisions: " + (collisions - ocollisions)
                    + " Cache: " + hashTable.filled() + "%");

            Runtime rt = Runtime.getRuntime();

            long total_mem = rt.totalMemory();
            long free_mem = rt.freeMemory();
            long used_mem = total_mem - free_mem;
            System.out.println("T: " + total_mem + " F: " + free_mem + " U: " + used_mem);

            owritten = written;
            ocollisions = collisions;
            Thread.sleep(1000 * 60);
        }
    }
}
