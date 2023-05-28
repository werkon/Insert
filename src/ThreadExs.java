import java.util.Scanner;

import de.esnecca.multi.History;
import de.esnecca.multi.hash.HashTable;
import de.esnecca.multi.HashThinkEx;

public class ThreadExs {

    public static void main(String[] args) throws Exception {

        History history = new History(5, 5, 2, 4);
        Scanner keyboard = new Scanner(System.in);
        HashTable hashTable = new HashTable(1000 * 1000 * 50);

        while (true) {
            history.print();

            Thread[] threads = new Thread[history.getWidth() * history.getWidth()];
            for (int x = 0; x < history.getWidth(); ++x) {
                if (!history.isFull(x)) {
                    history.insert(x);
                    for (int x2 = 0; x2 < history.getWidth(); ++x2) {
                        if (!history.isFull(x2)) {
                            HashThinkEx simpleThink = new HashThinkEx(history, x2, hashTable, false);
                            Thread thread = new Thread(simpleThink);
                            threads[x * history.getWidth() + x2] = thread;
                            thread.start();
                        }
                    }
                    history.remove();
                }
            }

            boolean stop = false;
            while (!stop) {
                stop = true;
                for (int x = 0; x < history.getWidth() * history.getWidth(); ++x) {
                    if (threads[x] != null && threads[x].isAlive()) {
                        stop = false;
                    }
                }
                Thread.sleep(1000);
                System.out.println(hashTable.filled());
                Runtime gfg = Runtime.getRuntime();
                System.out.println("TM: " + gfg.totalMemory());
                System.out.println("FM: " + gfg.freeMemory());
            }

            for (int x = 0; x < history.getWidth(); ++x) {
                if (!history.isFull(x)) {
                    HashThinkEx simpleThink = new HashThinkEx(history, x, hashTable, true);
                    Thread thread = new Thread(simpleThink);
                    threads[x * history.getWidth() + x] = thread;
                    thread.start();
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
