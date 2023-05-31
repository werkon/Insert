
import java.util.Scanner;

import de.esnecca.multi.History;
import de.esnecca.multi.hash.HashEntry;
import de.esnecca.multi.HashThink;

public class Simple {
    public static void main(String[] args) throws Exception {

        History history = new History(5, 4, 2, 4);

        Scanner keyboard = new Scanner(System.in);

        Integer[] result = new Integer[history.getWidth()];
        history.print(result);

        while (true) {

            HashThink simpleThink = new HashThink(history,0);

            for(int x = 0; x < history.getWidth(); ++x ){
                if( history.isFull(x)){
                    result[x] = null;
                }else{
                    result[x] = Integer.valueOf( simpleThink.think(x) );
                }
            }

            history.print(result);


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
