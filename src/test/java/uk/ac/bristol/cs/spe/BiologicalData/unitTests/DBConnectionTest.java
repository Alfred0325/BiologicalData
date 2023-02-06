package uk.ac.bristol.cs.spe.BiologicalData.unitTests;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;

/**
 *  checking the connection between the database
 */

@SpringBootTest
public class DBConnectionTest {

    @Test
    public void dbcall() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://biodb-3.camdnuwsjxkm.us-east-1.rds.amazonaws.com/biologicalData";
            String username = "admin";
            String password = "verylongpassword";
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("successful connection!");
            System.out.println(connection);
        } catch (Exception e) {
            System.out.println("no such database");
        }
    }
}
