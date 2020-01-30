package Commons;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import Users.User;

public class IO {
	public static String gameData_Path = "game.data";
	public static String font_Path = "game.data";
	public static Connection connection;
	public static Statement statement;
	public static ResultSet resultSet;

	public static ArrayList<User> readUsersData() {
		ArrayList<User> users = new ArrayList<>();
		try {
			resultSet = statement.executeQuery("SELECT * FROM USERS");
			while (resultSet.next()) {
				String name = (String) resultSet.getString("name");
				int score = resultSet.getInt("score");
				int power = resultSet.getInt("power");
				int rocket = resultSet.getInt("rocket");
				int level = resultSet.getInt("level");
				int heart = resultSet.getInt("heart");
				int food = resultSet.getInt("food");
				long timePassed = resultSet.getLong("timePassed");
				int weaponType = resultSet.getInt("weaponType");
				boolean canContinue = resultSet.getBoolean("canContinue");
				users.add(
						new User(name, score, power, heart, food, rocket, level, timePassed, weaponType, canContinue));
			}
		} catch (SQLException e) {
		}
		return users;
	}

	public static void saveUsersData(ArrayList<User> users) {
		try {
			statement.executeUpdate("TRUNCATE TABLE USERS");
			for (User user : users) {
				String pString = "INSERT into USERS(name , score , canContinue,timePassed,level,rocket,weaponType,power , heart,food) values ("
						+ "\"" + user.getName() + "\"" + "," + user.getScore() + "," + user.CanContiue() + ","
						+ user.getTimePassed() + "," + user.getLevelsPassed() + "," + user.getRocket() + ","
						+ user.getWeaponType() + "," + user.getPower() + "," + user.getHeart() + "," + user.getCoin()
						+ ")";
				statement.executeUpdate(pString);
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Font readFont() {
		Font myFont = null;
		try {
			InputStream inputStream = new FileInputStream(font_Path);
			try {
				myFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			} catch (FontFormatException | IOException e) {
			}
		} catch (FileNotFoundException e) {
		}
		return myFont;
	}
}
