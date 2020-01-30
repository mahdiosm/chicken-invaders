package Weapons.Neutron;

import Bullet.Bullet;
import Bullet.Bullets;

public class Neutron0 extends Neutron {
	@Override
	public void add(Bullets bullets) {
		bullets.add(new Bullet(smallNeutronImage, x, y, speed, Math.toRadians(-90), getPower() * midPower, getId()));
	}
}
