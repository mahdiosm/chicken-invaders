package Weapons.Blaster;

import java.awt.Image;
import Commons.Common;
import Weapons.Weapon;

public abstract class Blaster extends Weapon {
	private static final int BLASTER_WIDTH = 30, BLASTER_HEIGHT = 60;

	public static Image blasterImage = Common.createImage(BLASTER_WIDTH, BLASTER_HEIGHT,
			"resources\\Images\\blaster.png");
	{
		minPower = 1;
		setHotNess(4).setTimeDelay(200).setSpeed(4);
	}

	@Override
	public int getWidth() {
		return BLASTER_WIDTH;
	}

	@Override
	public int getHeigth() {
		return BLASTER_HEIGHT;
	}
}
