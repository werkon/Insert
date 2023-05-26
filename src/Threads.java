import java.util.Scanner;

import de.esnecca.multi.History;
import de.esnecca.multi.HashThink;

public class Threads {
    
    public static void main(String[] args) throws Exception {

        History history = new History(5, 4, 2, 4);
        Scanner keyboard = new Scanner(System.in);

        while (true) {
            history.print();

            Thread[] threads = new Thread[history.getWidth()];
            for( int x = 0; x < history.getWidth(); ++x){
                if(!history.isFull(x)){
                    HashThink simpleThink = new HashThink(history, x);
                    Thread thread = new Thread(simpleThink);
                    threads[x] = thread;
                    thread.start();
                }
            }

            boolean stop = false;
            while( !stop ){
                stop = true;
                for( int x = 0; x < history.getWidth(); ++x){
                    if( threads[x] != null && threads[x].isAlive() ){
                        stop = false;
                    }
                }
                Thread.sleep(1000);
//                System.out.println("sksks");
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
            if (x >= 0 && x < history.getWidth() && !history.isFull(x) ) {
                if (history.test(x)) {
                    System.out.println("*********************");
                }
                history.insert(x);
            }

        }
        keyboard.close();
    }   
}
