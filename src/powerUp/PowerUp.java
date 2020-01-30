package powerUp;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;

import GameFrames.Drawable;
import GameFrames.SpaceShip;
import MainFrame.MainMenu;
import powerUp.gift.BlueGift;
import powerUp.gift.GreenGift;
import powerUp.gift.RedGift;

public abstract class PowerUp extends Drawable {
	public static final List<Image> images = Arrays.asList(Coin.coin, MaxHeatChanger.maxHeatChanger,
			BulletPowerUp.bulletPowerUp, GreenGift.neutronImage, BlueGift.photonImage, RedGift.blasterImage);
	protected int speed = 1;
	protected int index = 0;

	public abstract void action(SpaceShip p);

	public void setBeginPoint(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
		xCenter = this.x + w / 2;
		yCenter = this.y + h / 2;
	}
	
	public int getIndex() {
		return index;
	}

	@Override
	public void move() {
		y += speed;
		yCenter += speed;
		if (y > MainMenu.Height) {
			visible = false;
		}
	}
}
