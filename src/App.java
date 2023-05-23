import java.util.Random;

import de.esnecca.multi.Game;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Game game = new Game(7,6,2,4);

        boolean complete = false;
        for(int i = 0; !complete; ++i){
            Random rn = new Random();
            System.out.println(i);
            game.reset();
            boolean stop = false;
            while(!stop){
                if( game.isFull() ){
                    stop = true;
                    complete = true;
                    game.print();
                }
                int x = rn.nextInt(game.getWidth());
                if( !game.isFull(x)){
                    if( game.test(x) ){
                        game.insert(x);
                        //game.print();
                        stop = true;
                    }else{
                        game.insert(x);
                    }
                }
            }

        }

    }
}
