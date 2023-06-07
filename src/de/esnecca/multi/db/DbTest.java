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
    private DbConnection dbConnection;

    @Before
    public void init() throws SQLException {
        db = new Db("insert", "insert", "jdbc:postgresql://localhost:5432/insert");
        dbConnection = new DbConnection(db);
    }

    @Test
    public void testGetGameId() throws SQLException {
        Game game = new Game(7, 6, 2, 4);
        assertEquals(1, dbConnection.getGameId(game));
        Game game2 = new Game(1, 1, 1, 1);
        dbConnection.deleteGame(game2);
        assertEquals(0, dbConnection.getGameId(game2));

    }

    @Test
    public void testCreateGame() throws SQLException {
        Game game = new Game(1, 1, 1, 2);
        assertTrue(dbConnection.deleteGame(game));
        assertTrue(dbConnection.createGame(game));
        assertTrue(dbConnection.getGameId(game) > 0);
        assertTrue(dbConnection.deleteGame(game));
    }

    @Test
    public void testDeleteGame() throws SQLException {
        Game game = new Game(1, 1, 1, 3);
        assertTrue(dbConnection.deleteGame(game));
        assertTrue(dbConnection.createGame(game));
        assertTrue(dbConnection.getGameId(game) > 0);
        assertTrue(dbConnection.deleteGame(game));
        assertFalse(dbConnection.getGameId(game) > 0);
    }

    @Test
    public void testCreateGetDeleteEntry() throws SQLException {

        Game game = new Game(1, 1, 1, 4);
        BigInteger bi = BigInteger.valueOf(558899);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);
        bi = bi.multiply(bi);

        int gameid = dbConnection.getGameId(game);
        if (gameid > 0) {
            assertTrue(dbConnection.deleteEntry(bi, gameid));
        }
        assertTrue(dbConnection.deleteGame(game));
        assertTrue(dbConnection.deleteGame(game));
        assertTrue(dbConnection.deleteGame(game));
        assertTrue(dbConnection.deleteGame(game));
        assertTrue(dbConnection.createGame(game));

        gameid = dbConnection.getGameId(game);

        DbEntry dbEntry = new DbEntry(bi, gameid, 1, 2);

        assertTrue(dbConnection.createEntry(dbEntry));
        assertFalse(dbConnection.createEntry(dbEntry));
        assertFalse(dbConnection.createEntry(dbEntry));

        DbEntry dbEntry2 = dbConnection.getDbEntry(bi, gameid);
        assertEquals(dbEntry, dbEntry2);

        assertTrue(dbConnection.deleteEntry(bi, gameid));
        assertNull(dbConnection.getDbEntry(bi, gameid));
        assertTrue(dbConnection.deleteEntry(bi, gameid));

        assertTrue(dbConnection.deleteGame(game));
    }

    @Test
    public void testReservation() throws SQLException {
        Game game = new Game(1, 1, 1, 1);
        dbConnection.createGame(game);
        int gameId = dbConnection.getGameId(game);
        assertTrue(gameId > 0);

        dbConnection.deleteAllReservations(gameId);

        BigInteger bi = BigInteger.valueOf(123);
        assertTrue(dbConnection.createReservation(bi, gameId,1));
        assertFalse(dbConnection.createReservation(bi, gameId,1));
        assertFalse(dbConnection.createReservation(bi, gameId,1));

        BigInteger bi2 = BigInteger.valueOf(321);
        assertTrue(dbConnection.createReservation(bi2, gameId,1));
        assertFalse(dbConnection.createReservation(bi2, gameId,2));
        assertFalse(dbConnection.createReservation(bi2, gameId,1));

        dbConnection.deleteReservation(bi, gameId);

        assertTrue(dbConnection.createReservation(bi, gameId,1));
        assertFalse(dbConnection.createReservation(bi, gameId,1));
        assertFalse(dbConnection.createReservation(bi, gameId,1));

        assertFalse(dbConnection.createReservation(bi2, gameId,1));
        assertFalse(dbConnection.createReservation(bi2, gameId,1));

        dbConnection.deleteAllReservations(gameId);

        assertTrue(dbConnection.createReservation(bi, gameId,1));
        assertFalse(dbConnection.createReservation(bi, gameId,1));
        assertFalse(dbConnection.createReservation(bi, gameId,1));

        assertTrue(dbConnection.createReservation(bi2, gameId,1));
        assertFalse(dbConnection.createReservation(bi2, gameId,1));
        assertFalse(dbConnection.createReservation(bi2, gameId,1));

        dbConnection.deleteAllReservations(gameId);

        assertTrue(dbConnection.deleteGame(game));

    }

}
