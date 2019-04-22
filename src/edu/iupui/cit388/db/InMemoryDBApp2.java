package edu.iupui.cit388.db;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class InMemoryDBApp2 {

	public static void main(String[] args) throws Exception {

		String databaseConnectionUrl = "jdbc:derby:memory:demo;create=true";
		System.out.println("Initiating DB Connection ... ");
		try (Connection connection = DriverManager.getConnection(databaseConnectionUrl)) {

			System.out.println("Connection established. ");

			Statement sql = connection.createStatement();
			
			System.out.println("Create items table");

			int result = sql.executeUpdate("Create table items" 
					+ " (" 
					+ " id int primary key," 
					+ " name varchar(30),"
					+ " price decimal(5,2)" 
					+ ")");

			System.out.println("Created items table " + result);
			
			System.out.println("Insert to items table");
			
			result = sql.executeUpdate(String.format("insert into items values ( %d, '%s', %.2f )", 1, "Egg", 1.99));
			result = sql.executeUpdate(String.format("insert into items values ( %d, '%s', %.2f )", 2, "Milk", 3.99));
			
			System.out.println("Inserted to items table " + result);
			
			System.out.println("Read from items table");
			
			ResultSet rs = sql.executeQuery("Select * from items");
			
			loopResultSet(rs);
			
			
			result = sql.executeUpdate(String.format("update items set price = %.2f where id = %d ", 2.99, 2));
			
			System.out.println("Updated to items table " + result);

			rs = sql.executeQuery(String.format("Select * from items where id = %d ", 2));
			
			loopResultSet(rs);

			System.out.println(" Using PreparedStatement ");
			PreparedStatement prepareStatement = connection.prepareStatement("Select * from items where id = ? and name = ? ");
			
			prepareStatement.setInt(1, 2);
			prepareStatement.setString(2, "Milk");
			
			rs = prepareStatement.executeQuery();
			
			loopResultSet(rs);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loopResultSet(ResultSet rs) throws SQLException {
		while(rs.next()) {
			System.out.print("id : " + rs.getInt("id"));
			System.out.print(", name : " + rs.getString("name"));
			System.out.println(", price : " + rs.getDouble("price"));			
		}
	}
}
