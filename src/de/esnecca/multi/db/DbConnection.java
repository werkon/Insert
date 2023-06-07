package de.esnecca.multi.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.esnecca.multi.Game;

public class DbConnection {
    private Connection conn;
    private PreparedStatement preparedStatementGetGameId;
    private PreparedStatement preparedStatementCreateGame;
    private PreparedStatement preparedStatementDeleteGame;
    private PreparedStatement preparedStatementGetEntry;
    private PreparedStatement preparedStatementCreateEntry;
    private PreparedStatement preparedStatementDeleteEntry;
    private PreparedStatement preparedStatementDeleteAllReservations;
    private PreparedStatement preparedStatementDeleteReservation;
    private PreparedStatement preparedStatementCreateReservation;

    public DbConnection(Db db) throws SQLException {
        conn = db.getConnection();
        String sql = "select id from games where width = ? and height = ? and colors = ? and wins = ?;";
        preparedStatementGetGameId = conn.prepareStatement(sql);
        sql = "insert into games (width, height, colors, wins) values(?,?,?,?);";
        preparedStatementCreateGame = conn.prepareStatement(sql);
        sql = "delete from games where width = ? and height = ? and colors = ? and wins = ?;";
        preparedStatementDeleteGame = conn.prepareStatement(sql);
        sql = "select result, inserted from entries where bi = ? and gameid = ?;";
        preparedStatementGetEntry = conn.prepareStatement(sql);
        sql = "insert into entries (bi, gameid, result, inserted) values(?,?,?,?);";
        preparedStatementCreateEntry = conn.prepareStatement(sql);
        sql = "delete from entries where bi = ? and gameid = ?;";
        preparedStatementDeleteEntry = conn.prepareStatement(sql);
        sql = "delete from reservations where gameid = ?;";
        preparedStatementDeleteAllReservations = conn.prepareStatement(sql);
        sql = "delete from reservations where bi = ? and gameid = ?;";
        preparedStatementDeleteReservation = conn.prepareStatement(sql);
        sql = "insert into reservations (bi, gameid, inserted) values(?,?,?);";
        preparedStatementCreateGame = conn.prepareStatement(sql);
    }

    public void close() throws SQLException {
        preparedStatementGetGameId.close();
        preparedStatementCreateGame.close();
        preparedStatementDeleteGame.close();
        preparedStatementGetEntry.close();
        preparedStatementCreateEntry.close();
        preparedStatementDeleteEntry.close();
        conn.close();
    }

    public int getGameId(Game game) throws SQLException {

        preparedStatementGetGameId.setInt(1, game.getWidth());
        preparedStatementGetGameId.setInt(2, game.getHeight());
        preparedStatementGetGameId.setInt(3, game.getColors());
        preparedStatementGetGameId.setInt(4, game.getWins());

        ResultSet rs = preparedStatementGetGameId.executeQuery();

        int ret = 0;
        if (rs.next()) {
            ret = rs.getInt("id");
        }

        rs.close();
        return ret;
    }

    public boolean createGame(Game game) throws SQLException {

        preparedStatementCreateGame.setInt(1, game.getWidth());
        preparedStatementCreateGame.setInt(2, game.getHeight());
        preparedStatementCreateGame.setInt(3, game.getColors());
        preparedStatementCreateGame.setInt(4, game.getWins());
        try {
            preparedStatementCreateGame.execute();
        } catch (SQLException sex) {
            System.out.println("ex: " + game.getWins());
            String s = sex.getSQLState();
            if (s.equals("23505")) {
                return false;
            }
            throw sex;
        }
        return true;
    }

    public boolean deleteGame(Game game) throws SQLException {

        preparedStatementDeleteGame.setInt(1, game.getWidth());
        preparedStatementDeleteGame.setInt(2, game.getHeight());
        preparedStatementDeleteGame.setInt(3, game.getColors());
        preparedStatementDeleteGame.setInt(4, game.getWins());
        preparedStatementDeleteGame.execute();
        return true;
    }

    public boolean createEntry(DbEntry dbEntry) throws SQLException {

        preparedStatementCreateEntry.setBigDecimal(1, new BigDecimal(dbEntry.getBi()));
        preparedStatementCreateEntry.setInt(2, dbEntry.getGameid());
        preparedStatementCreateEntry.setInt(3, dbEntry.getResult());
        preparedStatementCreateEntry.setInt(4, dbEntry.getInserted());

        try {
            preparedStatementCreateEntry.execute();
        } catch (SQLException sex) {
            String s = sex.getSQLState();
            if (s.equals("23505")) {
                return false;
            }
            throw sex;
        }
        return true;
    }

    public boolean deleteEntry(BigInteger bi, int gameid) throws SQLException {

        preparedStatementDeleteEntry.setBigDecimal(1, new BigDecimal(bi));
        preparedStatementDeleteEntry.setInt(2, gameid);
        preparedStatementDeleteEntry.execute();
        return true;
    }

    public DbEntry getDbEntry(BigInteger bi, int gameid) throws SQLException {

        preparedStatementGetEntry.setBigDecimal(1, new BigDecimal(bi));
        preparedStatementGetEntry.setInt(2, gameid);

        ResultSet rs = preparedStatementGetEntry.executeQuery();

        if (rs.next()) {
            int result = rs.getInt("result");
            int inserted = rs.getInt("inserted");

            rs.close();
            return new DbEntry(bi, gameid, result, inserted);
        }

        rs.close();
        return null;
    }

    public boolean deleteAllReservations(int gameid) throws SQLException {

        preparedStatementDeleteAllReservations.setInt(1, gameid);
        preparedStatementDeleteAllReservations.execute();

        return true;
    }

    public boolean deleteReservation(BigInteger bi, int gameid) throws SQLException {

        preparedStatementDeleteReservation.setBigDecimal(1, new BigDecimal(bi));
        preparedStatementDeleteReservation.setInt(2, gameid);
        preparedStatementDeleteReservation.execute();

        return true;
    }

    public boolean createReservation(BigInteger bi, int gameid, int inserted) throws SQLException {

        preparedStatementCreateReservation.setBigDecimal(1, new BigDecimal(bi));
        preparedStatementCreateReservation.setInt(2, gameid);
        preparedStatementCreateReservation.setInt(3, inserted);

        try {
            preparedStatementCreateReservation.execute();
        } catch (SQLException sex) {
            String s = sex.getSQLState();
            if (s.equals("23505")) {
                return false;
            }
            throw sex;
        }
        return true;
    }


}
