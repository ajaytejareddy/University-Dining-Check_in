package com.food.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogIn {
	
	private String email,password,name;
	private boolean isSignedIn;
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
	//instance initializer
	{
		password = "1234";
		isSignedIn = false;
	}
	
	public LogIn() throws IOException {
		System.out.println("Choose any Option:");
		System.out.println("1.SignIn");
		System.out.println("2.SignUp");
		String option = input.readLine();
		
		while (option!="1"||option!="2")
			option = input.readLine();
		
		if (option == "1")
			signIn();
		else
			signUp();
	}
	
	
	
	private void signIn() throws IOException {
		
		System.out.printf("\nEnter Email: ");
		email = input.readLine().toLowerCase();
		
		System.out.println("given name = "+email+" and password = "+password);
		//database logic for signing

	}
	
	private void signUp() throws IOException {
		
		System.out.printf("\nEnter Email: ");
		email = input.readLine().toLowerCase();
		System.out.printf("\nEnter your Name:");
		name = input.readLine();
		//database for signup
		
		
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getName() {
		// returns name by retrieving from database or local variable
		if(name == null) {
			name = retrieveName();
		}
		return name;
	}
	
	//retrieves user name from database
	private String retrieveName() {
		
		
		
		return name;
		
	}
}
