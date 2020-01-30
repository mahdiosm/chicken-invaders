package GameFrames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import Commons.Common;
import MainFrame.GameMode;
import MainFrame.MainFrame;
import MainFrame.MainMenu;
import MainFrame.Panel;
import Users.User;
import chickensGroups.Group;
import levels.Level;
import listeners.EscKey;
import network.MessageManager;
import network.ServerPlayer;

public class EscPanel extends Panel implements MouseListener {
	private static final long serialVersionUID = 1L;
	Image esc, buttons;
	ImageIcon buttonsIcon;
	JButton Continue, quitToMainMenu, addProperty;
	JLabel ContinueText, quitToMainMenuText, addPropertyText;
	int bh = 100, bw = 300;

	public EscPanel() {
		esc = Common.createImage(MainMenu.Width, MainMenu.Height, "resources\\Images\\esc.jpg");
		buttons = Common.createImage(bw, bh, "resources\\Images\\escButton.png");
		init();
		createButtons();
		add(quitToMainMenu);
		add(Continue);
		add(addProperty);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(esc, 0, 0, null);
		paintChildren(g);
	}

	private void init() {
		Dimension dim = new Dimension(MainMenu.Width, MainMenu.Height);
		addKeyListener(new EscKey());
		setSize(dim);
		setPreferredSize(dim);
		setLayout(null);
		setLocation((int) (MainMenu.Width - dim.getWidth()) / 2, (int) (MainMenu.Height - dim.getHeight()) / 2);
	}

	private void createButtons() {
		Continue = createButton((MainMenu.Width - bw) / 2 - getX(), (MainMenu.Height - 300) / 2 - getY());
		Continue.addMouseListener(this);
		ContinueText = createLabel("CONTINUE");
		Continue.add(ContinueText);

		quitToMainMenu = createButton((MainMenu.Width - bw) / 2 - getX(), (MainMenu.Height + 100) / 2 - getY());
		quitToMainMenu.addMouseListener(this);
		quitToMainMenuText = createLabel("MAIN MENU");
		quitToMainMenu.add(quitToMainMenuText);

		addProperty = createButton((MainMenu.Width - bw) / 2 - getX(), (MainMenu.Height + 500) / 2 - getY());
		addProperty.addMouseListener(this);
		addPropertyText = createLabel("ADD PROPERTY");
		addProperty.add(addPropertyText);
	}

	private JLabel createLabel(String string) {
		JLabel l = new JLabel(string);
		l.setAlignmentX(CENTER_ALIGNMENT);
		l.setBounds(0, 0, bw, bh);
		l.setFont(new Font("serif", 0, 35));
		l.setForeground(Color.BLACK);
		return l;
	}

	private JButton createButton(int i, int j) {
		JButton button = new JButton();
		button.setIcon(new ImageIcon(buttons));
		button.setBackground(new Color(255, 0, 0, 80));
		button.setBounds(i, j, bw, bh);
		return button;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == quitToMainMenu) {
			saveUser();
		} else if (e.getSource() == Continue) {
			continueGame();
		} else if (e.getSource() == addProperty) {
			addProperty();
		}
	}

	private void addProperty() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".class files", "class");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			URL url = null;
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			String pack = JOptionPane.showInputDialog(this, "Input package of your class");
			if (pack == null)
				return;
			try {
				URLConnection connection = url.openConnection();
				InputStream inputStream = connection.getInputStream();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				int data = inputStream.read();
				while (data != -1) {
					byteArrayOutputStream.write(data);
					data = inputStream.read();
				}
				inputStream.close();
				byte[] ba = byteArrayOutputStream.toByteArray();
				class CLS extends ClassLoader {
					Class<?> load() {
						return defineClass(pack + "." + file.getName().substring(0, file.getName().length() - 6), ba, 0,
								ba.length);
					}
				}
				Class<?> clazz = new CLS().load();
				Class<? extends Group> newClass = clazz.asSubclass(Group.class);
				if (clazz.getSuperclass().getSimpleName().equals("GroupOfBoss"))
					Level.bossGroups.add(newClass);
				else
					Level.chickenGroups.add(newClass);
			} catch (IOException e) {
				MainFrame.showMessage("Sorry, but while reading file a problem accrued.", "READING ISSUE",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void continueGame() {
		if (GameMode.type != GameMode.SINGLE && GameMode.type != GameMode.SEENER) {
			ContinueText.setText("READY");
			repaint();
			GamePanel.player.sendMessage(MessageManager.READY);
		}
		if (GameMode.type == GameMode.SINGLE || (GameMode.type == GameMode.SEENER && !GamePanel.spaceShip.paused)) {
			((EscKey) getKeyListeners()[0]).changeBoolean();
			getLastPanel().remove(this);
			if (GameMode.type == GameMode.SINGLE) {
				getLastPanel().addMouseMotionListener((GamePanel) getLastPanel());
				getLastPanel().addMouseListener((GamePanel) getLastPanel());
				GamePanel.setMousePosition(GamePanel.spaceShip);
			}
			getLastPanel().remove(this);
			((EscKey) getKeyListeners()[0]).setEsc(null);
			getLastPanel().setFocusable(true);
			getLastPanel().requestFocusInWindow();
			((GamePanel) getLastPanel()).resume();
			if (GameMode.type == GameMode.SINGLE) {
				GamePanel.spaceShip.setLasttimeMilliSecond(System.currentTimeMillis());
				MainMenu.user.setBeginingOfGame(System.currentTimeMillis());
			}
		}
	}

	public void saveUser() {
		if (GameMode.type == GameMode.SINGLE) {
			String name = MainMenu.user.getName();
			int score = Integer.valueOf(((GamePanel) getLastPanel()).heatAndScore.score.getText());
			int heart = Integer.valueOf(((GamePanel) getLastPanel()).info.heartC.getText());
			int power = Integer.valueOf(((GamePanel) getLastPanel()).info.powerC.getText());
			int coin = Integer.valueOf(((GamePanel) getLastPanel()).info.coinC.getText());
			int rocket = Integer.valueOf(((GamePanel) getLastPanel()).info.rocketC.getText());
			int level = ((GamePanel) getLastPanel()).getLevel().getCode();
			long timePassed = MainMenu.user.getTimePassed() + System.currentTimeMillis()
					- MainMenu.user.getBeginingOfGame();
			int weaponType = GamePanel.spaceShip.getWeaponType();
			MainMenu.updateUser(new User(name, score, power, heart, coin, rocket, level, timePassed, weaponType, true));
		}
		if (GameMode.type != GameMode.SINGLE) {
			GamePanel.player.closeSocket();
			if (GameMode.type == GameMode.SERVER) {
				try {
					((ServerPlayer) GamePanel.player).serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		MainFrame.ChangeCurrentPanelWith(new MainMenu());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
