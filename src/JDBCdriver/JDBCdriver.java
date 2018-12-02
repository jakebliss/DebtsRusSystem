package JDBCdriver;

import java.sql.Connection;
import java.sql.Statement;

public class JDBCdriver {
	// JDBC driver name and database URL
	   public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
	   public static final String DB_URL = "jdbc:oracle:thin:@cloud-34-133.eci.ucsb.edu:1521:XE";

	   //  Database credentials
	   public static final String USERNAME = "zakarybliss";
	   public static final String PASSWORD = "password";
	   public static Connection conn = null;
	   public static Statement stmt = null; 
}
