package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class dbConnection {

//    Connection conn;
//    Statement stmt;

    private static final String dbName = "seb_systemDB";
    private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String dbURL = "jdbc:mysql://localhost:3306/" + dbName;
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    public Connection createConnection() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            if (conn != null) {
//                System.out.println("Connected Successfully");
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("Driver not found: " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Error: " + ex.getMessage());
        }
        return conn;
    }
    
}


