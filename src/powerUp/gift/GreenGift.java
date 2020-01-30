package powerUp.gift;

import java.awt.Graphics;
import java.awt.Image;

import Commons.Common;

public class GreenGift extends Gift{
	public static int W = 50, H = 50;
	public static Image neutronImage = Common.createImage(W, H, "resources\\Images\\gifts\\neutronGift.png");
	public GreenGift() {
		index = 3;
		w = W;
		h = H;
		r = H;
		weaponType = 2;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(neutronImage, (int) x, (int) y, null);
	}
}
