package de.esnecca.multi.db;

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
        String selectSQL = "select id from games where width = ? and height = ? and colors = ? and wins = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

        preparedStatement.setInt(1, game.getWidth());
        preparedStatement.setInt(2, game.getHeight());
        preparedStatement.setInt(3, game.getColors());
        preparedStatement.setInt(4, game.getWins());

        ResultSet rs = preparedStatement.executeQuery();

        int ret = 0;
        while (rs.next()) {
            ret = rs.getInt("id");
        }

        rs.close();
        preparedStatement.close();
        conn.close();
        return ret;

    }

}
