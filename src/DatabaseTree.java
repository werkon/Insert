import de.esnecca.multi.History;
import de.esnecca.multi.db.Db;
import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.db.DbEntry;

public class DatabaseTree {

    public static void main(String[] args) throws Exception {

        History history = new History(7, 6, 2, 4);
        Db db = new Db("insert", "insert", "jdbc:postgresql://localhost:5432/insert");
        DbConnection dbConnection = new DbConnection(db);
        int gameid = dbConnection.getGameId(history);
        if (gameid <= 0) {
            dbConnection.createGame(history);
        }
        gameid = dbConnection.getGameId(history);

        System.out.print("\033[H\033[2J");

        // history.insert(6);
        // history.insert(6);

        while (true) {

            Integer[] results = new Integer[history.getWidth()];
            boolean lag = false;
            for (int x = 0; x < history.getWidth(); ++x) {
                Integer result = null;
                if (!history.isFull(x)) {
                    if (history.test(x)) {
                        result = Integer.valueOf(1);
                    } else {
                        history.insert(x);
                        for (int i = 0; i < history.getWidth(); ++i) {
                            if (!history.isFull(i)) {
                                if (history.test(i)) {
                                    result = Integer.valueOf(-2);
                                    break;
                                }
                            }
                        }
                        if (result == null) {
                            DbEntry dbEntry = dbConnection.getDbEntry(history.getSmallestBigInteger(), gameid);
                            if (dbEntry == null) {
                                lag = true;
                            } else {
                                result = Integer.valueOf(dbEntry.getResult());
                            }
                        }
                        history.remove();
                    }
                }
                results[x] = result;
            }
            if ( true || history.getInserted() == 19) {
                System.out.print("\033[H");
                history.print(results);
                System.out.flush();
            }

            if (lag) {
                if (history.getInserted() < 19) {
                    for (int x = 0; x < history.getWidth(); ++x) {
                        if (results[x] == null && !history.isFull(x)) {
                            history.insert(x);
                            break;
                        }
                    }
                }
            } else {
                if (history.getInserted() > 0) {
                    history.remove();
                } else {
                    break;
                }
            }
            Thread.sleep(100);
        }

        dbConnection.close();
    }
}
