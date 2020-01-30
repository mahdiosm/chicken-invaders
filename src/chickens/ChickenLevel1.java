package chickens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import Commons.Common;
import GameFrames.GamePanel;
import network.ServerPlayer;

public class ChickenLevel1 extends Chicken {
	private static final long serialVersionUID = 1L;
	public static final int CL1_WIDTH = 70;
	public static final int CL1_HEIGHT = 70;
	public static Image cl1 = Common.createImage(CL1_WIDTH, CL1_HEIGHT, "resources\\Images\\Chickens\\l1.png");

	public ChickenLevel1() {
		image = cl1;
		firstHealth = 2;
		w = CL1_WIDTH;
		h = CL1_HEIGHT;
		r = 30;
		setEggDelta(2).setEggPercent(2).setHealth(2);
		if (GamePanel.player != null) {
			setHealth((int) Math.floor(Math.sqrt(ServerPlayer.numOfClients + 1)) * 2);
		}
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(cl1, (int) xCenter - w / 2, (int) yCenter - h / 2, null);
	}
}
