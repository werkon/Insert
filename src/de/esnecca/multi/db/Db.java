package de.esnecca.multi.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import de.esnecca.multi.Game;

public class Db {

    String user;
    String password;
    String url;

    public Db(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public int getGameId(Game game) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password);
        String selectSQL = "select id from games where width = ? and height = ? and colors = ? and wins = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

        preparedStatement.setInt(1, game.getWidth());
        preparedStatement.setInt(2, game.getHeight());
        preparedStatement.setInt(3, game.getColors());
        preparedStatement.setInt(4, game.getWins());

        ResultSet rs = preparedStatement.executeQuery();

        int ret = 0;
        if (rs.next()) {
            ret = rs.getInt("id");
        }

        rs.close();
        preparedStatement.close();
        conn.close();
        return ret;

    }

    public boolean createGame(Game game) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        String selectSQL = "insert into games (width, height, colors, wins) values(?,?,?,?);";
        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

        preparedStatement.setInt(1, game.getWidth());
        preparedStatement.setInt(2, game.getHeight());
        preparedStatement.setInt(3, game.getColors());
        preparedStatement.setInt(4, game.getWins());
        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
        return true;
    }

    public boolean deleteGame(Game game) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        String selectSQL = "delete from games where width = ? and height = ? and colors = ? and wins = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

        preparedStatement.setInt(1, game.getWidth());
        preparedStatement.setInt(2, game.getHeight());
        preparedStatement.setInt(3, game.getColors());
        preparedStatement.setInt(4, game.getWins());
        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
        return true;
    }

    public boolean createEntry(DbEntry dbEntry) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        String selectSQL = "insert into entries (bi, gameid, result, inserted) values(?,?,?,?);";
        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

        preparedStatement.setBigDecimal(1, new BigDecimal(dbEntry.getBi()));
        preparedStatement.setInt(2, dbEntry.getGameid());
        preparedStatement.setInt(3, dbEntry.getResult());
        preparedStatement.setInt(4, dbEntry.getInserted());

        try {
            preparedStatement.execute();
        } catch (SQLException sex) {
            String s = sex.getSQLState();
            if (s.equals("23505")) {
                return false;
            }
            throw sex;
        } finally {
            preparedStatement.close();
            conn.close();
        }
        return true;
    }

    public boolean deleteEntry(BigInteger bi, int gameid) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        String selectSQL = "delete from entries where bi = ? and gameid = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

        preparedStatement.setBigDecimal(1, new BigDecimal(bi));
        preparedStatement.setInt(2, gameid);
        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
        return true;
    }

    public DbEntry getDbEntry(BigInteger bi, int gameid) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        String selectSQL = "select result, inserted from entries where bi = ? and gameid = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

        preparedStatement.setBigDecimal(1, new BigDecimal(bi));
        preparedStatement.setInt(2, gameid);

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            int result = rs.getInt("result");
            int inserted = rs.getInt("inserted");

            rs.close();
            preparedStatement.close();
            conn.close();

            return new DbEntry(bi, gameid, result, inserted);
        }

        rs.close();
        preparedStatement.close();
        conn.close();
        return null;
    }

}
