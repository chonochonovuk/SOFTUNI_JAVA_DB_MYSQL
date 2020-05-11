package demo;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {

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
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM colonists WHERE id = ?");
            System.out.println("Enter id :");
            String ids = scanner.nextLine().trim();
            ids = ids.equals("") ? "1" : ids ;
            stmt.setInt(1,Integer.parseInt(ids));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                System.out.printf("| %d | %-10.10s | %-10.10s |%n",rs.getInt("id"),rs.getString("first_name"),
                        rs.getString("last_name"));
            }
        }catch (SQLException esql){
            esql.printStackTrace();
        }


    }
}
