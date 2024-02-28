package persistence.utils;


import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtils {

    public Connection dbLink;

    public Connection getConnection() {
        String databaseName = "adminmontalban_clinica";
        String databaseUser = "329292_admin";
        String databasePassword = "adminmontalban!";
        String url = "mysql-adminmontalban.alwaysdata.net" + databaseName;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return dbLink;

    }


}
