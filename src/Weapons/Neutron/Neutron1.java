package Weapons.Neutron;

import Bullet.Bullet;
import Bullet.Bullets;

public class Neutron1 extends Neutron{
	@Override
	public void add(Bullets bullets) {
		bullets.add(new Bullet(mediumNeutronImage, x, y, speed, Math.toRadians(-90), getPower()*maxPower, getId()));
	}
}
