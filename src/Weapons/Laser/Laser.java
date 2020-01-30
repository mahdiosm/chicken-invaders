package Weapons.Laser;

import java.awt.Image;

import Commons.Common;
import Weapons.Weapon;

public abstract class Laser extends Weapon {
	float minP = 1, nimMidP = 1.2f, midP = 1.5f, maxP = 1.8f;
	public static int[] WIDTHES = { 30, 40, 50, 60 };

	public static Image[] images = { Common.createImage(WIDTHES[0], 1, "resources\\Images\\laser weapon\\weak.png"),
			Common.createImage(WIDTHES[1], 1, "resources\\Images\\laser weapon\\medium weak.png"),
			Common.createImage(WIDTHES[2], 1, "resources\\Images\\laser weapon\\medium strong.png"),
			Common.createImage(WIDTHES[3], 1, "resources\\Images\\laser weapon\\strong.png") };

	{
		minP = minPower;
		nimMidP = (float) (1.5 * minP);
		setHotNess(5).setSpeed(1).setTimeDelay(200);
	}

	@Override
	public int getWidth() {
		return WIDTHES[0];
	}

	@Override
	public int getHeigth() {
		return 0;
	}
}
