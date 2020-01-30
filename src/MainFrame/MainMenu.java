package MainFrame;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Commons.Common;
import Commons.IO;
import GameFrames.GamePanel;
import Users.User;

public class MainMenu extends Panel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static User user;
	public static int userIndex;
	public static final int Width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int Height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static JFrame frame;
	public static Image background, helpImage, logo;
	ImageIcon buttonsIcon;

	JButton newGame, loadGame, highScores, quit, options;
	JLabel newGameText, loadGameText, highScoresText, quitText, optionsText;

	JButton helloUser;
	JLabel hello, user1;

	public MainMenu() {
		init();
	}

	private void init() {
		GameMode.type = GameMode.SINGLE;
		createBackGround();
		createHelloUser();
		createButtonsBackGround();
		createButtons();
		addButtons();
	}

	private void addButtons() {
		add(helloUser);
		add(newGame);
		add(loadGame);
		add(options);
		add(highScores);
		add(quit);
	}

	int logoW = 800, logoH = Height / 2 + 30;

	private void createBackGround() {
		background = Common.createImage(Width, Height, "resources\\Images\\mainBack.jpg");
		logo = Common.createImage(logoW, logoH, "resources\\Images\\logo.png");
		Dimension dim = new Dimension(background.getWidth(null), background.getHeight(null));
		setSize(dim);
		setPreferredSize(dim);
		setLayout(null);
	}

	private void createHelloUser() {
		helloUser = Common.createButton(Width / 2 - bw / 2, Height / 2 - Height / 8, bw, bh, null);
		helloUser.addMouseListener(this);
		hello = Common.createLabel(helloUser.getWidth(), helloUser.getHeight(), "Hello " + user.getName());
		hello.setForeground(Color.black);
		helloUser.add(hello);
	}

	int bw = 420, bh = 100;

	private void createButtonsBackGround() {
		helpImage = Common.createImage(bw, bh, "resources\\Images\\mainButtons.png");
		buttonsIcon = new ImageIcon(helpImage);
		repaint();
	}

	private void createButtons() {
		newGame = Common.createButton(Width / 2 - bw / 2, Height / 2, bw, bh, buttonsIcon);
		newGame.addMouseListener(this);
		newGameText = Common.createLabel(bw, bh, "NEW GAME");
		newGame.add(newGameText);

		loadGame = Common.createButton(Width / 2 - bw / 2, Height / 2 + Height / 8, bw, bh, buttonsIcon);
		loadGame.addMouseListener(this);
		loadGameText = Common.createLabel(bw, bh, "LOAD GAME");
		loadGame.add(loadGameText);

		highScores = Common.createButton(Width / 2 - bw / 2, Height / 2 + 2 * Height / 8, bw, bh, buttonsIcon);
		highScores.addMouseListener(this);
		highScoresText = Common.createLabel(bw, bh, "HIGH SCORES");
		highScores.add(highScoresText);

		options = Common.createButton(Width / 2 - bw / 2, Height / 2 + 3 * Height / 8, bw, bh, buttonsIcon);
		options.addMouseListener(this);
		optionsText = Common.createLabel(bw, bh, "OPTIONS");
		options.add(optionsText);

		quit = Common.createButton(10, Height / 2 + 3 * Height / 8, bw, bh, buttonsIcon);
		quit.addMouseListener(this);
		quitText = Common.createLabel(bw, bh, "QUIT");
		quit.add(quitText);
		repaint();

	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
		g.drawImage(logo, (Width - logoW) / 2, 0, null);
		paintChildren(g);
	}

	JLabel getLabel(JButton b) {
		if (b == newGame)
			return newGameText;
		else if (b == loadGame)
			return loadGameText;
		else if (b == highScores)
			return highScoresText;
		else if (b == options)
			return optionsText;
		else if (b == helloUser)
			return hello;
		else
			return quitText;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == quit) {
			System.exit(0);
//			MainFrame.mainFrame.dispose();
		} else if (e.getSource() == newGame) {
			updateUser(new User(user.getName()));
//			GamePanel game = new GamePanel();
			ChooseModePanel chooseMode = new ChooseModePanel();
			MainFrame.ChangeCurrentPanelWith(chooseMode);
			chooseMode.requestFocusInWindow();
//			game.requestFocusInWindow();
//			game.run();
		} else if (e.getSource() == loadGame) {
			user.setBeginingOfGame(System.currentTimeMillis());
			GamePanel game = new GamePanel();
			MainFrame.ChangeCurrentPanelWith(game);
			game.requestFocusInWindow();
			game.run();
		} else if (e.getSource() == helloUser) {
			UserPanel userPanel = new UserPanel();
			MainFrame.ChangeCurrentPanelWith(userPanel);
		} else if (e.getSource() == highScores) {
			HighScorePanel highScorePanel = new HighScorePanel(HighScorePanel.NORMAL);
			highScorePanel.setLastPanel(this);
			MainFrame.ChangeCurrentPanelWith(highScorePanel);
		}
	}

	public static void updateUser(User u) {
		user = u;
		ArrayList<User> users = IO.readUsersData();
		users.remove(MainMenu.userIndex);
		users.add(MainMenu.userIndex, user);
		IO.saveUsersData(users);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		changeFont(getLabel((JButton) e.getSource()), Font.BOLD | Font.ITALIC);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		changeFont(getLabel((JButton) e.getSource()), 0);
	}

	private void changeFont(JLabel l, int style) {
		l.setFont(new Font("serif", style, 50));
	}
}
