import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbConnection {

	public static void main(String[] args) throws ClassNotFoundException {

	    Connection connection = null;
	    
	    try {
	    	connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
	    	Statement statement = connection.createStatement();
	    	statement.setQueryTimeout(30);
	    	
	    	statement.executeUpdate("create table Track (tid INTEGER PRIMARY KEY AUTOINCREMENT) WITHOUT ROWID;");
//	        statement.executeUpdate("create table person (id integer, name string)");
//	        statement.executeUpdate("insert into person values(1, 'leo')");
//	        statement.executeUpdate("insert into person values(2, 'yui')");
	        
//	        ResultSet rs = statement.executeQuery("select * from person");
	        
//	        while (rs.next()) {
//	        	System.out.println("name = " + rs.getString("name"));
//	            System.out.println("id = " + rs.getInt("id"));
//	        }
	    } catch (SQLException ex) {
	    	System.err.println(ex.getMessage());
	    } finally {
	    	try {
	    		if (connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException ex) {
	    		System.err.println(ex);
	    	}
	    }
	}

}
