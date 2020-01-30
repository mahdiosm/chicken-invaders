package MainFrame;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.Timer;

import GameFrames.GamePanel;
import GameFrames.SpaceShip;
import listeners.MyMouseListener;
import network.ClientPlayer;
import network.InformationForWaitPanel;
import network.MessageManager;
import network.PlayersName;
import network.ServerPlayer;

public class WaitPanel extends Panel implements MyMouseListener {
	private static final long serialVersionUID = 1L;
	// only for server
	JButton enter;
	JButton back;
	int type;
	public ArrayList<InformationForWaitPanel> infoes = new ArrayList<>();

	public WaitPanel() {
		type = GameMode.type;
		setBounds(0, 0, MainMenu.Width, MainMenu.Height);
		setLayout(null);
		createButtons();
		requestFocusInWindow();
	}

	private void createButtons() {
		if (type == GameMode.SERVER) {
			enter = createButton("ENTER", 1200, MainMenu.Height - 130, 150, 100);
			add(enter);
		}
		back = createButton("BACK", 100, MainMenu.Height - 130, 150, 100);
		add(back);
		revalidate();
		repaint();
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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(MainMenu.background, 0, 0, null);
		g.setFont(new Font("serif", 0, 40));
		g.setColor(Color.PINK);
		int level = 0, wave = 0;
		if (GameMode.type != GameMode.SERVER) {
			level = ((ClientPlayer) GamePanel.player).level;
			wave = ((ClientPlayer) GamePanel.player).wave;
		}
		g.drawString("Level: " + level + "      " + "Wave: " + wave, 100, 50);
		g.drawString("SCORE", 700, 50);
		g.drawString("HEART", 900, 50);
		drawNames(g);
		paintChildren(g);
	}

	private void drawNames(Graphics g) {
		getList();
		int x = 100, y = 100;
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 40));
		int index = 1;
		for (int i = 0; i < infoes.size(); i++) {
			if (GamePanel.spaceShip.getId() == infoes.get(i).getID())
				g.setColor(ServerPlayer.colors.get(infoes.get(i).getID()));
			if (infoes.get(i).isSeener())
				g.drawString((i + 1) + "- " + "Seener" + index++, x, y);
			else {
				g.drawString((i + 1) + "- " + PlayersName.values()[infoes.get(i).getID()], x, y);
				x = 700;
				g.drawString((infoes.get(i).getScore() + ""), x, y);
				x = 900;
				g.drawString((infoes.get(i).getHeart() + ""), x, y);
			}
			x = 100;
			y += 50;
			g.setColor(Color.WHITE);
		}
	}

	private void getList() {
		if (type == GameMode.SERVER) {
			infoes = new ArrayList<>();
			infoes.add(new InformationForWaitPanel(GamePanel.spaceShip.getId(), GamePanel.spaceShip.getHeart(),
					GamePanel.spaceShip.getScore(), false));
			for (Iterator<SpaceShip> iterator = ((ServerPlayer) GamePanel.player).otherPlayers.values()
					.iterator(); iterator.hasNext();) {
				SpaceShip spaceShip = iterator.next();
				infoes.add(new InformationForWaitPanel(spaceShip.getId(), spaceShip.getHeart(), spaceShip.getScore(),
						spaceShip.isSeener()));
			}
		} else {
			infoes = ((ClientPlayer) GamePanel.player).infoes;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == enter) {
			GamePanel game = new GamePanel();
			MainFrame.ChangeCurrentPanelWith(game);
			game.requestFocusInWindow();
			game.run();
			GamePanel.player.sendMessage(MessageManager.ENTER);
			ServerPlayer.state = ServerPlayer.GAME;
			ServerPlayer.inGame = true;
		}
	}
}
