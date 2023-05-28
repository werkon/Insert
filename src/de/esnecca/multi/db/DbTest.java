package de.esnecca.multi.db;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import de.esnecca.multi.Game;

public class DbTest {
    
    private Db db;

    @Before
    public void init() {
        db = new Db("insert", "insert", "jdbc:postgresql://localhost:5432/insert");
    }

    @Test
    public void testGetGameId() throws SQLException  {
        Game game = new Game(7,6,2,4);
        assertEquals(1, db.getGameId(game));
        Game game2 = new Game(6,6,2,4);
        assertEquals(0, db.getGameId(game2));
        
    }




}
