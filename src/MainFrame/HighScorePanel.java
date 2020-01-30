package MainFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JButton;

import Commons.IO;
import GameFrames.GamePanel;
import Users.User;
import listeners.MyMouseListener;
import network.MessageManager;
import network.ServerPlayer;

public class HighScorePanel extends Panel implements MyMouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Vector<User> users = new Vector<>();
	private JButton back, reset, enterAsSeener;

	// 0 for nothing, 1 for winner and 2 for looser
	public static int WINNER_MODE = 1, LOOSER_MODE = 2, NORMAL = 0;
	private int type = 0;

	public HighScorePanel(int type) {
		int gameModeType = GameMode.type;
		setLastPanel(new MainMenu());
		GameMode.type = gameModeType;
		this.type = type;
		init();
		revalidate();
		repaint();
	}

	private void init() {
		setBounds(0, 0, MainMenu.Width, MainMenu.Height);
		setLayout(null);
		if (GameMode.type == GameMode.SINGLE)
			readHighScores();
		createButtons();
	}

	private void createButtons() {
		back = createButton("BACK", 100, MainMenu.Height - 200, 150, 100);
		reset = createButton("RESET", 1200, MainMenu.Height - 200, 150, 100);
		enterAsSeener = createButton("ENTER AS SEENER", 1000, MainMenu.Height - 200, 450, 100);
		add(back);
		if (GameMode.type == GameMode.SINGLE)
			add(reset);
		else if (type == LOOSER_MODE) {
			add(enterAsSeener);
		}
	}

	private JButton createButton(String name, int i, int j, int w, int h) {
		JButton button = new JButton();
		button.setBackground(new Color(220, 150, 0));
		button.setBounds(i, j, w, h);
		button.setFont(new Font("serif", 0, 40));
		button.setText(name);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorderPainted(false);
		button.setIconTextGap(0);
		button.setBorder(null);
		button.addMouseListener(this);
		return button;
	}

	private void readHighScores() {
		users = new Vector<>();
		try {
			IO.resultSet = IO.statement.executeQuery("SELECT * FROM HIGHSCORES");
			while (IO.resultSet.next()) {
				String name = (String) IO.resultSet.getString("name");
				int score = IO.resultSet.getInt("score");
				int power = IO.resultSet.getInt("power");
				int rocket = IO.resultSet.getInt("rocket");
				int level = IO.resultSet.getInt("level");
				int heart = IO.resultSet.getInt("heart");
				int food = IO.resultSet.getInt("food");
				long timePassed = IO.resultSet.getLong("timePassed");
				int weaponType = IO.resultSet.getInt("weaponType");
				boolean canContinue = IO.resultSet.getBoolean("canContinue");
				users.add(
						new User(name, score, power, heart, food, rocket, level, timePassed, weaponType, canContinue));
			}
		} catch (SQLException e) {
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(MainMenu.background, 0, 0, null);
		if (GameMode.type == GameMode.SINGLE) {
			drawUsers(g);
		}
		drawType(g);
	}

	private void drawType(Graphics g) {
		g.setFont(new Font("serif", Font.BOLD, 50));
		if (type == NORMAL) {
			g.setColor(Color.white);
			g.drawString("HIGH SCORES", MainMenu.Width / 2 - 150, 50);
		} else if (type == WINNER_MODE) {
			g.setColor(Color.GREEN);
			g.drawString("YOU WIN", MainMenu.Width / 2 - 150, 50);
		} else if (type == LOOSER_MODE) {
			g.setColor(Color.RED);
			g.drawString("GAME OVER", MainMenu.Width / 2 - 150, 50);
		}
	}

	private void drawUsers(Graphics g) {
		int x = 100, y = 100;
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.setColor(Color.WHITE);
		for (int i = 0; i < users.size(); i++) {
			g.drawString((i + 1) + "- " + users.get(i).getName(), x, y);
			y += 50;
		}
		y = 100;
		x = 1200;
		for (int i = 0; i < users.size(); i++) {
			g.drawString(users.get(i).getScore() + "", x, y);
			y += 50;
		}
	}

	public void addNewUser(User newUser) {
		users.add(newUser);
		Collections.sort(users);
		if (users.size() > 10) {
			users.remove(10);
		}
		saveNewHighScore();
	}

	private void saveNewHighScore() {
		try {
			IO.statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS HIGHSCORES(name mediumtext not null,score int not null default 0,canContinue bool not null default false,timepassed long not null,level int not null default 11,rocket int not null default 3,weaponType int not null default 1,power int not null default 0,heart int not null default 5,food int not null default 0)");
			IO.statement.executeUpdate("TRUNCATE TABLE HIGHSCORES");
			for (User user : users) {
				String pString = "INSERT into HIGHSCORES(name , score , canContinue,timePassed,level,rocket,weaponType,power , heart,food) values ("
						+ "\"" + user.getName() + "\"" + "," + user.getScore() + "," + user.CanContiue() + ","
						+ user.getTimePassed() + "," + user.getLevelsPassed() + "," + user.getRocket() + ","
						+ user.getWeaponType() + "," + user.getPower() + "," + user.getHeart() + "," + user.getCoin()
						+ ")";
				IO.statement.executeUpdate(pString);
			}
			IO.connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == back) {
			MainFrame.ChangeCurrentPanelWith(new MainMenu());
			if (GameMode.type != GameMode.SINGLE) {
				GamePanel.player.closeSocket();
				if (GameMode.type == GameMode.SERVER) {
					try {
						((ServerPlayer) GamePanel.player).serverSocket.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		} else if (e.getSource() == reset) {
			users = new Vector<>();
			saveNewHighScore();
			repaint();
		} else if (e.getSource() == enterAsSeener) {
			enterAsSeener();
		}
	}

	private void enterAsSeener() {
		GameMode.type = GameMode.SEENER;
		GamePanel.player.sendMessage(MessageManager.REQUEST_TYPE);
		GamePanel game = new GamePanel();
		MainFrame.ChangeCurrentPanelWith(game);
		game.requestFocusInWindow();
		game.run();
	}
}
