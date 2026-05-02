package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/exam_monitor_db";

    private static final String USER = "root";
    private static final String PASS = "2nd-semester-sql-29-3";

    public static Connection getConnection() {
        try {
            Connection con =
                    DriverManager.getConnection(URL, USER, PASS);

            System.out.println("Database Connected!");
            return con;

        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}