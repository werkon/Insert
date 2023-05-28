package de.esnecca.multi.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
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
    public void testGetGameId() throws SQLException {
        Game game = new Game(7, 6, 2, 4);
        assertEquals(1, db.getGameId(game));
        Game game2 = new Game(1, 1, 1, 1);
        assertEquals(0, db.getGameId(game2));

    }

    @Test
    public void testCreateGame() throws SQLException {
        Game game = new Game(1, 1, 1, 1);
        assertTrue(db.deleteGame(game));
        assertTrue(db.createGame(game));
        assertTrue(db.getGameId(game) > 0);
        assertTrue(db.deleteGame(game));
    }

    @Test
    public void testDeleteGame() throws SQLException {
        Game game = new Game(1, 1, 1, 1);
        assertTrue(db.deleteGame(game));
        assertTrue(db.createGame(game));
        assertTrue(db.getGameId(game) > 0);
        assertTrue(db.deleteGame(game));
        assertFalse(db.getGameId(game) > 0);
    }

    @Test
    public void testCreateEntry() throws SQLException {

        Game game = new Game(1, 1, 2, 1);
        BigInteger bi = BigInteger.valueOf(558899);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);

        int gameid = db.getGameId(game);
        if (gameid > 0) {
            assertTrue(db.deleteEntry(bi, gameid));
        }
        assertTrue(db.deleteGame(game));
        assertTrue(db.createGame(game));

        gameid = db.getGameId(game);

        DbEntry dbEntry = new DbEntry(bi, gameid, 1, 2);

        assertTrue(db.createEntry(dbEntry));
        assertFalse(db.createEntry(dbEntry));
        assertFalse(db.createEntry(dbEntry));

        DbEntry dbEntry2 = db.getDbEntry(bi, gameid);
        assertEquals(dbEntry, dbEntry2);

        assertTrue(db.deleteEntry(bi, gameid));
        assertNull(db.getDbEntry(bi, gameid));
        assertTrue(db.deleteEntry(bi, gameid));

        assertTrue(db.deleteGame(game));
    }

    @Test
    public void testDeleteEntry() throws SQLException {

        Game game = new Game(1, 1, 2, 1);
        BigInteger bi = BigInteger.valueOf(558899);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);

        int gameid = db.getGameId(game);
        if (gameid > 0) {
            assertTrue(db.deleteEntry(bi, gameid));
        }
        assertTrue(db.deleteGame(game));
        assertTrue(db.createGame(game));

        gameid = db.getGameId(game);

        DbEntry dbEntry = new DbEntry(bi, gameid, 1, 2);

        assertTrue(db.createEntry(dbEntry));
        assertFalse(db.createEntry(dbEntry));
        assertFalse(db.createEntry(dbEntry));

        DbEntry dbEntry2 = db.getDbEntry(bi, gameid);
        assertEquals(dbEntry, dbEntry2);

        assertTrue(db.deleteEntry(bi, gameid));
        assertNull(db.getDbEntry(bi, gameid));
        assertTrue(db.deleteEntry(bi, gameid));

        assertTrue(db.deleteGame(game));
    }

    @Test
    public void testGetEntry() throws SQLException {

        Game game = new Game(1, 1, 2, 1);
        BigInteger bi = BigInteger.valueOf(558899);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);

        int gameid = db.getGameId(game);
        if (gameid > 0) {
            assertTrue(db.deleteEntry(bi, gameid));
        }
        assertTrue(db.deleteGame(game));
        assertTrue(db.createGame(game));

        gameid = db.getGameId(game);

        DbEntry dbEntry = new DbEntry(bi, gameid, 1, 2);

        assertTrue(db.createEntry(dbEntry));
        assertFalse(db.createEntry(dbEntry));
        assertFalse(db.createEntry(dbEntry));

        DbEntry dbEntry2 = db.getDbEntry(bi, gameid);
        assertEquals(dbEntry, dbEntry2);

        assertTrue(db.deleteEntry(bi, gameid));
        assertNull(db.getDbEntry(bi, gameid));
        assertTrue(db.deleteEntry(bi, gameid));

        assertTrue(db.deleteGame(game));
    }

}
