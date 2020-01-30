package listeners;

import java.awt.event.KeyEvent;
import GameFrames.EscPanel;
import GameFrames.GamePanel;
import MainFrame.GameMode;
import MainFrame.MainMenu;
import Users.User;
import network.MessageManager;

public class EscKey implements MyKeyListener {
	private static EscPanel esc;
	private static GamePanel game;
	public static boolean f = true;

	public void changeBoolean() {
		f = !f;
	}

	public EscPanel getEsc() {
		return esc;
	}

	public void setEsc(EscPanel esc) {
		EscKey.esc = esc;
	}

	public GamePanel getGame() {
		return game;
	}

	public void setGame(GamePanel game) {
		EscKey.game = game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && f == true) {
			if (GameMode.type != GameMode.SINGLE && GameMode.type != GameMode.SEENER) {
				GamePanel.player.sendMessage(MessageManager.PAUSE);
			}
			pause();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && f == false) {
			if (GameMode.type != GameMode.SINGLE && GameMode.type != GameMode.SEENER) {
				GamePanel.player.sendMessage(MessageManager.READY);
			}
			ready();
		}
	}

	public void pause() {
		User user = MainMenu.user;
		user.setTimePassed(user.getTimePassed() + System.currentTimeMillis() - user.getBeginingOfGame());
		game.removeMouseMotionListener(game);
		game.removeMouseListener(game);
		esc = new EscPanel();
		setEsc(esc);
		esc.setLastPanel(game);
		game.add(esc);
		game.repaint();
		game.stop();
		f = false;
	}

	public void ready() {
		MainMenu.user.setBeginingOfGame(System.currentTimeMillis());
		if (GameMode.type != GameMode.SEENER) {
			game.addMouseMotionListener(game);
			game.addMouseListener(game);
			GamePanel.setMousePosition(GamePanel.spaceShip);
		}
		game.remove(esc);
		setEsc(null);
		game.setFocusable(true);
		game.requestFocusInWindow();
		GamePanel.spaceShip.setLasttimeMilliSecond(System.currentTimeMillis());
		game.run();
		f = true;
	}

}
