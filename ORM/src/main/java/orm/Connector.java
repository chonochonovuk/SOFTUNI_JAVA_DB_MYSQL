package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {
    private static Connection connection;
    public static final String CONNECTION = "jdbc:mariadb://localhost:3306/";


    public static void createConnection(String username, String password, String dbname) {
        Properties props = new Properties();
        props.setProperty("username", username);
        props.setProperty("password", password);

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Driver Found!!!");
            connection = DriverManager.getConnection(CONNECTION + dbname, props);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
