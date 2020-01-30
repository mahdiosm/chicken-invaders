package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import GameFrames.GamePanel;
import GameFrames.SpaceShip;
import MainFrame.GameMode;
import MainFrame.MainFrame;
import MainFrame.MainMenu;

public class MessageManager {
	// message codes
	public static final byte FIRE = 97, ENTER = 98, REQUEST_TYPE = 99, JOIN = 100, POSITION = 101, LOSE = 102,
			PAUSE = 104, READY = 105, LIST = 108, POSITIONS = 109, TEMP_CHANGED = 110;
	public volatile boolean sendReceive = false;
	Socket socket;
	public SpaceShip currentPlayerSpaceShip, otherSpaceShip;
	///////////////////////
	///// --Streams-- /////
	///////////////////////
	InputStream receiverStream;
	OutputStream senderStream;
	DataOutputStream encoder;
	ObjectOutputStream encodeObject;
	DataInputStream decoder;
	ObjectInputStream decodeObject;

	public MessageManager(Socket socket) {
		currentPlayerSpaceShip = GamePanel.spaceShip;
		this.socket = socket;
		try {
			receiverStream = socket.getInputStream();
			senderStream = socket.getOutputStream();
			encoder = new DataOutputStream(senderStream);
			encodeObject = new ObjectOutputStream(senderStream);
			decoder = new DataInputStream(receiverStream);
			decodeObject = new ObjectInputStream(receiverStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MessageManager() {
	}

	public void manageReceivedMessage(byte code) {
		try {
			switch (code) {
			case LOSE:
				lose();
				break;
			case REQUEST_TYPE:
				checkType();
				break;
			case JOIN:
				checkRequest();
				break;
			case TEMP_CHANGED:
				changeTemp();
				break;
			case ENTER:
				enterToTheGame();
				break;
			case FIRE:
				updateBullets();
				break;
			case LIST:
				getList();
				break;
			case POSITION:
				updatePosition();
				break;
			case POSITIONS:
				draw();
				break;
			case PAUSE:
				pause();
				break;
			case READY:
				ready();
				break;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void manageSendingMessage(byte code) {
		try {
			switch (code) {
			case LOSE:
				sendLose();
				break;
			case REQUEST_TYPE:
				requestType();
				break;
			case JOIN:
				sendJoin();
				break;
			case TEMP_CHANGED:
				sendTemp();
				break;
			case FIRE:
				sendBullets();
				break;
			case ENTER:
				sendEnter();
				break;
			case POSITION:
				sendPosition();
				break;
			case LIST:
				prepareList();
				break;
			case POSITIONS:
				sendPositions();
				break;
			case PAUSE:
				sendPause();
				break;
			case READY:
				sendReady();
				break;
			}
		} catch (IOException e) {
			if (GamePanel.spaceShip.getGamePanel() != null && GameMode.type != GameMode.SERVER) {
				GamePanel.spaceShip.getGamePanel().stop();
			}
			if (GameMode.type != GameMode.SERVER) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/////////////////////////////////
	///// --Received messages-- /////
	/////////////////////////////////
	protected void updatePosition() throws IOException {
	}

	protected void checkRequest() throws IOException, ClassNotFoundException {
	}

	protected void draw() throws ClassNotFoundException, IOException {
	}

	protected void getList() throws IOException {
	}

	protected void sendEnter() throws IOException {
	}

	public void enterToTheGame() throws IOException {
	}

	protected void updateBullets() throws IOException {
	}

	protected void changeTemp() throws IOException {
	}

	protected void checkType() throws IOException {
	}

	protected void pause() throws IOException {
	}

	protected void ready() throws IOException {
	}

	protected void lose() throws IOException {
	}

	/////////////////////////////
	///// --Send messages-- /////
	/////////////////////////////
	protected void sendJoin() throws IOException {
	}

	protected void sendPosition() throws IOException {
	}

	protected void sendPositions() throws IOException {
	}

	protected void prepareList() throws IOException {
	}

	protected void sendBullets() throws IOException {
	}

	protected void sendTemp() throws IOException {
	}

	protected void requestType() throws IOException {
	}

	private void sendPause() throws IOException {
		encoder.writeByte(PAUSE);
		encoder.flush();
	}

	protected void sendReady() throws IOException {
		encoder.writeByte(READY);
		encoder.flush();
	}

	protected void sendLose() throws IOException {
	}
}

class Receiver extends Thread {
	MessageManager messageManager;
	byte code;

	public Receiver(MessageManager messageManager) {
		this.messageManager = messageManager;
		setDaemon(true);
	}

	@Override
	public void run() {
		while (true) {
			try {
				code = messageManager.decoder.readByte();
				messageManager.manageReceivedMessage(code);
			} catch (IOException e) {
				try {
					if (GamePanel.spaceShip.getGamePanel() != null && GameMode.type != GameMode.SERVER) {
						GamePanel.spaceShip.getGamePanel().stop();
					}
					if (GameMode.type != GameMode.SERVER) {
						MainFrame.ChangeCurrentPanelWith(new MainMenu());
						MainFrame.showMessage("server closed", "ERROR", JOptionPane.ERROR_MESSAGE);
						messageManager.socket.close();
					} else {
						ServerPlayer.remove(messageManager);
						ServerPlayer.count--;
						if (messageManager.otherSpaceShip.isSeener() == false) {
							ServerPlayer.numOfClients--;
						}
						messageManager.socket.close();
					}
					break;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
