package Weapons.Neutron;

import Bullet.Bullet;
import Bullet.Bullets;

public class Neutron3 extends Neutron {
	@Override
	public void add(Bullets bullets) {
		bullets.add(new Bullet(smallNeutronImage, x - NEUTRON_WIDTHES[1], y, speed, Math.toRadians(-90),
				getPower() * midPower, getId()));
		bullets.add(new Bullet(mediumNeutronImage, x, y, speed, Math.toRadians(-90), getPower() * maxPower, getId()));
		bullets.add(new Bullet(smallNeutronImage, x + NEUTRON_WIDTHES[1], y, speed, Math.toRadians(-90),
				getPower() * midPower, getId()));
	}
}
