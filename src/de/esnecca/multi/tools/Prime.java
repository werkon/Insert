package de.esnecca.multi.tools;

import java.math.BigInteger;

public class Prime {

    int prime;
    BigInteger bigPrime;

    public Prime(int p){
        prime = prime(p);
        bigPrime = BigInteger.valueOf(prime);
    }

    private int prime(int i){
        for( ; i > 0; --i ){
            int j;
            for( j = 2; j < i; ++j){
                if( i % j == 0 ){
                    break;
                }
            }
            if( j >= i ){
                return i;
            }
        }
        return 0;
    }

    public int getPrime() {
        return prime;
    }

    public BigInteger getBigPrime() {
        return bigPrime;
    }

}
