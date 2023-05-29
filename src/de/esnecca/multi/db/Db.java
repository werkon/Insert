package de.esnecca.multi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public Connection getConnection() throws SQLException{

        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

}
