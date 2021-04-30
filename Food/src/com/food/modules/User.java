package com.food.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class User {
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private Login user;
	private Connection conn;
	private PreparedStatement ps;
	
	class Items{
		String name,dept;
		int quantity;
		float price;
		public Items(String string, String string2, int string3,float ps) {
			name = string;
			dept = string2;
			quantity = string3;
			price = ps;
		}
		public Items(String name,int quantity) {
			this.name = name;
			this.quantity = quantity;
		}
		public Items(String name,int quantity,float price) {
			this.name = name;
			this.quantity = quantity;
			this.price=price;
		}
		public String getName() {
			return name;
		}
		public String getDept() {
			return dept;
		}
		public int getQuantity() {
			return quantity;
		}
		public float getPrice() {
			return price;
		}
		
	}
	
	class Orders{
		int id;
		Timestamp ts;
		ArrayList<Items> items;
		
		public Orders(int id, Timestamp ts, ArrayList<Items> items) {
			this.id = id;
			this.ts = ts;
			this.items = items;
		}
		public int getId() {
			return id;
		}
		public Timestamp getTs() {
			return ts;
		}
		public ArrayList<Items> getItems() {
			return items;
		}
	}

	
	public User(Login user2, Connection conn2) {
		user = user2;
		conn = conn2;
	}
	

	public void run() throws IOException {
		String op;
		while(true) {
			System.out.println("\t\t**USER MENU**");
			System.out.println("\t1.Order Food");
			System.out.println("\t2.View Orders");
			System.out.println("\t3.Manage Orders");
			System.out.println("\t4.Log Out");
			System.out.print("Enter Choice: ");
			op = input.readLine();
			
			switch(op) {
			case "1":oFood();
					break;
			case "2":viewOrders();
					break;
			case "3":manageOrders();
					break;
			case "4":return;
			default: System.out.println("Enter correct option: ");
			}
		}
	}


	private void oFood() throws IOException {
		int Option,quantity;
		int i;
		ArrayList<Items> order,items = getFoodItems();
		order = new ArrayList<Items>();
		
		Timestamp ts = Timestamp.from(Instant.now());
		//System.out.println(ts);
		while(true) {
			System.out.println("\t\tItems\n");
			System.out.format("\t%10s\t%16s%16s\n","Name","Remaining Quantity","Section");
			for(i=0;i<items.size();i++) {
				
				System.out.format("\t%d. %10s%16s%16s\n",i,items.get(i).getName(),items.get(i).getQuantity(),items.get(i).getDept());
				
			}
			System.out.println("\n\npress "+i+" for cancel/check out");
			
			System.out.print("Choose any item:");
			Option = Integer.parseInt(input.readLine());
			
			if(i==Option) {
				break;
			}
			
			System.out.print("Quantity of "+items.get(Option).getName()+" : ");
			while(true) {
				quantity = Integer.parseInt(input.readLine());
				if(quantity>items.get(Option).getQuantity()) {
					System.out.println("Wrong quantity: ");
					continue;
				}
				break;
			}
			System.out.println(items.get(Option).getName()+"  "+quantity);
			order.add(new Items(items.get(Option).getName(),quantity,items.get(Option).getPrice()));
		}
		if(order==null|order.isEmpty()) {
			return;
		}
		
		System.out.println("\t\tYour Cart:\n\titemName\tQuantity");
		for(i=0;i<order.size();i++) {
			System.out.println("\t"+order.get(i).getName()+"\t"+order.get(i).getQuantity());
		}
		System.out.print("Press any key to continue or 2 to cancel:");
		if(input.readLine().contentEquals("2")) {
			return;
		}
		//System.out.println(order.toString());
		
		try {
			
			ps = conn.prepareStatement("INSERT INTO Orders(OrderId,Date_Time,CustId,Status) VALUES(orderId_inrement.NEXTVAL,?,?,?)");
			ts = Timestamp.from(Instant.now());
			ps.setTimestamp(1, ts);
			ps.setString(2, user.getEmail());
			ps.setNString(3,"placed");
			if(ps.executeUpdate()<=0) {
				System.out.println("Error ");
				return;
			}
			conn.commit();
			
			
			ps = conn.prepareStatement("SELECT OrderID FROM Orders WHERE CustId=? AND Date_Time = ?");
			ps.setString(1, user.getEmail());
			ps.setTimestamp(2, ts);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int orderid = rs.getInt("OrderId");
			
			//System.out.println(orderid+" ; "+order.get(0).getPrice());
			
			for(i=0;i<order.size();i++) {
				try {
					ps = conn.prepareStatement("INSERT INTO OrderDetails(OrderId,ItemName,Quantity,Amount) VALUES(?,?,?,?)");
					
					ps.setInt(1,orderid);
					ps.setString(2, order.get(i).getName());
					ps.setInt(3, order.get(i).getQuantity());
					float price = order.get(i).getQuantity()*order.get(i).getPrice();
					ps.setFloat(4, price);
					ps.executeUpdate();
					conn.commit();
					
					
					ps = conn.prepareStatement("DELETE FROM FoodItems WHERE Quantity = ? AND ItemName = ?");
					ps.setInt(1, order.get(i).getQuantity());
					ps.setString(2, order.get(i).getName());
					ps.executeUpdate();
					conn.commit();
					
					
					ps = conn.prepareStatement("UPDATE FoodItems SET Quantity = Quantity - ? WHERE ItemName = ? ");
					//ps.setString(1, order.get(i).getName());
					ps.setInt(1, order.get(i).getQuantity());
					ps.setString(2, order.get(i).getName());
					ps.executeUpdate();
					conn.commit();
					
				}catch(SQLException e) {
					System.out.println("Database ERROR:300x22"+e);
				}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	private void viewOrders() {
		
		try {
			
			ps = conn.prepareStatement("SELECT OrderId,Date_time FROM Orders WHERE CUSTID = ? ORDER BY Date_Time DESC");
			ps.setString(1, user.getEmail());
			ResultSet rs = ps.executeQuery();
			
			System.out.println("\n");
			while(rs.next()) {
				PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM OrderDetails WHERE OrderId=?");
				ps1.setInt(1,rs.getInt("OrderId"));
				
				
				System.out.println("\t\tOrderId = "+rs.getInt("OrderId"));
				ResultSet rset = ps1.executeQuery();
				while(rset.next()) {
					System.out.println("\t "+rset.getString("ITEMNAME")+" \t"+rset.getString("QUANTITY")+"\t $"+rset.getString("AMOUNT"));
				}
				System.out.println("\n");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	
	private void manageOrders() {
		System.out.println("Needs to be implemented");
		
	}
	
	public ArrayList<Items> getFoodItems() {
		ArrayList<Items> res = new ArrayList<Items>();
		try {
			ps = conn.prepareStatement("SELECT ItemName, Dept, Quantity,price_per_serving FROM FoodItems INNER JOIN ListOfItems USING(ItemName) WHERE Quantity>? ORDER BY Dept");
			ps.setInt(1, 0);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				res.add(new Items(rs.getString("ItemName"),rs.getString("Dept"),Integer.parseInt(rs.getString("Quantity")), rs.getFloat("price_per_serving")));
			}
			
		} catch (SQLException e) {
			System.out.println("Database ERROR: 300x1"+e);
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
