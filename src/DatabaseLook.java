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

        while (true) {
            history.print();
            System.out.print("   ");
            for (int x = 0; x < history.getWidth(); ++x) {
                System.out.print(" |");
            }
            System.out.println("");
            System.out.print("   ");
            for (int x = 0; x < history.getWidth(); ++x) {
                if (history.isFull(x)) {
                    System.out.print("   x ");
                }else{
                    if( history.test(x)){
                        System.out.format("%3d ", 1);
                    }else{
                        history.insert(x);
                        DbEntry dbEntry = dbConnection.getDbEntry(history.getSmallestBigInteger(), gameid);
                        if( dbEntry == null ){
                            System.out.print("    ");
                        }else{
                            System.out.format("%3d ", dbEntry.getResult());
                        }
                        history.remove();
                    }
                }
            }
            System.out.println("");


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
        dbConnection.close();
        keyboard.close();
    }
}
