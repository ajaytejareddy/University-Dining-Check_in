package com.food.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Manager {
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	@SuppressWarnings("unused")
	private Login user;
	private Connection conn;
	private PreparedStatement ps;
	
	public Manager(Login user, Connection conn) {
		this.user = user;
		this.conn = conn;
	}
	
	class EmpPair{
		
		private String name,email,dept;
		public EmpPair(String email,String name) {
			this.name = name;
			this.email = email;
			this.dept = dept;
		}
		
		public String getName() {
			return name;
		}
		public String getEmail() {
			return email;
		}
		
	}
	
	private void addCookItems() {
		
		
	}

	private void viewOrders() {
		
		
	}

	private void addItems() throws IOException {
		String itemName,Description;
		float price;
		DecimalFormat df = new DecimalFormat("#.##");
		
		System.out.print("Enter Food Item Name:");
		itemName = input.readLine();
		System.out.print("Enter Description: ");
		Description = input.readLine();
		while(true) {
			System.out.print("Enter Price:");
			try {
				price = Float.parseFloat(input.readLine());
				break;
			}catch(Exception e) {
				System.out.println("WRONG INPUT");
			}
		}
		
		//Databse
		try {
			
			ps = conn.prepareStatement("INSERT INTO ListOfItems VALUES(?,?,?)");
			ps.setString(1,itemName);
			ps.setString(2,Description);
			ps.setFloat(3,price);
			int rowsUpdated = ps.executeUpdate();
			if(rowsUpdated>0) {
				System.out.println("Successfully deleted "+rowsUpdated+" Customer/Customers");
			}else {
				System.out.println("Unsuccessful in deleting records");
			}
			
			
		} catch (SQLException e) {
			System.out.println("Database ERROR");
		}
		
		
		System.out.println("\t\tUpdated Items List");
		itemList();
		
		
	}

	private void mEmployees() throws IOException {
		String a;
		String query;
		while(true) {
			System.out.println("\t\tMenu");
			System.out.println("\t1.Add Employee");
			System.out.println("\t2.Remove Employee");
			System.out.println("\t3.Change Employee Department");
			System.out.println("\tPress any key for Main Menu");
			System.out.print("Choose an option:");
			a=input.readLine();
			
			if(a.equals("1")) {
					String name,email,ismanager = "",isfulltime="";
					int deptOption;
					
					
					System.out.print("Name of Employee: ");
					name = input.readLine();
					System.out.print("Email of Employee: ");
					email = input.readLine().toLowerCase();
					while(true) {
						System.out.print("Is a Manager(y/n):");
						ismanager = input.readLine().toLowerCase();
						if(!(ismanager.equals("y") || ismanager.equals("n") || ismanager.equals("yes") || ismanager.equals("no"))) {
							System.out.println("please enter correct input:");
						}
						else {
							if(ismanager.equals("yes")||isfulltime.equals("y"))
								ismanager = "y";
							else
								ismanager = "n";
							break;
						}
					}
					while(!(isfulltime.equals("y") || isfulltime.equals("n") || isfulltime.equals("yes") || isfulltime.equals("no"))) {
						System.out.print("Is a Fulltime Employee(y/n):");
						isfulltime = input.readLine().toLowerCase();
						if(!(isfulltime.equals("y") || isfulltime.equals("n") || isfulltime.equals("yes") || isfulltime.equals("no"))) {
							System.out.println("please enter correct input:");
						}
						else {
							if(isfulltime.equals("yes")||isfulltime.equals("y"))
								isfulltime = "y";
							else
								isfulltime = "n";
							break;
						}
					}
					ArrayList<String> dept = getDept();
					int i;
					
					System.out.println("\t\tChoose Department of Employee:");
					for(i=0;i<dept.size();i++) {
						  System.out.println("\t"+i+"."+dept.get(i));
					}
					while(true) {
						try {
							System.out.print("Enter a number from above list: ");
						
							deptOption = Integer.parseInt(input.readLine());
							break;
						}
						catch(NumberFormatException e) {
							System.out.println("Wrong  Input");
						}
					}
					
					//database insertion
					query = "INSERT INTO Employees(Name,email,ismanager,isfulltime,department) VALUES(?,?,?,?,?)";
					
					try {
						ps = conn.prepareStatement(query);
						ps.setString(1, name);
						ps.setString(2, email);
						ps.setString(3, ismanager);
						ps.setString(4, isfulltime);
						ps.setString(5, dept.get(deptOption));
						int res = ps.executeUpdate();
						if(res==1) {
							
							System.out.println("Employee added successfully");
						}
						conn.commit();
						
					} catch (SQLException e) {
						System.out.println("Database ERROR");
					}					
				
			}
			
			else if(a.equals("2")) {
				int inp,i;
				ArrayList<EmpPair> emp;
				
				
				System.out.println("\t Remove any Employee:");
				emp = getEmp();
				for(i=0;i<emp.size();i++) {
					System.out.println("\t"+i+". "+emp.get(i).getName()+" ( "+emp.get(i).getEmail()+" )");
				}
				System.out.println("Press "+i+" to cancel");
				
				try {
					conn.close();
				} catch (SQLException e1) {
					System.out.println("Database ERROR");
				}
				while(true) {
				System.out.print("Enter Option:");
					try {
					inp = Integer.parseInt(input.readLine());
					break;
					}catch(Exception e1) {
						System.out.println("Enter Correct INPUT");
					}
				
				}
				
				if(inp==emp.size()) {
					continue;
				}
				
				try {
					
					ps = conn.prepareStatement("DELETE FROM Employees WHERE email = ?");
					ps.setString(1,emp.get(inp).getEmail());
					int rowsDeleted = ps.executeUpdate();
					if(rowsDeleted>0) {
						System.out.println("Successfully deleted "+rowsDeleted+" Customer/Customers");
					}else {
						System.out.println("Unsuccessful in deleting records");
					}
					
				} catch (SQLException e) {
					
					System.out.println("Database ERROR");
				
				}
				
				
			}
			else if(a.contentEquals("3")) {
				
				ArrayList<String> dept = getDept();
				int i,deptOption,inp;
				ArrayList<EmpPair> emp;
				
				
				System.out.println("\t Remove any Employee:");
				emp = getEmp();
				for(i=0;i<emp.size();i++) {
					System.out.println("\t"+i+". "+emp.get(i).getName()+" ( "+emp.get(i).getEmail()+" )");
				}
				System.out.println("Press "+i+" to cancel");
				
				try {
					conn.close();
				} catch (SQLException e1) {
					System.out.println("Database ERROR");
				}
				while(true) {
				System.out.print("Enter Option:");
					try {
					inp = Integer.parseInt(input.readLine());
					break;
					}catch(Exception e1) {
						System.out.println("Enter Correct INPUT");
					}
				
				}
				
				if(inp==emp.size()) {
					continue;
				}
				
				
				
				System.out.println("\t\tChoose Department of Employee:");
				for(i=0;i<dept.size();i++) {
					  System.out.println("\t"+i+"."+dept.get(i));
				}
				while(true) {
					try {
						System.out.print("Enter a number from above list: ");
					
						deptOption = Integer.parseInt(input.readLine());
						break;
					}
					catch(NumberFormatException e) {
						System.out.println("Wrong  Input");
					}
				}
				
				try {
					ps = conn.prepareStatement("UPDATE Employees SET Department = ? WHERE Email = ?");
					ps.setString(1,dept.get(deptOption));
					ps.setString(2,emp.get(inp).getEmail());
					int rowsUpdated=ps.executeUpdate();
					
					if(rowsUpdated>0) {
						System.out.println("Successfully deleted "+rowsUpdated+" Customer/Customers");
					}else {
						System.out.println("Unsuccessful in deleting records");
					}
					
				} catch (SQLException e) {
					System.out.println("DATABSE ERROR");
				}
				
				
			}
			
			else{
				return;
			}
		}
		
	}

	public boolean run() throws IOException {
		while(true) {
			String a;
			System.out.println("\t\tMenu:");
			System.out.println("\t1.Manage Employees");
			System.out.println("\t2.Add Food Items to List");
			System.out.println("\t3.View Customer Orders");
			System.out.println("\t4.Cook Items");
			System.out.println("\t5.LogOut");
			System.out.print("Choose an option:");
			a=input.readLine();
			switch(a) {
				case "1":mEmployees();
					break;
				case "2":addItems();
					break;
				case "3":viewOrders();
					break;
				case "4":addCookItems();
					break;
				case "5":return false;
				default:
			}
		
		}
	}
	
	private ArrayList<String> getDept() {
		ArrayList<String> result = new ArrayList<String>();
		String query = "SELECT DName FROM Department";
		
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result.add(rs.getString("DName"));
			}
			
			
			
		} catch (SQLException e) {
			System.out.println("Database error");
		} 
		
		
		return result;
	}
	
	
	private ArrayList<EmpPair> getEmp(){
		ArrayList<EmpPair> result = new ArrayList<EmpPair>();
		
		
		
		String query = "SELECT Email,Name FROM Employees";
		
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result.add(new EmpPair(rs.getString("Email"),rs.getString("Name")));
			}
			
			
		} catch (SQLException e) {
			System.out.println("Database error");
		} 
		
		
		return result;
		
	}

	
	private void itemList() {
		try {
			ps = conn.prepareStatement("SELECT * FROM ListOfItems");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println("\t\t"+rs.getString("ItemName")+"\t"+rs.getString("Description")+"\t"+rs.getString("Price_Per_Serving"));
			}	
			
		} catch (SQLException e) {
			System.out.println("Database error");
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		} 
	}
	
}
