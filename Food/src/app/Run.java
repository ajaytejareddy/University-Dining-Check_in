package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.food.modules.*;

public class Run {
	
	static BufferedReader keyboard;
    static Connection conn; 
    static Statement stmt;
	
	private static void dbConnect() {
		
		//database user name and password
    	String username="ard129", password = "CS470_6609";

    	try { 

     	   DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

                 System.out.println("Registered the driver...");

     	    conn = DriverManager.getConnection (
                        "jdbc:oracle:thin:@oracle1.wiu.edu:1521/toolman.wiu.edu",
     		    username, password);

     	    conn.setAutoCommit(false);
     	    
     	   stmt = conn.createStatement();
    	}
    	
    	catch(SQLException e)
	    {
    		System.out.println("Error :100x1");
    		//System.out.println("Caught SQL Exception: \n     " + e);
    		System.exit(0);
	    }
		return ;

	}
	
	
	
	public static void main(String[] args) throws IOException {
		
		dbConnect();
		while(true) {
			Login user = new Login(conn);
			
			if(user.getManagerStatus()) {
				Manager manager = new Manager(user,conn);
				if(manager.run()) {
					continue;
				}
			}
			
			else if(user.getEmpStatus()) {
				Employee emp = new Employee(user,conn);
				emp.run();
			}
			
			else {
				User cust = new User(user,conn);
				cust.run();
			}
			
		}
	}
	
}
