package ewision.sahan.model;

import ewision.sahan.loggers.DatabaseLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class MySQL {

    private static Connection connection;

    private static final String URL = "localhost";
    private static final String DATABASE = "alpha_sub";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "LeaveMe@666";
    private static final String PORT = "3306";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + URL + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // System.out.println(e.getMessage());
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
        }
    }

    public static ResultSet execute(String query) throws Exception {
        Statement statement = connection.createStatement();
        if (query.startsWith("SELECT")) {
            return statement.executeQuery(query);
        } else {
            statement.executeUpdate(query);
            return null;
        }
    }

}
