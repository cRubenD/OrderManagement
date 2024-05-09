package connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A factory class for managing database connections. This class employs the Singleton pattern
 * to ensure that only one instance is used to create connections. It provides methods for establishing
 * connections and for closing connections, statementss and result sets.
 * @author Ruben
 */

public class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/ordersmanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    /**
     * The single instance of the ConnectionFactory, implementing the Singleton pattern.
     */
    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Private constructor to prevent instantiation from outside the class.
     * It loads the JDBC driver to ensure it's available for establishing connections.
     */
    private ConnectionFactory(){
        try{
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new connection to the database using the specified URL, user and password.
     * This method handles any SQL exception and logs of warning if an error occurs during connection.
     * @return a Connection object representing the database connection
     */
    private Connection createConnection(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
            connection = null;
        }
        return connection;
    }

    /**
     * Gets a connection to the database. This method retrieves a connection from the
     * Singleton instance. If a connection is required throughout the application,
     * this method should be used to maintain a consistent connection source.
     * @return A Connection object representing the database connection
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Closes a database connection. This method ensures that resources are properly
     * released and logs a warning if an error occurs during closure.
     * @param connection The Connection to be closed
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close connection", e);
            }
        }
    }

    /**
     * Close a database statement. This method should be used to release resources
     * associated with statements.
     * @param statement The Statement to be closed
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close connection", e);
            }
        }
    }

    /**
     * Close a database result set. This method should be used to release resources
     * associated with result sets.
     * @param resultSet The ResultSet to be closed
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close result set", e);
            }
        }
    }

}
