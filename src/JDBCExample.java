import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

class CoffeeUser {
  public String username;
  public String password;
  public String email;
  public int phone;
  public int mid;
}

class CoffeeOrder {
	public String username;
	public java.sql.Date date;
	public int mode;
}

public class JDBCExample {
	static LinkedList<CoffeeUser> listOfCoffeeUsers = new LinkedList<CoffeeUser>();
	static LinkedList<CoffeeOrder> listOfCoffeeOrders = new LinkedList<CoffeeOrder>();

	public static void main(String[] argv) {

		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");

		Connection connection = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://pdc-amd01.poly.edu/", "sh3244",
					"jzkzpftk");

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		// get the data
		getCoffeeUsers(connection);
		getOrders(connection);

		// print the results
		printEverything();
	}
	
	private static void printEverything()
	{
		System.out.println("\nCoffee Users");

		Iterator<CoffeeUser> it = listOfCoffeeUsers.iterator();
		while (it.hasNext())
		{
			CoffeeUser CoffeeUser = (CoffeeUser)it.next();
			System.out.println(CoffeeUser.username + " " + CoffeeUser.password + " " + CoffeeUser.email + " " + CoffeeUser.phone + " " + CoffeeUser.mid);
		}
		
		System.out.println("\nCoffee Orders");
		
		Iterator<CoffeeOrder> it2 = listOfCoffeeOrders.iterator();
		while (it2.hasNext())
		{
			CoffeeOrder order = (CoffeeOrder)it2.next();
			System.out.println(order.username + " " + order.date.toString() + " " + order.mode);
		}
	}

	private static void getCoffeeUsers(Connection conn)
	{
		try 
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT username, password, email, phone, mid FROM cafeuser ORDER BY mid");
			while (rs.next())
			{
				CoffeeUser user = new CoffeeUser();
				user.username   = rs.getString ("username");
				user.password = rs.getString("password");
				user.email = rs.getString("email");
				user.phone = rs.getInt("phone");
				user.mid = rs.getInt("mid");
				listOfCoffeeUsers.add(user);
			}
			
			rs.close();
			st.close();
		}
		catch (SQLException se) {
			System.err.println("Threw a SQLException creating the list of CoffeeUsers.");
			System.err.println(se.getMessage());
		}
	}
	
	private static void getOrders(Connection conn)
	{
		try 
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT username, ordertime, mode FROM orders ORDER BY username");
			while (rs.next())
			{
				CoffeeOrder order = new CoffeeOrder();
				order.username = rs.getString("username");
				order.date = rs.getDate("ordertime");
				order.mode = rs.getInt("mode");
				listOfCoffeeOrders.add(order);
			}
			
			rs.close();
			st.close();
		}
		catch (SQLException se) {
			System.err.println("Threw a SQLException creating the list of CoffeeOrders.");
			System.err.println(se.getMessage());
		}
	}
}