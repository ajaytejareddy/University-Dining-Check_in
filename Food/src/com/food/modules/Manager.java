package com.food.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Manager {
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private Login user;
	private Connection conn;
	private PreparedStatement ps;
	
	public Manager(Login user, Connection conn) {
		this.user = user;
		this.conn = conn;
	}
	
	class EmpPair{
		
		private String name,email;
		public EmpPair(String email,String name) {
			this.name = name;
			this.email = email;
		}
		
		public String getName() {
			return name;
		}
		public String getEmail() {
			return email;
		}
		
	}
	
	class ItemPair{
			
			private String name,price;
			public ItemPair(String name,String price) {
				this.name = name;
				this.price = price;
			}
			
			public String getName() {
				return name;
			}
			public String getPrice() {
				return price;
			}
			
		}
	
	private void addCookItems() throws IOException {
		//String Option;
		int Option,quantity,i;
		ArrayList<ItemPair> items = getItemList();
		System.out.println("\n\t\t**ITEMS LIST**\n\n");
		System.out.println("\tName\tprice");
		for(i=0;i<items.size();i++) {
			System.out.println("\t"+i+"."+items.get(i).getName()+"\t"+items.get(i).getPrice());
		}
		System.out.print("\nEnter you choice: ");
		while(true){
		try{
				Option =  Integer.parseInt(input.readLine());
				break;
			}catch(Exception e) {
				continue;
			}
		}
		System.out.println("Quantity of '"+items.get(Option).getName()+"' Cooked: ");
		while(true){
			try{
					quantity =  Integer.parseInt(input.readLine());
					
						break;
				}catch(Exception e) {
					continue;
				}
			}
		
		try {
			
			ps = conn.prepareStatement("INSERT INTO FoodItems(ItemName,Quantity,MadeBy) VALUES(?,?,?)");
			ps.setString(1, items.get(Option).getName());
			ps.setInt(2, quantity);
			ps.setString(3, user.getEmail());
			
			int rowsUpdated=ps.executeUpdate();
			
			if(rowsUpdated>0) {
				conn.commit();
				
				ps = conn.prepareStatement("UPDATE FoodItems SET Dept = (SELECT Department FROM Employees WHERE Email = ?) WHERE ItemName=?");
				ps.setString(1, user.getEmail());
				ps.setString(2, items.get(Option).getName());
			
				int updated = ps.executeUpdate();
				if(updated>0)
					System.out.println("added successfully");
				conn.commit();
			}
			
		}catch(SQLException e) {
			System.out.println("Database ERROR");
		}
		
		
	}

	private void manageOrders() throws IOException{
		String menuOption;
		System.out.println("\t\t***Menu****");
		System.out.println("\t1.Top 5 most sold item");
		System.out.println("\t2.No of Orders Made By Customers");
		System.out.println("\t3.View Orders");
		System.out.println("\t4.Main Menu");
		
		System.out.println("Enter Your Choice:");
		menuOption = input.readLine();
		
		if(menuOption.contentEquals("1")) {
			
			try {
				ps = conn.prepareStatement("SELECT ItemName,SUM(Quantity) quantity FROM OrderDetails GROUP BY ItemName ORDER BY ItemName DESC FETCH NEXT 5 ROWS ONLY");
				ResultSet rs = ps.executeQuery();
				System.out.println("\t\tTOP 5 MOST SOLD ITEMS");
				System.out.format("\n\t%16s%16s\n","Item Name","Quantity");
				while(rs.next()) {
					System.out.format("\n\t%16s%16s\n",rs.getString("ItemName"),rs.getString("quantity"));
				}
				System.out.println("\n\n\n");
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		else if(menuOption.contentEquals("2")) {
			
			try {
				ps = conn.prepareStatement("SELECT Name,Email,COUNT(OrderID) OrdersMade FROM Orders INNER JOIN Customers ON Customers.Email = Orders.CustID GROUP BY Name,Email ORDER BY OrdersMade DESC");
				ResultSet rs = ps.executeQuery();
				System.out.println("\n\n\t\tTOTAL ORDERS MADE BY CUSTOMES");
				System.out.format("\n\t%16s%16s%16s\n","Customer Name","Email","OrdersMade");
				while(rs.next()) {
					System.out.format("\n\t%16s%16s%16s\n",rs.getString("Name"),rs.getString("Email"),rs.getString("OrdersMade"));
				}
				System.out.println("\n\n\n");
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		else if(menuOption.contentEquals("3")) {
			
					try {
						ps = conn.prepareStatement("SELECT OrderId,Date_Time,Status,Customers.Name CName,Employees.Name EName FROM Orders INNER JOIN Customers ON Customers.email = Orders.CustID INNER JOIN Employees ON Employees.Email = Orders.EmpID ORDER BY Status");
						ResultSet rs = ps.executeQuery();
						System.out.println("\n\n\t\t***ORDERS***");
						System.out.format("\n%10s%10s%16s%16s%16s\n","OrderID","Date Ordered","Status","Customer Name","EmployeeName");
						while(rs.next()) {
							System.out.format("\n%10s%10s%16s%16s%16s\n",rs.getString("OrderId"),rs.getString("Date_Time"),rs.getString("Status"),rs.getString("CName"),rs.getString("EName"));
						}
						System.out.println("\n\n\n");
						
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
		else if(menuOption.contentEquals("4")) {
			return;
		}
		
		return;
		
	}

	private void addItems() throws IOException {
		String itemName,Description;
		float price;
		
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
			System.out.println("Database ERROR"+e);
		}
		
		
		System.out.println("\n\n\t\tUpdated Items List\n");
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
				case "3":manageOrders();
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
				System.out.println("\t"+rs.getString("ItemName")+"\t"+rs.getString("Description")+"\t"+rs.getString("Price_Per_Serving"));
			}
			System.out.println("\n\n");
			
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
	
	private ArrayList<ItemPair> getItemList() {
		ArrayList<ItemPair> res = new ArrayList<ItemPair>();
		
		try {
			ps = conn.prepareStatement("SELECT * FROM ListOfItems");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				res.add(new ItemPair(rs.getString("ItemName"),rs.getString("Price_Per_Serving")));
			}
			System.out.println("\n\n");
			
		} catch (SQLException e) {
			System.out.println("Database ERROR");
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		return res;
		
	}
	
	
}
