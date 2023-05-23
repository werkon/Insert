
import java.math.BigInteger;
import java.util.Scanner;

import de.esnecca.multi.Game;
import de.esnecca.multi.History;
import de.esnecca.multi.SimpleThink;

public class Simple {
    public static void main(String[] args) throws Exception {

        History history = new History(4, 4, 2, 4);



        Scanner keyboard = new Scanner(System.in);

        while (true) {
            history.print();
            BigInteger bi = history.getBigInteger();
            Game game = new Game(history);
            System.out.println(bi.toString(10));
            game.fromBigInteger(bi);
            game.print();

            SimpleThink simpleThink = new SimpleThink(history);

            if( !history.isFull(0)){
                System.out.println( "1:" + simpleThink.think(0) );
            }
            if( !history.isFull(1)){
                System.out.println( "2:" + simpleThink.think(1) );
            }
            if( !history.isFull(2)){
                System.out.println( "3:" + simpleThink.think(2) );
            }
            if( !history.isFull(3)){
                System.out.println( "4:" + simpleThink.think(3) );
            }
            // if( !history.isFull(4)){
            //     //System.out.println( "5:" + simpleThink.think(4) );
            // }

            String input = keyboard.nextLine();

            if ("q".equals(input)) {
                break;
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
