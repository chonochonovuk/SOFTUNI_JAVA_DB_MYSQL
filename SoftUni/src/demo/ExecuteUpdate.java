package demo;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class ExecuteUpdate {

    public static final String CONNECTION = "jdbc:mariadb://localhost:3306/exams_mysql?useSSL=false";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter username default (root) :");
//        String user = scanner.nextLine().trim();
//        user = user.equals("") ? "root" : user;
//
//        System.out.println("Enter password default (empty) :");
//        String password = scanner.nextLine().trim();
//        password = password.equals("") ? "" : password;

//        Properties props = new Properties();
//        props.setProperty("user",user);
//        props.setProperty("password",password);


//        Or

        Properties props = new Properties();
        props.setProperty("user","chono");
        props.setProperty("password","chono0511");

        //Check drivers ,before establish Connection

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Driver Found!!!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try(Connection connection = DriverManager.getConnection(CONNECTION,props);

        ){
            System.out.println("Connected");
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO colonists (first_name,last_name,ucn,birth_date)" +
                    "VALUE (?,?,?,?)");

            stmt.setString(1,"Chono");
            stmt.setString(2,"Chonov");
            stmt.setString(3,"7675894517");
            stmt.setString(4,"1978-02-05");

            int rows  = stmt.executeUpdate();

            System.out.println("Rows updated " + rows);
        }catch (SQLException esql){
            esql.printStackTrace();
        }


    }
}
