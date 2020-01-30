package Weapons.Laser;

import Bullet.Bullets;
import Bullet.LaserBullet;

public class Laser0 extends Laser {
	@Override
	public void add(Bullets bullets) {
		bullets.add(new LaserBullet(images[0], x, y, speed, Math.toRadians(-90), getPower()*minP, getId()));
	}
}
