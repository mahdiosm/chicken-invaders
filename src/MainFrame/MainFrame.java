package MainFrame;

import java.awt.Cursor;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import Commons.Common;
import Commons.IO;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static JPanel contentJPanel;
	public static Image back;
	private UserPanel initPanel;
	private Font myFont;
//	private final File fontFile = new File("B Ziba_0.ttf");
	public static MainFrame mainFrame;
//	private static File highScores = new File("users.highScores");

	public static void main(String[] args) {
		mainFrame = new MainFrame();
	}

	public static void ChangeCurrentPanelWith(JPanel p) {
		MainFrame.mainFrame.getContentPane().removeAll();
		MainFrame.mainFrame.getContentPane().revalidate();
		MainFrame.mainFrame.getContentPane().add(p);
		MainFrame.mainFrame.getContentPane().repaint();
	}

	public static void showMessage(String message, String title, int type) {
		JOptionPane.showMessageDialog(MainFrame.mainFrame, message, title, type);
	}

	public MainFrame() {
		init();
	}

	private void init() {
		connectToDataBase();
		createMyCursor();
		readFont();
		createPlaySound();
		createFrame();
	}

	private void connectToDataBase() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			IO.connection = DriverManager.getConnection("jdbc:mysql:your ip", "your usename", "your pass");
			IO.connection.setAutoCommit(false);
			IO.statement = IO.connection.createStatement();
			IO.statement.executeUpdate("CREATE DATABASE IF NOT EXISTS CHICKEN_INVADERS");
			IO.statement.executeQuery("USE CHICKEN_INVADERS");
			IO.statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS USERS(name mediumtext not null,score int not null default 0,canContinue bool not null default false,timepassed long not null,level int not null default 11,rocket int not null default 3,weaponType int not null default 1,power int not null default 0,heart int not null default 5,food int not null default 0)");
		} catch (ClassNotFoundException e) {
			showMessage("DRIVER NOT FOUND", "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (SQLException e) {
			showMessage("CONNECTION FAILED\n" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private void createMyCursor() {
		Image cursor = Common.createImage(16, 16, "resources\\Images\\cursor.png");
		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "mine");
		getContentPane().setCursor(c);
	}

	private void readFont() {
		myFont = IO.readFont();
	}

	private void createPlaySound() {
	}

	private void createFrame() {
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initPanel = new UserPanel();
		getContentPane().add(initPanel);
		pack();
		setVisible(true);
	}

	public Font getFont() {
		return myFont;
	}
}
