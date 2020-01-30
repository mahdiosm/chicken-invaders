package MainFrame;

import java.awt.Color;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import GameFrames.GamePanel;
import listeners.MyMouseListener;
import network.CantEnterException;
import network.ClientPlayer;
import network.ServerPlayer;

public class ChooseModePanel extends Panel implements MyMouseListener, TextListener {
	private static final long serialVersionUID = 1L;
	private int type;
	private static final int HOME = 0, MULTI_SINGLE = 1, SERVER_CLIENT = 2, SERVER = 3, CLIENT = 4;
	List<String> IP_const = Arrays.asList("127", "0", "0", "1");
	Map<TextField, String> constValues = new HashMap<>();
	ArrayList<TextField> IP_getters = new ArrayList<>();
	TextField portClient, portServer, maxPlayers, maxLevels;
	JLabel entermaxPlayers, entermaxLevels, enterPortClient, enterPortServer, enterIp;
	JButton multiPalyer, singlePlayer, server, client, seener;
	JButton enter, back, reset;
	Pattern pattern = Pattern.compile("\\d*");

	public ChooseModePanel() {
		setLastPanel(new MainMenu());
		setBounds(0, 0, MainMenu.Width, MainMenu.Height);
		setLayout(null);
		createButtons();
		createTextFields();
		createLabels();
		setType(MULTI_SINGLE);
	}

	private void setType(int type) {
		this.type = type;
		removeAll();
		add(back);
		switch (type) {
		case HOME:
			MainFrame.ChangeCurrentPanelWith(new MainMenu());
			break;
		case MULTI_SINGLE:
			add(multiPalyer);
			add(singlePlayer);
			break;
		case SERVER_CLIENT:
			add(server);
			add(client);
			break;
		case SERVER:
			createServerPage();
			break;
		case CLIENT:
			craeteClientPage();
			break;
		}
		revalidate();
		repaint();
	}

	private void craeteClientPage() {
		addArray();
		add(enter);
		add(seener);
		add(reset);
		add(enterIp);
		add(portClient);
		add(enterPortClient);
	}

	private void createServerPage() {
		add(enter);
		add(reset);
		add(portServer);
		add(enterPortServer);
		add(maxPlayers);
		add(entermaxPlayers);
		add(maxLevels);
		add(entermaxLevels);
	}

	@Override
	public Component add(Component comp) {
		if (comp instanceof TextField) {
			((TextField) comp).setText(constValues.get((TextField) comp));
		}
		return super.add(comp);
	}

	private void addArray() {
		for (int i = 0; i < 4; i++) {
			add(IP_getters.get(i));
		}
	}

	private JButton createButton(String name, int i, int j, int w, int h) {
		JButton button = new JButton();
		button.setForeground(Color.black);
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

	private void createTextFields() {
		int x = MainMenu.Width / 2, y = MainMenu.Height / 2, h = 80;
		for (int i = 0; i < 4; i++) {
			TextField textField = createTextField(x - 82 * (2 - i), y - 2 * h, 80, h);
			IP_getters.add(textField);
			constValues.put(textField, IP_const.get(i));
		}
		portServer = createTextField(x - 164, y - 2 * h, 328, h);
		portClient = createTextField(x - 164, y, 328, h);
		constValues.put(portClient, "8080");
		constValues.put(portServer, "8080");
		maxPlayers = createTextField(x - 164, y, 328, h);
		constValues.put(maxPlayers, "4");
		maxLevels = createTextField(x - 164, y + 2 * h, 328, h);
		constValues.put(maxLevels, "4");
	}

	private TextField createTextField(int i, int j, int w, int h) {
		TextField textField = new TextField();
		textField.setForeground(Color.black);
		textField.addTextListener(this);
		textField.setBounds(i, j, w, h);
		textField.setFont(new Font("serif", 0, 50));
		return textField;
	}

	private void createLabels() {
		int x = MainMenu.Width / 2, y = MainMenu.Height / 2, h = 80;
		enterIp = createLabel("ENTER IP:", x - 160, y - 3 * h, 320, h);
		enterPortClient = createLabel("ENTER PORT:", x - 160, y - h, 320, h);
		enterPortServer = createLabel("ENTER PORT:", x - 160, y - 3 * h, 320, h);
		entermaxPlayers = createLabel("Enter maximum players can join:", x - 160, y - h, 600, h);
		entermaxLevels = createLabel("Enter number of levels you want to play:", x - 160, y + h, 600, h);
	}

	private JLabel createLabel(String text, int i, int j, int w, int h) {
		JLabel label = new JLabel(text);
		label.setForeground(Color.white);
		label.setFont(new Font("serif", 0, 30));
		label.setBounds(i, j, w, h);
		return label;
	}

	private void createButtons() {
		int w = 400, h = 100, x = MainMenu.Width / 2 - w / 2, y = MainMenu.Height / 2 - h;
		singlePlayer = createButton("SINGLE PLAYER", x, y, w, h);
		multiPalyer = createButton("MULTI PLAYER", x, y + h + 20, w, h);
		server = createButton("CREATE SERVER", x, y, w, h);
		client = createButton("FIND SERVER", x, y + h + 20, w, h);
		back = createButton("BACK", 100, MainMenu.Height - 130, 150, 100);
		enter = createButton("ENTER", 1200, MainMenu.Height - 130, 150, 100);
		seener = createButton("ENTER AS SEENER", 1000, MainMenu.Height - 130, 150, 100);
		seener.setFont(new Font("serif", 0, 16));
		reset = createButton("SET AS DEFAULT", 1200, MainMenu.Height - 240, 150, 100);
		reset.setFont(new Font("serif", 0, 18));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(MainMenu.background, 0, 0, null);
		paintChildren(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		check(e);
	}

	private void check(MouseEvent e) {
		if (e.getSource() == singlePlayer) {
			GameMode.type = GameMode.SINGLE;
			GamePanel game = new GamePanel();
			MainFrame.ChangeCurrentPanelWith(game);
			game.requestFocusInWindow();
			game.run();
		} else if (e.getSource() == multiPalyer) {
			setType(SERVER_CLIENT);
		} else if (e.getSource() == server) {
			setType(SERVER);
		} else if (e.getSource() == client) {
			setType(CLIENT);
		} else if (e.getSource() == back) {
			goBack();
		} else if (e.getSource() == enter) {
			enter();
		} else if (e.getSource() == reset) {
			setDefault();
		} else if (e.getSource() == seener) {
			enterAsSeener();
		}
	}

	private void setDefault() {
		for (int i = 0; i < 4; i++) {
			IP_getters.get(i).setText(IP_const.get(i));
		}
		portClient.setText("8080");
		portServer.setText("8080");
		maxLevels.setText("4");
		maxPlayers.setText("4");
	}

	private void goBack() {
		if (type == SERVER || type == CLIENT)
			setType(SERVER_CLIENT);
		else
			setType(type - 1);
	}

	private void enter() {
		ServerPlayer.state = ServerPlayer.WAIT;
		if (type == SERVER) {
			GameMode.type = GameMode.SERVER;
			try {
				GamePanel.player = new ServerPlayer(Integer.valueOf(portServer.getText()),
						Integer.valueOf(maxPlayers.getText()), Integer.valueOf(maxLevels.getText()));
				WaitPanel waitPanel = new WaitPanel();
				MainFrame.ChangeCurrentPanelWith(waitPanel);
				((ServerPlayer) GamePanel.player).waitPanel = waitPanel;
				waitPanel.repaint();
			} catch (IOException e) {
				MainFrame.showMessage("Sorry but maybe there is another Server with this port and IP", "CAN'T CREATE",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (type == CLIENT) {
			GameMode.type = GameMode.CLIENT;
			enterAsClient();
		}
	}

	private void enterAsClient() {
		String ip = IP_getters.get(0).getText() + "." + IP_getters.get(1).getText() + "." + IP_getters.get(2).getText()
				+ "." + IP_getters.get(3).getText();
		try {
			GamePanel.player = new ClientPlayer(ip, Integer.valueOf(portClient.getText()));
		} catch (UnknownHostException e) {
			MainFrame.showMessage("There is no server with this ip", "ERROR", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			MainFrame.showMessage("An error while connecting accrued.", "ERROR", JOptionPane.ERROR_MESSAGE);
		} catch (CantEnterException e) {
			MainFrame.showMessage("you cannot enter because that another player with your name exists.", "CAN'T ENTER",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void enterAsSeener() {
		GameMode.type = GameMode.SEENER;
		enterAsClient();
	}

	@Override
	public void textValueChanged(TextEvent e) {
		TextField textField = (TextField) e.getSource();
		String text = textField.getText().replaceAll("[^0-9]", "");
		textField.setText(text);
		textField.setCaretPosition(text.length());
		manageServerClient(textField);
	}

	private void manageServerClient(TextField textField) {
		if (type == SERVER || type == CLIENT) {
			try {
				int number = Integer.parseInt(textField.getText());
				if (IP_getters.contains(textField) && number >= 255) {
					textField.setText(String.valueOf(number / 10));
				} else if (textField == maxPlayers && number > 10) {
					textField.setText(number >= 10 ? String.valueOf(number / 10) : "");
				} else if (textField == maxLevels && number > 4) {
					textField.setText(number >= 10 ? String.valueOf(number / 10) : "");
				} else if ((textField == portClient || textField == portServer) && number >= 1000000) {
					textField.setText(String.valueOf(number / 10));
				}
			} catch (NumberFormatException e) {
			}
		}
		checkForEnterButton();
	}

	private void checkForEnterButton() {
		boolean f = true;
		if (type == SERVER) {
			f = f && !isWhiteSpaceOrNull(maxPlayers.getText()) && !isWhiteSpaceOrNull(maxLevels.getText())
					&& !isWhiteSpaceOrNull(portServer.getText());
		} else if (type == CLIENT) {
			for (TextField textField : IP_getters) {
				f = f && !isWhiteSpaceOrNull(textField.getText());
			}
			f = f && !isWhiteSpaceOrNull(portClient.getText());
		}
		enter.setEnabled(f);
		seener.setEnabled(f);
	}

	public static boolean isWhiteSpaceOrNull(String string) {
		return string == null || string.equals("");
	}
}
