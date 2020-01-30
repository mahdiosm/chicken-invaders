package powerUp;

import java.awt.Graphics;
import java.awt.Image;
import Commons.Common;
import GameFrames.SpaceShip;
import powerUp.PowerUp;

public class BulletPowerUp extends PowerUp {
	public static final int W = 40, H = 40;
	public static Image bulletPowerUp = Common.createImage(W, H, "resources\\Images\\powerUps\\bulletPowerUp.png");

	public BulletPowerUp() {
		index = 2;
		w = W;
		h = H;
		r = W;
	}

	@Override
	public void action(SpaceShip p) {
		int power = p.getPower() + 1;
		p.setPower(power);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(bulletPowerUp, (int) x, (int) y, null);
	}
}
