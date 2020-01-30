import java.sql.*;

public class Test {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "mahdi1379@osm");
			// here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			stmt.executeUpdate("CREATE IF NOT EXISTS DATABASE Chick");
//			ResultSet rs = stmt.executeQuery("CREATE DATABASE TEST");
//			while (rs.next())
//				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}