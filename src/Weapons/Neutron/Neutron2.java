package Weapons.Neutron;

import Bullet.Bullet;
import Bullet.Bullets;

public class Neutron2 extends Neutron {

	@Override
	public void add(Bullets bullets) {
		bullets.add(new Bullet(mediumNeutronImage, x - NEUTRON_WIDTHES[0] / 2, y, speed, Math.toRadians(-90),
				getPower() * maxPower, getId()));
		bullets.add(new Bullet(mediumNeutronImage, x + NEUTRON_WIDTHES[0] / 2, y, speed, Math.toRadians(-90),
				getPower() * maxPower, getId()));
	}

}
