package de.esnecca.multi;

import java.math.BigInteger;

import de.esnecca.multi.tools.Prime;

public class HashThink implements Runnable{
    
    private Game game;
    private int x;
    private HashEntry[] hashEntries;
    private Prime prime;

    public HashThink( Game game, int x ){
        this.game = new Game(game);
        this.x = x;

        prime = new Prime(1000*1000);
        hashEntries = new HashEntry[prime.getPrime()];
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

        BigInteger bi = null;
        int index = 0;
        if( game.getInserted() < game.getSize() - 4  ){
            bi = game.getSmallestBigInteger();
            index = bi.mod(prime.getBigPrime()).intValue();
            HashEntry hashEntry = hashEntries[index];
            if( hashEntry != null && hashEntry.getValue().equals(bi) ){
                game.remove(x);
                return hashEntry.getResult();
            }
        }


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

            int ret = ( kleinstesPositivesR + 1 ) * (-1);
            if( bi != null){
                HashEntry hashEntry = new HashEntry(bi, ret);
                hashEntries[index] = hashEntry;
            }
            return ret;
        }
        if(unentschieden){
            // System.out.println("x: " + x + " c: " + game.getCurrentColor());
            // game.print();
            // System.out.println("e: " + 0);
            // System.out.println("********************************************");
    
            int ret = 0;
            if( bi != null){
                HashEntry hashEntry = new HashEntry(bi, ret);
                hashEntries[index] = hashEntry;
            }
            return ret;
        }

        // System.out.println("x: " + x + " c: " + game.getCurrentColor());
        // game.print();
        // System.out.println("e: " + ((groesstesNegativesR * (-1)) + 1));
        // System.out.println("********************************************");

        int ret = (groesstesNegativesR * (-1)) + 1;
        if( bi != null){
            HashEntry hashEntry = new HashEntry(bi, ret);
            hashEntries[index] = hashEntry;
        }
        return ret;
    }

    @Override
    public void run() {
        int ret = think(x);
        System.out.println("x: " + (x + 1) + " r: " +ret);
    }



}
