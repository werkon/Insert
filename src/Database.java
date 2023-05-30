import java.util.Scanner;

import de.esnecca.multi.History;
import de.esnecca.multi.db.Db;
import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.hash.HashTable;
import de.esnecca.multi.DbThink;

public class Database {

    public static void main(String[] args) throws Exception {

        History history = new History(7, 6, 2, 4);
        DbThink.DbThinkLimits dbThinkLimits = new DbThink.DbThinkLimits(history.getSize() - 9, 24, 16);
        Scanner keyboard = new Scanner(System.in);
        HashTable hashTable = new HashTable(1000 * 1000 * 50);
        Db db = new Db("insert", "insert", "jdbc:postgresql://localhost:5432/insert");
        DbConnection dbConnection = new DbConnection(db);
        if (dbConnection.getGameId(history) <= 0) {
            dbConnection.createGame(history);
        }
        dbConnection.close();

        long owritten = 0;
        while (true) {
            history.print();

            DbThink[] threads = new DbThink[history.getWidth() * history.getWidth()];
            for (int x = 0; x < history.getWidth(); ++x) {
                if (!history.isFull(x)) {
                    history.insert(x);
                    for (int x2 = 0; x2 < history.getWidth(); ++x2) {
                        if (!history.isFull(x2)) {
                            DbThink dbThink = new DbThink(history, x2, hashTable, new DbConnection(db), dbThinkLimits, false);
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
                for (int x = 0; x < history.getWidth() * history.getWidth(); ++x) {
                    if (threads[x] != null) {
                        written += threads[x].getWritten();
                        if (threads[x].isAlive()) {
                            stop = false;
                        }
                    }
                }
                System.out.println("Written: " + (written - owritten) + " Cache: " + hashTable.filled() + "%");
                owritten = written;
                Thread.sleep(1000 * 60);
            }

            for (int x = 0; x < history.getWidth(); ++x) {
                if (!history.isFull(x)) {
                    DbThink dbThink = new DbThink(history, x, hashTable, new DbConnection(db), dbThinkLimits, true);
                    threads[x * history.getWidth() + x] = dbThink;
                    dbThink.start();
                }
            }

            stop = false;
            while (!stop) {
                stop = true;
                for (int x = 0; x < history.getWidth(); ++x) {
                    if (threads[x] != null && threads[x].isAlive()) {
                        stop = false;
                    }
                }
                Thread.sleep(1000);
            }

            String input = keyboard.nextLine();

            if ("q".equals(input)) {
                break;
            }

            if ("c".equals(input)) {
                history.reset();
            }

            if ("r".equals(input)) {
                if (history.getInserted() > 0) {
                    history.remove();
                }
            }

            int x = 0;
            try {
                x = Integer.parseInt(input);
            } catch (Exception ex) {
                continue;
            }
            --x;
            if (x >= 0 && x < history.getWidth() && !history.isFull(x)) {
                if (history.test(x)) {
                    System.out.println("*********************");
                }
                history.insert(x);
            }

        }
        keyboard.close();
    }
}
