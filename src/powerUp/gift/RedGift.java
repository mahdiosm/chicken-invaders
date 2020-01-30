package powerUp.gift;

import java.awt.Graphics;
import java.awt.Image;

import Commons.Common;

public class RedGift extends Gift {
	public static int W = 50, H = 50;
	public static Image blasterImage = Common.createImage(W, H, "resources\\Images\\gifts\\blasterGift.png");

	public RedGift() {
		index = 5;
		w = W;
		h = H;
		r = H;
		weaponType = 1;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(blasterImage, (int) x, (int) y, null);
	}
}
