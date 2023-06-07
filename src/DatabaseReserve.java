import de.esnecca.multi.History;
import de.esnecca.multi.db.Db;
import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.hash.HashTable;
import de.esnecca.multi.DbReserveThink;

public class DatabaseReserve {

    public static void main(String[] args) throws Exception {

        History history = new History(7, 6, 2, 4);
        DbReserveThink.DbThinkLimits dbThinkLimits = new DbReserveThink.DbThinkLimits(history.getSize() - 11, 20, 12);
        HashTable hashTable = new HashTable(1000 * 1000 * 100);
        Db db = new Db("insert", "insert", "jdbc:postgresql://localhost:5432/insert");
        DbConnection dbConnection = new DbConnection(db);
        if (dbConnection.getGameId(history) <= 0) {
            dbConnection.createGame(history);
        }
        dbConnection.deleteAllReservations(dbConnection.getGameId(history));
        dbConnection.close();

        long owritten = 0;
        long ocollisions = 0;

        DbReserveThink[] threads = new DbReserveThink[history.getWidth() * history.getWidth()];
        for (int x = 0; x < history.getWidth(); ++x) {
            if (!history.isFull(x)) {
                history.insert(x);
                for (int x2 = 0; x2 < history.getWidth(); ++x2) {
                    if (!history.isFull(x2)) {
                        DbReserveThink dbThink = new DbReserveThink(history, x2, hashTable, new DbConnection(db),
                                dbThinkLimits);
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
            for (int x = 0; x < history.getWidth() * history.getWidth(); ++x) {
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
            owritten = written;
            ocollisions = collisions;
            Thread.sleep(1000 * 60);
        }

        owritten = 0;
        ocollisions = 0;
        for (int x = 0; x < history.getWidth(); ++x) {
            if (!history.isFull(x)) {
                DbReserveThink dbThink = new DbReserveThink(history, x, hashTable, new DbConnection(db), dbThinkLimits);
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

            owritten = written;
            ocollisions = collisions;
            Thread.sleep(1000 * 60);
        }
    }
}