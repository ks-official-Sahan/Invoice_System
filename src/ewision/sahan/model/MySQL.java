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
    private static final String USERNAME = "sahan";
    private static final String PASSWORD = "Sahan@123";
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
            //DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
        }
    }

    public static ResultSet execute(String query) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            if (query.startsWith("SELECT")) {
                return statement.executeQuery(query);
            } else {
                statement.executeUpdate(query);
                return null;
            }
        } catch (NullPointerException e) {
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            return null;
        }
    }

    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        try {
            PreparedStatement pStatement = connection.prepareStatement(query);
            return pStatement;
        } catch (NullPointerException e) {
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            return null;
        }
    }

    public static PreparedStatement getPreparedStatement(String query, String[] generatedKeys) throws SQLException {
        try {
            PreparedStatement pStatement = connection.prepareStatement(query, generatedKeys);
            return pStatement;
        } catch (NullPointerException e) {
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            return null;
        }
    }

    public static ResultSet executeSelect(PreparedStatement pStatement) throws SQLException {
        try {
            ResultSet resultSet = pStatement.executeQuery();
            return resultSet;
        } catch (NullPointerException e) {
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            return null;
        }
    }

    public static ResultSet executeInsert(PreparedStatement pStatement) throws SQLException {
        try {
            if (pStatement.executeUpdate() > 0) {
                ResultSet resultSet = pStatement.getGeneratedKeys();
                return resultSet;
            }
            throw new SQLException("No results found");
        } catch (NullPointerException e) {
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            return null;
        }
    }

    public static int executeIUD(PreparedStatement pStatement) throws SQLException {
        try {
            return pStatement.executeUpdate();
        } catch (NullPointerException e) {
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            return 0;
        }
    }
}
