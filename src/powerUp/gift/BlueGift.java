package powerUp.gift;
import java.awt.Graphics;
import java.awt.Image;
import Commons.Common;

public class BlueGift extends Gift{
	public static int W = 50, H = 50;
	public static Image photonImage = Common.createImage(W, H, "resources\\Images\\gifts\\photonGift.png");
	
	public BlueGift() {
		index = 4;
		w = W;
		h = H;
		r = H;
		weaponType = 3;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(photonImage, (int) x, (int) y, null);
	}
}
