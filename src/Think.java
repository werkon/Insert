import de.esnecca.multi.Game;
import de.esnecca.multi.SimpleThink;

public class Think {
    public static void main(String[] args) throws Exception {

        Game game = new Game(4, 4, 2, 3);
        SimpleThink simpleThink = new SimpleThink(game);

        System.out.println( simpleThink.think(0) );
        System.out.println( simpleThink.think(1) );
        System.out.println( simpleThink.think(2) );
        System.out.println( simpleThink.think(3) );



    }
}
