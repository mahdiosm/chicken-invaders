package network;

import java.awt.Point;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Timer;

import GameFrames.GamePanel;
import GameFrames.SpaceShip;
import MainFrame.GameMode;
import MainFrame.MainFrame;
import MainFrame.MainMenu;
import Weapons.Weapon;
import listeners.EscKey;

public class ServerMessageManager extends MessageManager {
	ServerPlayer player;

	public ServerMessageManager(Socket socket, ServerPlayer serverPlayer) {
		super(socket);
		player = serverPlayer;
	}

	////////////////////////
	/// Received messages///
	////////////////////////

	@Override
	protected void updatePosition() throws IOException {
		int id = decoder.readInt();
		float x = decoder.readFloat();
		float y = decoder.readFloat();
		SpaceShip spaceShip = player.otherPlayers.get(id);
		spaceShip.setX(x);
		spaceShip.setY(y);
	}

	@Override
	protected void updateBullets() throws IOException {
		int id = decoder.readInt();
		int x = decoder.readInt();
		int y = decoder.readInt();
		SpaceShip spaceShip = player.otherPlayers.get(id);
		Weapon weapon = spaceShip.getWeapon();
		weapon.setBeginPoint(new Point(x, y));
		weapon.setId(id);
		weapon.add(SpaceShip.bullets);
		spaceShip.setTemp(spaceShip.getTemp() + weapon.hotNess);
	}

	@Override
	protected void changeTemp() throws IOException {
		int id = decoder.readInt();
		int temp = decoder.readInt();
		player.otherPlayers.get(id).setTemp(temp);
	}

	@Override
	protected void pause() throws IOException {
		GamePanel.escKey.pause();
		player.sendMessage(MessageManager.PAUSE);
	}

	@Override
	protected void ready() throws IOException {
		player.numOfPaused++;
		if (player.numOfPaused == (ServerPlayer.numOfClients + 1) || ServerPlayer.numOfClients == 0) {
			player.sendMessage(MessageManager.READY);
			player.numOfPaused = 0;
			GamePanel.escKey.ready();
		}
	}

	@Override
	protected void getList() throws IOException {
		prepareList();
	}

	@Override
	protected void checkType() throws IOException {
		int type = decoder.readInt();
		otherSpaceShip.setSeener(type == GameMode.SEENER);
		if (type == GameMode.CLIENT)
			ServerPlayer.numOfClients++;
		sendReceive = true;
	}

	/////////////////////
	/// send messages ///
	/////////////////////

	@Override
	protected void sendJoin() throws IOException {
		encoder.writeByte(JOIN);
		encoder.flush();
		SpaceShip spaceShip = new SpaceShip();
		spaceShip.setId(player.index);
		otherSpaceShip = spaceShip;
		encodeObject.writeObject(spaceShip);
		encodeObject.flush();
		requestType();
		waitToReceive();
		player.lock.writeLock().lock();
		if (!ServerPlayer.inGame || spaceShip.isSeener())
			player.otherPlayers.put(player.index, spaceShip);
		else
			player.waitUntilLevelFinishedPlayers.put(player.index, spaceShip);
		if (spaceShip.isSeener() && ServerPlayer.inGame) {
			sendEnter();
			return;
		}
		player.lock.writeLock().unlock();
		player.index++;
		player.sendMessage(LIST);
	}

	private void waitToReceive() {
		while (!sendReceive) {
		}
		sendReceive = false;
	}

	@Override
	protected void prepareList() throws IOException {
		encoder.writeByte(LIST);
		if (!player.inGame) {
			encoder.writeInt(0);
			encoder.writeInt(0);
			encoder.flush();
		} else {
			encoder.writeInt(GamePanel.level.getLevel());
			encoder.writeInt(GamePanel.level.getWave());
			encoder.flush();
		}
		player.lock.readLock().lock();
		Set<Integer> indexes = prepareSet();
		int number = indexes.size() + 1;
		encoder.writeInt(number);
		completeList(indexes);
		player.lock.readLock().unlock();
	}

	private Set<Integer> prepareSet() {
		Set<Integer> indexes = new HashSet<>();
		for (Iterator<Entry<Integer, SpaceShip>> iterator = player.otherPlayers.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<Integer, SpaceShip> entry = iterator.next();
			indexes.add(entry.getKey());
		}
		for (Iterator<Entry<Integer, SpaceShip>> iterator = player.waitUntilLevelFinishedPlayers.entrySet()
				.iterator(); iterator.hasNext();) {
			Entry<Integer, SpaceShip> entry = iterator.next();
			indexes.add(entry.getKey());
		}
		return indexes;
	}

	private void completeList(Set<Integer> indexes) throws IOException {
		encoder.writeInt(GamePanel.spaceShip.getId());
		encoder.writeInt(GamePanel.spaceShip.getHeart());
		encoder.writeInt(GamePanel.spaceShip.getScore());
		encoder.writeBoolean(false);
		encoder.flush();
		HashMap<Integer, SpaceShip> all = player.otherPlayers;
		all.putAll(player.waitUntilLevelFinishedPlayers);
		for (Iterator<Integer> iterator = indexes.iterator(); iterator.hasNext();) {
			Integer integer = iterator.next();
			SpaceShip spaceShip = all.get(integer);
			encoder.writeInt(integer);
			encoder.writeInt(spaceShip.getHeart());
			encoder.writeInt(spaceShip.getScore());
			encoder.writeBoolean(spaceShip.isSeener());
			encoder.flush();
		}
		if (ServerPlayer.inGame == false)
			player.waitPanel.repaint();
	}

	@Override
	protected void sendEnter() throws IOException {
		encoder.writeByte(ENTER);
		player.lock.readLock().lock();
		int num = player.otherPlayers.size() + 1;
		GamePanel.spaceShip.setX(MainMenu.Width / 2 - GamePanel.spaceShip.getWidth() * (1f * (num - 1) / 2));
		otherSpaceShip.setX(
				MainMenu.Width / 2 - GamePanel.spaceShip.getWidth() * (1f * (num - 1) / 2 - otherSpaceShip.getId()));
		otherSpaceShip.setY(MainMenu.Height - GamePanel.spaceShip.getHeight());
		encoder.writeInt(num);
		player.lock.readLock().unlock();
	}

	@Override
	protected void sendPositions() throws IOException {
		if (player.otherPlayers.containsValue(otherSpaceShip)) {
			encoder.writeByte(POSITIONS);
			encoder.flush();
			player.infoForDraw.determineInfo(player, otherSpaceShip);
			encodeObject.reset();
			encodeObject.flush();
			encodeObject.writeObject(player.infoForDraw);
		}
	}

	@Override
	protected void sendReady() throws IOException {
		if (player.numOfPaused == (ServerPlayer.numOfClients + 1)) {
			super.sendReady();
		} else {
			ready();
		}
	}

	@Override
	protected void requestType() throws IOException {
		encoder.writeByte(REQUEST_TYPE);
		encoder.flush();
	}

	@Override
	protected void sendLose() throws IOException {
		encoder.writeByte(LOSE);
		encoder.flush();
	}
}