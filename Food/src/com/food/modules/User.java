package com.food.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class User {
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private Login user;
	private Connection conn;
	private PreparedStatement ps;
	
	
	private void userMenu() throws IOException {
		String op;
		
		System.out.println("\t\t**USER MENU**");
		System.out.println("\t1.Order Food");
		System.out.println("\t2.View Orders");
		System.out.println("\t3.Manage Orders");
		System.out.println("\t4.Log Out");
		System.out.print("Enter Choice: ");
		op = input.readLine();
		
		switch(op) {
			
		}
		
	}
	
	
}
