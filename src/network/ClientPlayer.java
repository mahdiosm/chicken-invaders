package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import GameFrames.GamePanel;
import MainFrame.WaitPanel;

public class ClientPlayer implements Player {
	public ArrayList<InformationForWaitPanel> infoes = new ArrayList<>();
	Thread sender, reciver;
	public InfoForDraw infoForDraw;
	GamePanel game;
	Connection connection;
	public WaitPanel waitPanel;
	boolean running = false;
	public static boolean canJoin = false;
	public int level, wave;
	byte code;

	public ClientPlayer(String ip, int port) throws UnknownHostException, IOException, CantEnterException {
		Socket socket = new Socket(ip, port);
		ClientMessageManger messageManager = new ClientMessageManger(socket, this);
		connection = new Connection(messageManager);
		sendMessage(MessageManager.REQUEST_TYPE);
	}

	@Override
	public void sendMessage(byte code) {
		connection.messageManager.manageSendingMessage(code);
	}

	@Override
	public void closeSocket() {
		try {
			connection.messageManager.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
