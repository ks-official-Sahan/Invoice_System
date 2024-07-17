package ewision.sahan.model;

import ewision.sahan.loggers.DatabaseLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MySQL {

    private static Connection connection;

    private static final String URL = "localhost";
    private static final String DATABASE = "alpha_sub";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "LeaveMe@666";
    private static final String PORT = "3306";

    static {
        creteConnection();
    }

    private static void creteConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + URL + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
            DatabaseLogger.logger.log(Level.INFO, System.currentTimeMillis() + " :: DB CONNECTED ");
        } catch (ClassNotFoundException | SQLException e) {
            // System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
        }
    }

    public static ResultSet execute(String query) throws SQLException {
        Statement statement = connection.createStatement();
        if (query.startsWith("SELECT")) {
            return statement.executeQuery(query);
        } else {
            statement.executeUpdate(query);
            return null;
        }
    }

    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        PreparedStatement pStatement = connection.prepareStatement(query);
        return pStatement;
    }

    public static ResultSet executeSelect(PreparedStatement pStatement) throws SQLException {
        ResultSet resultSet = pStatement.executeQuery();
        return resultSet;
    }

    public static int executeIUD(PreparedStatement pStatement) throws SQLException {
        return pStatement.executeUpdate();
    }
}
