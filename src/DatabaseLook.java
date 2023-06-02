import java.util.Scanner;

import de.esnecca.multi.HashThink2;
import de.esnecca.multi.History;
import de.esnecca.multi.db.Db;
import de.esnecca.multi.db.DbConnection;
import de.esnecca.multi.db.DbEntry;
import de.esnecca.multi.hash.HashTable;

public class DatabaseLook {

    public static void main(String[] args) throws Exception {

        History history = new History(7, 6, 2, 4);
        int dbLimit = 20;
        Scanner keyboard = new Scanner(System.in);
        Db db = new Db("insert", "insert", "jdbc:postgresql://localhost:5432/insert");
        DbConnection dbConnection = new DbConnection(db);
        HashTable hashTable = new HashTable(1000*1000);
        int gameid = dbConnection.getGameId(history);
        if (gameid <= 0) {
            dbConnection.createGame(history);
        }
        gameid = dbConnection.getGameId(history);
        Integer[] results = new Integer[history.getWidth()];

        while (true) {
            for (int x = 0; x < history.getWidth(); ++x) {
                Integer result = null;
                if (!history.isFull(x)) {
                    if (history.test(x)) {
                        result = Integer.valueOf(1);
                    } else {
                        history.insert(x);

                        for(int i = 0; i < history.getWidth(); ++i ){
                            if(!history.isFull(i)){
                                if(history.test(i)){
                                    result = Integer.valueOf(-2);
                                    break;
                                }
                            }
                        }
                        if(result == null){
                            DbEntry dbEntry = dbConnection.getDbEntry(history.getSmallestBigInteger(), gameid);
                            if (dbEntry != null) {
                                result = Integer.valueOf(dbEntry.getResult());
                            }
                        }
                        history.remove();
                    }
                }
                results[x] = result;
            }

            if(history.getInserted() >= dbLimit ){
                HashThink2[] threads = new HashThink2[history.getWidth()];
                for (int x = 0; x < history.getWidth(); ++x) {
                    if( results[x] == null ){
                        if (!history.isFull(x)) {
                            HashThink2 hashThink2 = new HashThink2(history, x, hashTable, history.getSize() - 10);
                            threads[x] = hashThink2;
                            hashThink2.start();
                        }
                    }
                }
                boolean stop = false;
                while(!stop){
                    System.out.println("Think...");
                    Thread.sleep(100);
                    stop = true;
                    for (int x = 0; x < history.getWidth(); ++x) {
                        if( threads[x] != null ){
                            if( threads[x].isAlive() ){
                                stop = false;
                            }
                        }
                    }
                }
                for (int x = 0; x < history.getWidth(); ++x) {
                    if( threads[x] != null ){
                        results[x] = threads[x].getResult();
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

            if ("t".equals(input)) {
                hashTable = new HashTable(1000*1000);
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
