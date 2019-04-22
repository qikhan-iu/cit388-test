package edu.iupui.cit388.db;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class InMemoryDBApp {

	public static void main(String[] args) throws Exception {

		String databaseConnectionUrl = "jdbc:derby:memory:demo;create=true";
		System.out.println("Initiating DB Connection ... ");
		try (Connection connection = DriverManager.getConnection(databaseConnectionUrl)) {
			System.out.println("Connection established. ");

			Statement sql = connection.createStatement();

			System.out.println("Create items table");
			sql.executeUpdate("Create table items" + " (" + " id int primary key," + " name varchar(30),"
					+ " price decimal(5,2)" + ")");

			System.out.println("Insert to items table");

			try (Scanner input = new Scanner(Paths.get("./resource/Item.txt"))) {

				int count = 1;
				while (input.hasNext()) {
					String name = input.next();
					double price = input.nextDouble();
					sql.executeUpdate(String.format("insert into items values ( %d, '%s', %.2f )", count, name, price));
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("Read from items table");
			ResultSet rs = sql.executeQuery("Select * from items");
			while(rs.next()) {
				System.out.print("id : " + rs.getInt("id"));
				System.out.print(", name : " + rs.getString("name"));
				System.out.println(", price : " + rs.getDouble("price"));			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
