package network;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import GameFrames.GamePanel;
import GameFrames.SpaceShip;
import MainFrame.MainFrame;
import MainFrame.MainMenu;
import MainFrame.WaitPanel;

public class ServerPlayer implements Player {
	public static final int GAME = 1000, PAUSE = 1001, WAIT = 999;
	public static int state = 0;
	public static final List<Color> colors = Arrays.asList(Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN, Color.PINK,
			Color.BLACK, Color.WHITE, Color.ORANGE, Color.GRAY, Color.CYAN);
	static Vector<Connection> clients = new Vector<>();
	public static int numOfClients = 0;
	static public HashMap<Integer, SpaceShip> otherPlayers = new HashMap<>();
	static public HashMap<Integer, SpaceShip> waitUntilLevelFinishedPlayers = new HashMap<>();

	public static final Object connectionLock = new Object();
	ReadWriteLock lock = new ReentrantReadWriteLock();
	public ServerSocket serverSocket;
	public WaitPanel waitPanel;
	static int count = 0, size;
	int index = 0;
	boolean running = true;
	public boolean wantGetMorePlayer = true;
	byte code;
	public static boolean inGame = false;
	int maxLevel, numOfPaused = 0;
	public InfoForDraw infoForDraw;

	public ServerPlayer(int port, int size1, int maxLevel) throws IOException {
		inGame = false;
		size = size1;
		this.maxLevel = maxLevel;
		infoForDraw = new InfoForDraw(this);
		GamePanel.spaceShip = new SpaceShip();
		GamePanel.spaceShip.setId(index);
		index++;
		serverSocket = new ServerSocket(port);
		Thread thread = new Thread(new GetPlayer());
		thread.setDaemon(true);
		thread.start();
	}

	class GetPlayer implements Runnable {

		@Override
		public void run() {
			try {
				while (count < size) {
					Socket socket = serverSocket.accept();
					ServerMessageManager messageManager = new ServerMessageManager(socket, ServerPlayer.this);
					lock.writeLock().lock();
					clients.add(new Connection(messageManager, messageManager.otherSpaceShip));
					count++;
					lock.writeLock().unlock();
					messageManager.manageSendingMessage(MessageManager.JOIN);
				}
			} catch (IOException e) {
			}
		}
	}

	@Override
	public void sendMessage(byte code) {
		if (clients.size() == 0 && code == MessageManager.ENTER) {
			GamePanel.spaceShip.setX(MainMenu.Width / 2 - GamePanel.spaceShip.getWidth() / 2);
			GamePanel game = new GamePanel();
			MainFrame.ChangeCurrentPanelWith(game);
			game.requestFocusInWindow();
			game.run();
		}
		if (code == MessageManager.READY) {
			if (numOfClients == 0) {
				GamePanel.escKey.ready();
			}
			for (Iterator<Connection> iterator = clients.iterator(); iterator.hasNext();) {
				Connection connection = iterator.next();
				connection.messageManager.manageSendingMessage(code);
				break;
			}
			return;
		}
		synchronized (connectionLock) {
			Iterator<Connection> iterator = clients.iterator();
			while (iterator.hasNext()) {
				Connection connection = iterator.next();
				connection.messageManager.manageSendingMessage(code);
			}
		}
	}

	@Override
	public void closeSocket() {
		for (Iterator<Connection> iterator = clients.iterator(); iterator.hasNext();) {
			Connection connection = iterator.next();
			try {
				connection.messageManager.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public HashMap<Integer, SpaceShip> getAllPlayers() {
		HashMap<Integer, SpaceShip> result = new HashMap<>(otherPlayers);
		result.put(GamePanel.spaceShip.getId(), GamePanel.spaceShip);
		return result;
	}

	public static void remove(MessageManager messageManager) {
		synchronized (connectionLock) {
			SpaceShip spaceShip = null;
			for (Iterator<Connection> iterator = clients.iterator(); iterator.hasNext();) {
				Connection connection = iterator.next();
				if (connection.messageManager.equals(messageManager)) {
					spaceShip = connection.messageManager.otherSpaceShip;
					iterator.remove();
					break;
				}
			}
			otherPlayers.remove(spaceShip.getId());
		}
	}

	public static MessageManager findWithSpaceShip(SpaceShip spaceShip) {
		MessageManager messageManager = null;
		for (Iterator<Connection> iterator = clients.iterator(); iterator.hasNext();) {
			Connection connection = iterator.next();
//			System.out.println(connection.otherSpaceShip + " " + spaceShip);
			if (connection.messageManager.otherSpaceShip.equals(spaceShip)) {
				messageManager = connection.messageManager;
				break;
			}
		}
		return messageManager;
	}

	public static void syncPlayers() {
		otherPlayers.putAll(waitUntilLevelFinishedPlayers);
	}
}

class Connection {
	SpaceShip otherSpaceShip;
	MessageManager messageManager;
	Receiver receiver;

	public Connection(MessageManager messageManager) {
		this.messageManager = messageManager;
		receiver = new Receiver(messageManager);
		receiver.start();
	}

	public Connection(MessageManager messageManager, SpaceShip otherSpaceShip) {
		this(messageManager);
		this.otherSpaceShip = otherSpaceShip;
	}
}
