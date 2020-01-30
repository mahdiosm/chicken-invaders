package chickens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import Commons.Common;
import GameFrames.GamePanel;
import network.ServerPlayer;

public class ChickenLevel3 extends Chicken {
	public static final int CL3_WIDTH = 125;
	public static final int CL3_HEIGHT = 125;
	public static Image cl3 = Common.createImage(CL3_WIDTH, CL3_HEIGHT, "resources\\Images\\Chickens\\l3.png");

	public ChickenLevel3() {
		image = cl3;
		firstHealth = 5;
		w = CL3_WIDTH;
		h = CL3_HEIGHT;
		r = 50;
		setEggDelta(4).setEggPercent(10).setHealth(5);
		if (GamePanel.player != null) {
			setHealth((int) Math.floor(Math.sqrt(ServerPlayer.numOfClients + 1)) * 5);
		}
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(cl3, (int) xCenter - w / 2, (int) yCenter - h / 2, null);
	}
}
