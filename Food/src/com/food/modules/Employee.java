package com.food.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class Employee {
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private Login user;
	private Connection conn;
	private PreparedStatement ps;
	
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
	
	public Employee(Login user, Connection conn) {
		this.user = user;
		this.conn = conn;
	}

	public void run() throws IOException {
		while(true) {
			String a;
			System.out.println("\t\tMenu:");
			System.out.println("\t1.Cook Item");
			System.out.println("\t2.Process Customer Orders");
			System.out.println("\t3.LogOut");
			System.out.print("Choose an option:");
			a=input.readLine();
			switch(a) {
				case "1":addCookItems();
						break;
				case "2":manageOrders();
						break;
				case "3":return;
				default:
			}
		
		}
	}
	

	private void manageOrders() {
		System.out.println("Not Available");
		
	}

	private void addCookItems() {
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
					
					ps = conn.prepareStatement("INSERT INTO ard129.FoodItems(ItemName,Quantity,MadeBy) VALUES(?,?,?)");
					ps.setString(1, items.get(Option).getName());
					ps.setInt(2, quantity);
					ps.setString(3, user.getEmail());
					
					int rowsUpdated=ps.executeUpdate();
					
					if(rowsUpdated>0) {
						conn.commit();
						
						ps = conn.prepareStatement("UPDATE ard129.FoodItems SET Dept = (SELECT Department FROM ard129.Employees WHERE Email = ?) WHERE ItemName=?");
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

	private ArrayList<ItemPair> getItemList() {
			
		ArrayList<ItemPair> res = new ArrayList<ItemPair>();
		try {
			ps = conn.prepareStatement("SELECT * FROM ard129.ListOfItems");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				res.add(new ItemPair(rs.getString("ItemName"),rs.getString("Price_Per_Serving")));
			}
			System.out.println("\n\n");
				
		} catch (SQLException e) {
			System.out.println("Database ERROR/Item Already Present");
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