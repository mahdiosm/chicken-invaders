package chickens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import Commons.Common;
import GameFrames.GamePanel;
import network.ServerPlayer;

public class ChickenLevel4 extends Chicken {
	public static final int CL4_WIDTH = 130;
	public static final int CL4_HEIGHT = 130;
	public static Image cl4 = Common.createImage(CL4_WIDTH, CL4_HEIGHT, "resources\\Images\\Chickens\\l4.png");

	public ChickenLevel4() {
		image = cl4;
		firstHealth = 8;
		w = CL4_WIDTH;
		h = CL4_HEIGHT;
		r = 60;
		setEggDelta(4).setEggPercent(20).setHealth(8);
		if (GamePanel.player != null) {
			setHealth((int) Math.floor(Math.sqrt(ServerPlayer.numOfClients + 1)) * 8);
		}
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(cl4, (int) xCenter - w / 2, (int) yCenter - h / 2, null);
	}
}
