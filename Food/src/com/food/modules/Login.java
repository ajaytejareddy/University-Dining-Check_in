package com.food.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {
	
	@SuppressWarnings("unused")
	private String email,password,name,address;
	private boolean isSignedIn=false, isEmployee = false, isManager = false;
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
	private Connection conn; 
    private Statement stmt;
    private PreparedStatement ps;
	
	//instance initializer
	{
		password = "1234";
		isSignedIn = false;
	}
	
	public Login(Connection conn) throws IOException{
		this.conn = conn;
		if(isSignedIn == false) {
			userInput();
		}
	}
	
	private void userInput() throws IOException{
		System.out.println("Choose any Option:");
		System.out.println("1.SignIn");
		System.out.println("2.SignUp");
		System.out.println("3.Exit from Program");
		String option = input.readLine();
		
		//System.out.println((option.equals("1"))+"");
		
		while (!(option.equals("1") ^ option.equals("2")^option.equals("3")))
			option = input.readLine();
		
		if (option.equals("1"))
			signIn();
		else if(option.equals("2"))
			signUp();
		else if(option.equals("3")) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			System.exit(0);
		}
			
	}
	
	
	
	
	private void signIn() throws IOException {
		
		System.out.printf("\nEnter Email: ");
		email = input.readLine().toLowerCase();
		
		//System.out.println("given name = "+email+" and password = "+password);
		//database logic for signing
		
		if(checkCustUser(email) == 1) {
			//System.out.println("user login");
			userLogin(email);
		}
		
		else if(checkEmpUser(email) == 1) {
			//System.out.format("%32s%16s\n","","Employee LoggedIn");
			isEmployee = true;
			empLogin(email);
		}
		else {
			System.out.println("**User not EXIST**");
		}
		
		
	}
	
	private void empLogin(String email) {
		String query = "SELECT name,isManager FROM Employees WHERE email=?";
		ResultSet rsEmp=null;
		
		
		try {
			//setting prepared statement object and set parameters
			ps=conn.prepareStatement(query);
			ps.setString(1, email);
			
			//execute query
			rsEmp = ps.executeQuery();
			rsEmp.next();
			
			name = rsEmp.getString("name");
			isManager = rsEmp.getString("isManager").equals("Y");
			System.out.format("%32s%16s\n","","Employee LoggedIn");
			System.out.println("Name = "+name+"\temail = "+email+" \tisManager?="+ isManager);
			
		}
		catch(SQLException e) {
			e.getMessage();
			System.out.println("Database ERROR");
		}
		return ;
	}

	private void userLogin(String email) {
		String query = "SELECT name,password,address FROM Customers WHERE email=?";
		ResultSet rsCust=null;
		
		
		try {
			//setting prepared statement object and set parameters
			ps=conn.prepareStatement(query);
			ps.setString(1, email);
			
			//execute query
			rsCust = ps.executeQuery();
			rsCust.next();
			
			name = rsCust.getString("name");
			password = rsCust.getNString("password");
			address = rsCust.getNString("address");
			
			System.out.format("\n%32s%16s\n","","User Login Successful");
			System.out.format("\n%16s%s%16s%s\n","name = ",name,"email = ",email);
			
		}
		catch(Exception e) {
			System.out.println("Database ERROR");
		}		
		return ;
		
	}

	private void signUp() throws IOException {
		
		String query,address;
		System.out.printf("\nEnter Email: ");
		email = input.readLine().toLowerCase();
		System.out.printf("\nEnter your Name: ");
		name = input.readLine();
		System.out.printf("\nEnter Address(Optional): ");
		address = input.readLine();
		
		//database for signup
		if(address.isEmpty())
			query = "INSERT INTO customers(Name,Email,password) Values('"+name+"', '"+email+"','"+password+"')";
		else
			query = "INSERT INTO customers(Name,Email,password,address) Values('"+name+"', '"+email+"','"+password+"','"+address+"')";
		
		if(checkCustUser(email)!=0 | checkEmpUser(email)!= 0) {
			System.out.println("\n**Email already EXISTS**\n");
			userInput();
		}
		
		
		try {	
			stmt = conn.createStatement();
				
			int a = stmt.executeUpdate(query);
			if(a==1) {
				conn.commit();
				isSignedIn = true;
				System.out.println("User profile created successfully");
			}
		}
		catch(SQLException e) {
			isSignedIn = false;
			System.out.println("Database Error: 100x3");
			System.exit(0);
		}
		
		
	}

	private int checkCustUser(String email) throws IOException {
		String query = "SELECT COUNT(*) FROM Customers WHERE email=?";
		ResultSet rsCust=null;
		
		
		try {
			//setting prepared statement object and set parameters
			ps=conn.prepareStatement(query);
			ps.setString(1, email);
			
			//execute query
			rsCust = ps.executeQuery();
			rsCust.next();
			
			return rsCust.getInt(1);
		}
		catch(Exception e) {
			System.out.println("Database ERROR");
			userInput();
		}
		return 0;
	}
	
	
	private int checkEmpUser(String email) throws IOException {
		String query = "SELECT COUNT(*) FROM Employees WHERE email=?";
		ResultSet rsEmp = null;
		
		
		try {
			//setting prepared statement object and set parameters
			ps=conn.prepareStatement(query);
			ps.setString(1, email);
			
			//execute query
			rsEmp = ps.executeQuery();
			rsEmp.next();
			
			return rsEmp.getInt(1);
			
		}
		catch(Exception e) {
			System.out.println("Database ERROR");
			userInput();
		}
		return 0;
		
	}
	
	
	
	public String getEmail() {
		return email;
	}
	
	public String getName() {
		// returns name by retrieving from database or local variable
		return name;
	}
	
	public boolean getEmpStatus() {
		return isEmployee;
	}
	
	public boolean getManagerStatus() {
		return isManager;
	}
 	
}
