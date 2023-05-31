import java.util.Scanner;

import de.esnecca.multi.History;
import de.esnecca.multi.db.Db;
import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.db.DbEntry;

public class DatabaseLook {

    public static void main(String[] args) throws Exception {

        History history = new History(7, 6, 2, 4);
        Scanner keyboard = new Scanner(System.in);
        Db db = new Db("insert", "insert", "jdbc:postgresql://localhost:5432/insert");
        DbConnection dbConnection = new DbConnection(db);
        int gameid = dbConnection.getGameId(history);
        if (gameid <= 0) {
            dbConnection.createGame(history);
        }
        gameid = dbConnection.getGameId(history);
        Integer[] results = new Integer[history.getWidth()];

        while (true) {
            for (int x = 0; x < history.getWidth(); ++x) {
                if (history.isFull(x)) {
                    results[x] = null;
                } else {
                    if (history.test(x)) {
                        results[x] = Integer.valueOf(1);
                    } else {
                        history.insert(x);
                        DbEntry dbEntry = dbConnection.getDbEntry(history.getSmallestBigInteger(), gameid);
                        if (dbEntry == null) {
                            results[x] = null;
                        } else {
                            results[x] = Integer.valueOf(dbEntry.getResult());
                        }
                        history.remove();
                    }
                }
            }

            history.print(results);
            System.out.println("");

            String input = keyboard.nextLine();

            if ("q".equals(input)) {
                break;
            }

            if ("c".equals(input)) {
                history.reset();
                continue;
            }

            if ("r".equals(input)) {
                if (history.getInserted() > 0) {
                    history.remove();
                }
                continue;
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
        dbConnection.close();
        keyboard.close();
    }
}
