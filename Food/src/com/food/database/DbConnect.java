package com.food.database;

import java.util.HashMap;
import java.util.LinkedList;

public interface DbConnect {
	public void getConnection();
	public HashMap<String,LinkedList<String>> getOrderDetails();
	
}
