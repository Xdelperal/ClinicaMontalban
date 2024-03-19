package persistence.utils;


import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtils {

    public static Connection dbLink;

    public static Connection getConnection() {
        String databaseName = "adminmontalban_clinica";
        String databaseUser = "329292_admin";
        String databasePassword = "adminmontalban!";
        String url = "jdbc:mysql://mysql-adminmontalban.alwaysdata.net:3306/adminmontalban_clinica";


        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return dbLink;

    }


}
