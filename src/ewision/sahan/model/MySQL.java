package ewision.sahan.model;

import ewision.sahan.loggers.DatabaseLogger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

//    private static String URL = "91.204.209.9";
    private static String URL = "localhost";
//    private static String DATABASE = "sereneh1_harvest";
    private static String DATABASE = "alpha_sub";
    //private static String USERNAME = "sahan";
    //private static String PASSWORD = "Sahan@123";
//    private static String USERNAME = "sereneh1_harvest";
    private static String USERNAME = "alpha";
    private static String PASSWORD = "Alpha@123";
//    private static String PASSWORD = "&lE(1Nf.5JH(";
    private static String PORT = "3306";

    static {
        creteConnection();
    }

    private static void getConfig() {
        try {
            File serial = new File("serial");
            serial.mkdir();

            File config = new File("serial/config.inf");
            config.createNewFile();
            if (config.exists()) {
                FileReader fileReader = new FileReader(config);
                BufferedReader reader = new BufferedReader(fileReader);

                reader.readLine();
                String Url = reader.readLine();
                reader.readLine();
                String Database = reader.readLine();
                reader.readLine();
                String Username = reader.readLine();
                reader.readLine();
                String Password = reader.readLine();
                reader.readLine();
                String Port = reader.readLine();

                MySQL.URL = Url;
                MySQL.DATABASE = Database;
                MySQL.USERNAME = Username;
                MySQL.PASSWORD = Password;
            } else {
                FileWriter fileWriter = new FileWriter(config);
                BufferedWriter writer = new BufferedWriter(fileWriter);

                writer.write("HOST:");
                writer.newLine();
                writer.write(URL);
                writer.newLine();
                writer.write("DATABASE_NAME:");
                writer.newLine();
                writer.write(DATABASE);
                writer.newLine();
                writer.write("USERNAME:");
                writer.newLine();
                writer.write(USERNAME);
                writer.newLine();
                writer.write("PASSWORD:");
                writer.newLine();
                writer.write(PASSWORD);
                writer.newLine();
                writer.write("PORT:");
                writer.newLine();
                writer.write(PORT);

                writer.flush();
            }
        } catch (IOException e) {
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + ":: DB Config Failiure.. \n " + e.getLocalizedMessage());
        }
    }

    private static void creteConnection() {
        try {
            getConfig();

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + URL + ":" + PORT + "/" + DATABASE + "?useSSL=false", USERNAME, PASSWORD);
            DatabaseLogger.logger.log(Level.INFO, System.currentTimeMillis() + " :: DB CONNECTED");
        } catch (ClassNotFoundException | SQLException | NullPointerException e) {
            // System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            // DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            // CREATE USER IF NOT EXISTS 'alpha'@'%' IDENTIFIED BY 'Alpha@123';
            // GRANT ALL PRIVILEGEs ON alpha_sub.* TO 'alpha'@'%';
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
        } catch (java.sql.SQLNonTransientConnectionException | com.mysql.cj.exceptions.CJCommunicationsException | com.mysql.cj.exceptions.ConnectionIsClosedException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Database Network Connection Issue :" + e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            return null;
        } catch (NullPointerException e) {
            DatabaseLogger.logger.log(Level.SEVERE, System.currentTimeMillis() + " :: " + e.getLocalizedMessage() + " -- " + e.getClass().getName(), e.getLocalizedMessage());
            return null;
        }
    }

    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        try {
            PreparedStatement pStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
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
