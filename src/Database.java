import de.esnecca.multi.History;
import de.esnecca.multi.db.Db;
import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.hash.HashTable;
import de.esnecca.multi.reserve.Reserve;
import de.esnecca.multi.DbThink;

public class Database {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting DB...");

        History history = new History(7, 6, 2, 4);
        DbThink.DbThinkLimits dbThinkLimits = new DbThink.DbThinkLimits(history.getSize() - 10, 20, 10);
        HashTable hashTable = new HashTable(1000 * 1000 * 100);
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

        System.out.println("Starting threads...");

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

        System.out.println("Started " + threads.length + " threads..." );

        long rounds = 1;
        boolean stop = false;
        while (!stop) {
            stop = true;

            Thread.sleep(1000 * 60);

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
                    "Tested: " + ((tested - otested) / 1000000000)
                            + " Written: " + (written - owritten) + "/" + (written / rounds) + " Collisions: "
                            + (collisions - ocollisions)
                            + " Hits: " + (nohits - onohits) * 100 / ((hits - ohits) + (nohits - onohits))
                            + "% Cache: " + hashTable.filled() + "% Sleeping: " + reserve.getSleeping() + " "
                            + reserve.toString());
            owritten = written;
            ocollisions = collisions;
            ohits = hits;
            onohits = nohits;
            otested = tested;
            ++rounds;
        }

        for (int x = 0; x < history.getWidth(); ++x) {
            if (!history.isFull(x)) {
                DbThink dbThink = new DbThink(history, x, hashTable, new DbConnection(db), dbThinkLimits, reserve);
                threads[x] = dbThink;
                dbThink.start();
            }
        }

        System.out.println("Started (2)...");

        stop = false;
        while (!stop) {
            stop = true;

            Thread.sleep(1000 * 60);

            for (int x = 0; x < history.getWidth(); ++x) {
                if (threads[x] != null) {
                    if (threads[x].isAlive()) {
                        stop = false;
                    }
                }
            }
        }

        System.out.println("End...");

    }
}
