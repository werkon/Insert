package de.esnecca.multi;

public class SimpleThink {
    
    private Game game;

    public SimpleThink( Game game ){
        this.game = new Game(game);
    }

    // Positiver Wert:
    // Ich gewinne in spätestens x Zügen

    // Negativer Wert:
    // Ich verliere in spätestens x Zügen

    // 0:
    // Unentscheiden.

    public int think(int x){
        if( game.test(x)){
            // System.out.println("x: " + x + " c: " + game.getCurrentColor());
            // game.print();
            // System.out.println("e: " + 1);
            // System.out.println("********************************************");
            return 1;
        }
        if( game.getInserted() >= game.getSize() - 1 ){
            // System.out.println("x: " + x + " c: " + game.getCurrentColor());
            // game.print();
            // System.out.println("e: " + 0);
            // System.out.println("********************************************");
            return 0;
        }
        game.insert(x);
        
        int kleinstesPositivesR = 0;
        int groesstesNegativesR = 0;
        boolean unentschieden = false;
        for(int i = 0; i < game.getWidth(); ++i ){
            if(!game.isFull(i)){
                int r = think(i);
                if( r > 0 ){
                    if( kleinstesPositivesR == 0 || r < kleinstesPositivesR ){
                        kleinstesPositivesR = r;
                    }
                }else{
                    if( r < 0 ){
                        if( r < groesstesNegativesR ){
                            groesstesNegativesR = r;
                        }
                    }else{
                        unentschieden = true;
                    }
                }
            }
        }

        game.remove(x);

        if(kleinstesPositivesR > 0){

            // System.out.println("x: " + x + " c: " + game.getCurrentColor());
            // game.print();
            // System.out.println("e: " + ( ( kleinstesPositivesR + 1 ) * (-1)  ));
            // System.out.println("********************************************");


            return ( kleinstesPositivesR + 1 ) * (-1);
        }
        if(unentschieden){
            // System.out.println("x: " + x + " c: " + game.getCurrentColor());
            // game.print();
            // System.out.println("e: " + 0);
            // System.out.println("********************************************");
    

            return 0;
        }

        // System.out.println("x: " + x + " c: " + game.getCurrentColor());
        // game.print();
        // System.out.println("e: " + ((groesstesNegativesR * (-1)) + 1));
        // System.out.println("********************************************");


        return (groesstesNegativesR * (-1)) + 1;
    }



}
