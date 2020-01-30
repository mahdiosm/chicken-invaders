package network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.Timer;
import GameFrames.GamePanel;
import GameFrames.SpaceShip;
import MainFrame.GameMode;
import MainFrame.HighScorePanel;
import MainFrame.MainFrame;
import MainFrame.MainMenu;
import MainFrame.WaitPanel;
import Weapons.Weapon;

public class ClientMessageManger extends MessageManager {
	ClientPlayer player;

	public ClientMessageManger(Socket socket, ClientPlayer player) {
		super(socket);
		this.player = player;
	}

	////////////////////////
	/// Received messages///
	////////////////////////
	@Override
	protected void checkRequest() throws IOException, ClassNotFoundException {
		GamePanel.spaceShip = (SpaceShip) decodeObject.readObject();
		WaitPanel waitPanel = new WaitPanel();
		MainFrame.ChangeCurrentPanelWith(waitPanel);
		((ClientPlayer) GamePanel.player).waitPanel = waitPanel;
	}

	@Override
	protected void getList() throws IOException {
		player.level = decoder.readInt();
		player.wave = decoder.readInt();
		int num = decoder.readInt();
		player.infoes = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			player.infoes.add(new InformationForWaitPanel(decoder.readInt(), decoder.readInt(), decoder.readInt(),
					decoder.readBoolean()));
		}
		player.waitPanel.repaint();
	}

	@Override
	public void enterToTheGame() throws IOException {
		float num = (decoder.readInt() - 1) / 2;
		float index = num - GamePanel.spaceShip.getId();
		GamePanel.spaceShip.setX(MainMenu.Width / 2 - GamePanel.spaceShip.getWidth() * index);
		GamePanel game = new GamePanel();
		MainFrame.ChangeCurrentPanelWith(game);
		game.requestFocusInWindow();
		game.run();
		player.game = game;
	}

	@Override
	protected void draw() throws ClassNotFoundException, IOException {
		InfoForDraw infoForDraw = (InfoForDraw) decodeObject.readObject();
		player.infoForDraw = infoForDraw;
	}

	@Override
	protected void pause() throws IOException {
		GamePanel.spaceShip.paused = true;
		GamePanel.escKey.pause();
	}

	@Override
	protected void ready() throws IOException {
		GamePanel.spaceShip.paused = false;
		GamePanel.escKey.ready();
	}

	@Override
	protected void checkType() throws IOException {
		encoder.writeByte(REQUEST_TYPE);
		encoder.writeInt(GameMode.type);
		encoder.flush();
	}

	@Override
	protected void lose() throws IOException {
		SpaceShip.goToRankingPageMultiPlayer(HighScorePanel.LOOSER_MODE);
	}

	/////////////////////
	/// send messages ///
	/////////////////////
	@Override
	protected void sendPosition() throws IOException {
		encoder.writeByte(POSITION);
		encoder.flush();
		encoder.writeInt(GamePanel.spaceShip.getId());
		encoder.flush();
		encoder.writeFloat(GamePanel.spaceShip.getX());
		encoder.flush();
		encoder.writeFloat(GamePanel.spaceShip.getY());
		encoder.flush();
	}

	@Override
	protected void sendBullets() throws IOException {
		encoder.writeByte(FIRE);
		int id = GamePanel.spaceShip.getId();
		MyPoint.Point point = GamePanel.spaceShip.getCenterPoint();
		Weapon weapon = GamePanel.spaceShip.getWeapon();
		int x = (int) (point.getX() - weapon.getWidth() / 2);
		int y = (int) (point.getY() - weapon.getHeigth());
		GamePanel.spaceShip.shooting = false;
		new Timer(weapon.timeDelay, e -> {
			GamePanel.spaceShip.shooting = true;
			((Timer) e.getSource()).stop();
		}).start();
		encoder.writeInt(id);
		encoder.writeInt(x);
		encoder.writeInt(y);
		encoder.flush();
	}

	@Override
	protected void sendTemp() throws IOException {
		encoder.writeByte(TEMP_CHANGED);
		encoder.writeInt(GamePanel.spaceShip.getId());
		encoder.writeInt((int) GamePanel.spaceShip.getTemp());
		encoder.flush();
	}

	@Override
	protected void prepareList() throws IOException {
		encoder.writeByte(MessageManager.LIST);
		encoder.flush();
	}
}