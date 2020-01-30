package Weapons.Laser;

import Bullet.Bullets;
import Bullet.LaserBullet;

public class Laser2 extends Laser {
	@Override
	public void add(Bullets bullets) {
		bullets.add(new LaserBullet(images[2], x - 8, y + 5, speed, Math.toRadians(-90), getPower() * midP, getId()));
	}
}
