// for JDBC access
import java.sql.*;
import java.util.Properties;
import com.intersystems.jdbc.IRISDataSource;

public class Test1 {
    protected   static  String              namespace = "SAMPLES";
    protected   static  String              host = "localhost";
    protected   static  int                 port = 1972; 
    protected   static  String              url = "jdbc:IRIS://"+host+":" + port + "/"+namespace;
    protected   static  String              username = "SuperUser";
    protected   static  String              password = "SYS";
    protected   static  String              sqlQuery = "SELECT top 1 b FROM Test.SlowQuery";
        
    
    public static void main(String[] args) {
        Connection dbconnection=null;
        try {
        	
			Properties props = new Properties();
			props.setProperty("SharedMemory", "false");
			props.setProperty("user", username);
			props.setProperty("password",password);
			props.setProperty("FeatureOption","3");

 			Class.forName ("com.intersystems.jdbc.IRISDriver");
 			dbconnection = DriverManager.getConnection(url,props);

            /* DS経由ではsetNetworkTimeout()が機能しない模様 */
/*            
            IRISDataSource ds = new IRISDataSource(); 

             ds.setURL(url);
             ds.setUser(username);
             ds.setPassword(password);
             dbconnection = ds.getConnection();
*/
 			dbconnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
 			dbconnection.setAutoCommit(true);
            dbconnection.setNetworkTimeout(null, 1000);
 			PreparedStatement pstmt = dbconnection.prepareStatement(sqlQuery);
            java.sql.ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                 System.out.println(rs.getInt(1));
            }
            pstmt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
            	dbconnection.close();
            } catch (Exception ex) {
            }
        }    
    }

}
