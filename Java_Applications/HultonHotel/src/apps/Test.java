package apps;

import java.sql.*;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException {
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C://sqlite//HultonHotel.db");
			Statement statement = connection.createStatement();
			Scanner input = new Scanner(System.in);
			String option = "";
			String res = "";
			String hotelResults = "";
			while (true) {
				System.out.println("Connection established, Welcome to Hulton Hotel! Please select an option:");
				System.out.println();
				System.out.println("Press 1 to register as a new customer");
				System.out.println();
				System.out.println("Press 2 to search hotels");
				option = input.next();
				if (option.equals("1")) {
					res = newCustomer(connection, input);
					System.out.println(res);
					break;
				}

				else if (option.equals("2")) {
					hotelResults = viewHotels(connection, input);
				}

				// ResultSet res = statement.executeQuery("SELECT * FROM
				// customer");

				// while(res.next()){
				// System.out.println("Name: " + res.getString("Name"));
				// }
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) { // Use SQLException class instead.
				System.err.println(e);
			}
		}
	}

	public static String newCustomer(Connection conn, Scanner input) throws SQLException {
		// get all registration info from customer
		String name, address, phone, email = "";
		input.nextLine();
		System.out.println("Please enter your name:");
		name = input.nextLine();
		System.out.println("Please enter your address:");
		address = input.nextLine();
		System.out.println("Please enter your phone number:");
		phone = input.nextLine();
		System.out.println("Please enter your email address:");
		email = input.nextLine();
		input.close();

		System.out.println("Name:" + name);
		System.out.println("Address:" + address);
		System.out.println("Phone:" + phone);
		System.out.println("Email:" + email);
		// check last customer ID to create next primary customer ID
		Statement statement = conn.createStatement();
		ResultSet res = statement.executeQuery("SELECT max(CID) from CUSTOMER");
		int CID = res.getInt(1);
		// increment CID to create new CID
		CID++;
		String create = "(" + CID + ",'" + name + "','" + address + "','" + phone + "','" + email + "');";
		statement.executeUpdate("INSERT into CUSTOMER values" + create + ";");
		return "Customer successfuly created, your customer ID is: " + CID;
	}

	public static String viewHotels(Connection conn, Scanner input) throws SQLException {
		String country, state = "";
		input.nextLine();
		ResultSet res = null;
		System.out.println("Please enter the country to search for a Hotel");
		country = input.nextLine();
		System.out.println("Please enter the state in " + country + " to search for a Hotel");
		state = input.nextLine();

		Statement statement = conn.createStatement();
		res = statement.executeQuery("SELECT * from HOTEL where Country ='" + country + "' AND State ='" + state + "';");
		if (!res.isBeforeFirst()) {
			return "Oops! There are no hotels present in " + state + " , " + country + "!";
		} else {
			System.out.println("Hotels found in " + state + " , " + country + "! Below are the results:");
			System.out.println();
			while (res.next()) {
				System.out.print("Hotel:");
				for (int i = 1; i < 6; i++) {
					System.out.print(" " +res.getString(i));
				}
				System.out.println();
			}
			System.out.println();
			return "These are the available Hotels in " + state + ", " + country +".";
		}

	}

}
