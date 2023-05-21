package de.esnecca.multi;

public class Game extends Field {

    private int colors;
    private int wins;
    private int inserted;
    private int[] insertedRow;
    private int currentColor;

    public Game(int width, int height, int colors, int wins) {
        super(width, height);
        this.colors = colors;
        this.wins = wins;
        insertedRow = new int[width];
        reset();
    }

    public void reset(){
        super.reset();
        for( int x = 0; x < getWidth(); ++x ){
            insertedRow[x] = 0;
        }
        inserted = 0;
        currentColor = 1;
    }
    
    public void insert(int x){
        set( x, insertedRow[x]++, currentColor );
        ++currentColor;
        if( currentColor > colors){
            currentColor = 1;
        }
    }

    public void remove(int x){
        set( x, --insertedRow[x], 0 );
        --currentColor;
        if( currentColor <= 0 ){
            currentColor = colors;
        }
    }

    public int getCurrentColor(int x){
        return currentColor;
    }

    public int getInserted(int x){
        return insertedRow[x];
    }

    public int getInserted(){
        return inserted;
    }

    public boolean isFull(){
        return getInserted() >= getSize();
    }

    public boolean isFull(int x){
        return getInserted( x ) < getHeight();
    }

    public boolean test(int x){

        int i;
        for( i = 1; i < wins; ++i){
            
        }
        return false;
    }

}
