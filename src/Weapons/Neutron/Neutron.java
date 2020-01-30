package Weapons.Neutron;

import java.awt.Image;
import Commons.Common;
import Weapons.Weapon;

public abstract class Neutron extends Weapon {
	float midPower = 0.7f, maxPower = 0.9f;

	protected static final int[] NEUTRON_WIDTHES = { 30, 35, 40 };
	protected static final int[] NEUTRON_HEIGHTS = { 60, 80, 120 };

	public static Image smallNeutronImage = Common.createImage(NEUTRON_WIDTHES[0], NEUTRON_HEIGHTS[0],
			"resources\\Images\\neutron weapon\\small.png"),
			mediumNeutronImage = Common.createImage(NEUTRON_WIDTHES[1], NEUTRON_HEIGHTS[1],
					"resources\\Images\\neutron weapon\\medium.png"),
			largeNeutronImage = Common.createImage(NEUTRON_WIDTHES[2], NEUTRON_HEIGHTS[2],
					"resources\\Images\\neutron weapon\\larg.png");

	{
		minPower = 0.5f;
		setHotNess(3).setTimeDelay(100).setSpeed(6);
	}

	@Override
	public int getWidth() {
		return NEUTRON_WIDTHES[0];
	}

	@Override
	public int getHeigth() {
		return NEUTRON_HEIGHTS[0];
	}
}
