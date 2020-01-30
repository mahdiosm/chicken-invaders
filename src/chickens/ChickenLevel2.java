package chickens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import Commons.Common;
import GameFrames.GamePanel;
import network.ServerPlayer;

public class ChickenLevel2 extends Chicken {
	public static final int CL2_WIDTH = 125;
	public static final int CL2_HEIGHT = 125;
	public static Image cl2 = Common.createImage(CL2_WIDTH, CL2_HEIGHT, "resources\\Images\\Chickens\\l2.png");

	public ChickenLevel2() {
		image = cl2;
		firstHealth = 3;
		w = CL2_WIDTH;
		h = CL2_HEIGHT;
		r = 50;
		setEggDelta(2).setEggPercent(5).setHealth(3);
		if (GamePanel.player != null) {
			setHealth((int) Math.floor(Math.sqrt(ServerPlayer.numOfClients + 1)) * 3);
		}
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(cl2, (int) xCenter - w / 2, (int) yCenter - h / 2, null);
	}
}
