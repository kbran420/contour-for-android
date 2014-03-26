import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton-Class to handle database-access
 * @author j0h1
 *
 */
public class DatabaseManager {

	private static DatabaseManager dbm;
	private Connection connection;
	
	protected DatabaseManager() {
		dbm = this;
	}
	
	/**
	 * Creates/returns existing instance of DatabaseManager
	 * @return instance of DatabaseManager
	 */
	public static DatabaseManager getInstance() {
		if (dbm == null)
			return new DatabaseManager();
		return dbm;
	}
	
	/**
	 * Opens a connection to SQLite-Database
	 * @param host defines path to persistance (database)
	 * @throws ClassNotFoundException when JDBC-driver for SQLite is not installed properly
	 */
	public void openConnection(String host) throws ClassNotFoundException {
		// load the sqlite-JDBC driver using the current class loader
	    Class.forName("org.sqlite.JDBC");
	    
	    // host = "jdbc:sqlite:sample.db"
	    try {
			connection = DriverManager.getConnection(host);
		} catch (SQLException e) {
			// TODO: Exception handling
		}
	}
	
	/**
	 * @return connection object handled by this class
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Closes connection to database
	 * @throws SQLException if closing is not possible (for whatever reason)
	 */
	public void closeConnection() throws SQLException {
		connection.close();
		connection = null;
	}
}
